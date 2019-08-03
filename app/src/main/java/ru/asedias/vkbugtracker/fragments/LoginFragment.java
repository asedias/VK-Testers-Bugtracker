package ru.asedias.vkbugtracker.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.Actions;
import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.ui.LayoutHelper;
import ru.asedias.vkbugtracker.ui.drawer.DrawerAdapter;

/**
 * Created by Roma on 10.05.2019.
 */

public class LoginFragment extends LoaderFragment {

    private static final long UPDATE_TIME = 1528985210;
    public static final int client_id = 6605572;

    public LoginFragment() {
        this.showBottom = false;
        this.title = BTApp.String(R.string.title_activity_login);
        this.logo = null;
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(this.parent.getSharedPreferences("user", 0).getString("user_id", "").isEmpty()) clearCookies();
        this.parent.setDrawerEnabled(false);
        WebView webView = new WebView(this.parent);
        webView.setLayoutParams(LayoutHelper.margins(LayoutHelper.matchParent(), 0, BTApp.dp(56), 0, 0));
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("blank.html")) {
                    UserData.cookie = CookieManager.getInstance().getCookie("https://vk.com");

                    SharedPreferences.Editor editor = BTApp.context.getSharedPreferences("user", 0).edit();
                    editor.putString("url", url);
                    editor.putLong("time", (System.currentTimeMillis() / 1000));
                    editor.putString("cookies", UserData.cookie);
                    url = url.substring("https://oauth.vk.com/blank.html#".length());
                    String[] result = url.split("&");
                    for (int i = 0; i < result.length; i++) {
                        String[] data = result[i].split("=");
                        String key = data[0];
                        String value = data[1];
                        if(key.equals("access_token")) {
                            UserData.accessToken = value;
                        } else if(key.equals("user_id")) {
                            UserData.uid = Integer.parseInt(value);
                        }
                        editor.putString(key, value);
                    }
                    editor.apply();

                    BTApp.context.sendBroadcast(new Intent(Actions.ACTION_COOKIE_UPDATED));
                    parent.spliceStack(R.id.navigation_reports, 0);
                    parent.replaceFragment(new ReportListFragment(), R.id.navigation_reports);
                    parent.setDrawerEnabled(true);
                    new DrawerAdapter(parent.getDrawerLayout(), parent.getDrawerList(), parent.getLayoutInflater());
                    parent.getDrawerLayout().setStatusBarBackgroundColor(0);
                }
            }

        });
        webView.loadUrl(getLoginURL());
        return webView;
    }

    private void getInfoAndStart(String uid) {
        final ProgressDialog dialog = ProgressDialog.show(parent, BTApp.String(R.string.title_activity_login), BTApp.String(R.string.loading));
        new GetUserInfo(uid, new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo data = response.body();
                try {
                    UserInfo.User user = data.getResponse().get(0);
                    UserData.updateUserData(user);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(parent, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.cancel();
                parent.finish();
            }
            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        }).execute();

    }

    public static String getLoginURL() {
        return "https://oauth.vk.com/authorize?" +
                "client_id=" + client_id +
                "&scope=196608" +
                "&display=mobile" +
                "&v=5.47" +
                "&response_type=token";
    }

    public static boolean isLoggedOn() {
        SharedPreferences pref = BTApp.context.getSharedPreferences("user", 0);
        return (pref.contains("access_token"));
    }

    public static boolean isLoggedOnAndActual() {
        try {
            SharedPreferences pref = BTApp.context.getSharedPreferences("user", 0);
            long time = pref.getLong("time", 0);
            boolean needLogin = false;
            if (time < UPDATE_TIME) {
                clearCookies();
                needLogin = true;
            }
            return (pref.contains("access_token") && !needLogin);
        } catch(ClassCastException e) {
            e.printStackTrace();
            clearPrefs();
            clearCookies();
            return false;
        }
    }

    public static void clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else
        {
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(BTApp.context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public static void clearPrefs() {
        BTApp.context.getSharedPreferences("user", 0).edit().clear().apply();
    }

}
