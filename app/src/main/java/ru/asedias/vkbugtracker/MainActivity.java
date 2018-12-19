package ru.asedias.vkbugtracker;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.fragments.ReportListFragment;
import ru.asedias.vkbugtracker.fragments.ViewReportFragment;
import ru.asedias.vkbugtracker.ui.MaterialDialogBuilder;
import ru.asedias.vkbugtracker.ui.UIController;

import static ru.asedias.vkbugtracker.ThemeManager.currentTheme;
import static ru.asedias.vkbugtracker.api.API.Prefs;

public class MainActivity extends AppCompatActivity {

    UIController controller;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Actions.ACTION_USER_UPDATED)) {
                controller.LoadUserPhoto();
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
        BugTrackerApp.setAppDisplayMetrix(this);
        if(!LoginActivity.isLoggedOnAndActual()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }
        ErrorController.Setup(this);
        setContentView(R.layout.activity_main);
        this.controller = new UIController().Setup(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Actions.ACTION_USER_UPDATED);
        filter.addAction(Actions.ACTION_COOKIE_UPDATED);
        registerReceiver(this.receiver, filter);
        if(savedInstanceState == null) {
            if(this.getIntent().getBooleanExtra("crash", false)) {
                showCrashLog();
            } else {
                getUserInfo();
                this.controller.ReplaceFragment(new ReportListFragment(), R.id.navigation_reports);
                handleIntent(getIntent());
            }
        }
    }

    private boolean handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            String url = appLinkData.toString();
            if(url.contains("act=show&id=")) {
                String id = url.replaceAll("https:\\/\\/vk\\.com\\/bugtracker\\?act=show&id=([0-9]*)", "$1");
                this.controller.ReplaceFragment(ViewReportFragment.newInstance(Integer.parseInt(id)), R.id.navigation_reports);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.controller.LoadUserPhoto();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Actions.ACTION_USER_UPDATED);
        filter.addAction(Actions.ACTION_COOKIE_UPDATED);
        registerReceiver(this.receiver, filter);
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
                    controller.LoadUserPhoto();
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
        var1.setOnDismissListener(dialog -> controller.ReplaceFragment(new ReportListFragment(), R.id.navigation_reports));
        var1.show();
    }

    @Override
    public void onBackPressed() {
        if(!controller.onBackPressed()) super.onBackPressed();
    }

    public UIController getController() {
        return this.controller;
    }
}
