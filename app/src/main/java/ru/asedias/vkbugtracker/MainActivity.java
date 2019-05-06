package ru.asedias.vkbugtracker;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.fragments.ReportListFragment;
import ru.asedias.vkbugtracker.fragments.ViewReportFragment;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.MaterialDialogBuilder;
import ru.asedias.vkbugtracker.ui.drawer.DrawerAdapter;

import static ru.asedias.vkbugtracker.ThemeManager.currentTheme;
import static ru.asedias.vkbugtracker.api.API.Prefs;

public class MainActivity extends FragmentStackActivity {

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Actions.ACTION_USER_UPDATED)) {
                loadUserPhoto();
            }
            if(intent.getAction().equals(Actions.ACTION_COOKIE_UPDATED)) {
                ProductsData.updateProducts(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(currentTheme);
        super.onCreate(savedInstanceState);
        BTApp.setAppDisplayMetrix(this);
        if(!LoginActivity.isLoggedOnAndActual()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }
        ErrorController.Setup(this);
        if(savedInstanceState == null) {
            if(this.getIntent().getBooleanExtra("crash", false)) {
                showCrashLog();
            } else {
                getUserInfo();
                this.replaceFragment(new ReportListFragment(), R.id.navigation_reports);
                handleIntent(getIntent());
            }
        }
        getToolbar().getMenu().getItem(0).getActionView().setOnClickListener(v -> {
            this.startActivity(new Intent(this, SettingsActivity.class));
        });
        DrawerAdapter adapter = new DrawerAdapter(this.findViewById(R.id.drawer_layout), this.findViewById(R.id.drawer_list), getLayoutInflater());
    }

    private boolean handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            String url = appLinkData.toString();
            if(url.contains("bugtracker?act=show&id=")) {
                String id = url.replaceAll("https:\\/\\/vk\\.com\\/bugtracker\\?act=show&id=([0-9]*)", "$1");
                this.replaceFragment(ViewReportFragment.newInstance(Integer.parseInt(id)), R.id.navigation_reports);
            }
            if(url.matches("vk\\.com\\/bug([0-9]*)")) {
                String id = url.replaceAll("https:\\/\\/vk\\.com\\/bug([0-9]*)", "$1");
                this.replaceFragment(ViewReportFragment.newInstance(Integer.parseInt(id)), R.id.navigation_reports);
            }
            return true;
        }
        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Actions.ACTION_USER_UPDATED);
        filter.addAction(Actions.ACTION_COOKIE_UPDATED);
        registerReceiver(this.receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(this.receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loadUserPhoto();
    }

    public void loadUserPhoto() {
        Picasso.with(BTApp.context)
                .load(UserData.getPhoto())
                .transform(new CropCircleTransformation())
                .placeholder(BTApp.Drawable(R.drawable.placeholder_user))
                .error(BTApp.Drawable(R.drawable.ic_settings))
                .into((ImageView) getToolbar().getMenu().getItem(0).getActionView());
    }

    private void getUserInfo() {
        new GetUserInfo(UserData.getUID(), new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo data = response.body();
                try {
                    UserInfo.User user = data.getResponse().get(0);
                    Prefs();
                    UserData.updateUserData(user);
                    loadUserPhoto();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        }).execute();
    }

    private void showCrashLog() {
        MaterialDialogBuilder var1 = new MaterialDialogBuilder(this);
        var1.setTitle(R.string.crash);
        var1.setMessage(this.getIntent().getStringExtra("crash_info"));
        var1.setPositiveButton(android.R.string.copy, (var11, var2) -> {
            ((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("crash", getIntent().getStringExtra("crash_info")));
            var11.dismiss();
        });
        var1.setPositiveWarning(false);
        var1.setOnDismissListener(dialog -> {
            replaceFragment(new ReportListFragment(), R.id.navigation_reports);
            ProductsData.updateProducts(false);
        });
        var1.show();
    }
}
