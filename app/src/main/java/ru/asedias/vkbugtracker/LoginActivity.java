package ru.asedias.vkbugtracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;

import static ru.asedias.vkbugtracker.api.API.Prefs;

public class LoginActivity extends AppCompatActivity {

    private static final long UPDATE_TIME = 1528985210;
    private static final int client_id = 6605572;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSharedPreferences("user", 0).getString("user_id", "").isEmpty()) clearCookies();
        //clearAndFillCookies();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("blank.html")) {
                    SharedPreferences.Editor editor = BugTrackerApp.context.getSharedPreferences("user", 0).edit();
                    editor.putString("url", url);
                    editor.putLong("time", (System.currentTimeMillis() / 1000));
                    url = url.substring("https://oauth.vk.com/blank.html#".length());
                    String[] result = url.split("&");
                    for (int i = 0; i < result.length; i++) {
                        editor.putString(result[i].split("=")[0], result[i].split("=")[1]);
                    }
                    String cookies = CookieManager.getInstance().getCookie("https://vk.com");
                    editor.putString("cookies", cookies);
                    editor.apply();
                    Prefs();
                    new UserData();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    //getInfoAndStart(UserData.getUID());
                }
            }

        });
        webView.loadUrl(getLoginURL());
    }

    private void getInfoAndStart(String uid) {
        final ProgressDialog dialog = ProgressDialog.show(this, BugTrackerApp.String(R.string.title_activity_login), BugTrackerApp.String(R.string.loading));
        new GetUserInfo(uid, new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo data = response.body();
                try {
                    UserInfo.User user = data.getResponse().get(0);
                    Prefs();
                    UserData.updateUserData(user);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.cancel();
                finish();
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
        SharedPreferences pref = BugTrackerApp.context.getSharedPreferences("user", 0);
        return (pref.contains("access_token"));
    }

    public static boolean isLoggedOnAndActual() {
        try {
            SharedPreferences pref = BugTrackerApp.context.getSharedPreferences("user", 0);
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

    public static void clearCookies()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d("Login", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else
        {
            Log.d("Login", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(BugTrackerApp.context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public static void clearPrefs() {
        BugTrackerApp.context.getSharedPreferences("user", 0).edit().clear().apply();
    }


}
