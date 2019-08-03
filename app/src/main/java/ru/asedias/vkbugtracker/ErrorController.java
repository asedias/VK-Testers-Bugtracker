package ru.asedias.vkbugtracker;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;
import ru.asedias.vkbugtracker.fragments.RecyclerFragment;
import ru.asedias.vkbugtracker.fragments.ReportListFragment;
import ru.asedias.vkbugtracker.fragments.TabbedFragment;
import ru.asedias.vkbugtracker.ui.MaterialDialogBuilder;

import static ru.asedias.vkbugtracker.fragments.LoginFragment.getLoginURL;

/**
 * Created by rorom on 30.10.2018.
 */

public class ErrorController {

    public static MainActivity main;
    private static AlertDialog dialog;
    private static WebView webView;
    private static TextView title;
    private static TextView description;
    public static boolean running = false;

    public static void Setup(MainActivity main) {
        ErrorController.main = main;
    }

    public static void updateCookie() {
        if(!running) showDialog();
    }

    private static View initializeDialogView() {
        View dialogView = main.getLayoutInflater().inflate(R.layout.dialog_cookie, null);
        webView = dialogView.findViewById(R.id.dialog_webview);
        title = dialogView.findViewById(R.id.dialog_title);
        description = dialogView.findViewById(R.id.dialog_description);
        if(UserData.getUID() == 86185582) webView.setVisibility(View.VISIBLE);
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
                    description.setText(R.string.updating_user_info);

                    new GetUserInfo(UserData.getUID(), new Callback<UserInfo>() {
                        @Override
                        public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                            UserInfo data = response.body();
                            try {
                                UserInfo.User user = data.getResponse().get(0);
                                UserData.updateUserData(user);
                                if (dialog != null) dialog.cancel();
                                running = false;
                                Fragment fragment = main.getFragmentManager().findFragmentById(R.id.appkit_content);
                                if(fragment != null) {
                                    if(fragment instanceof TabbedFragment) {
                                        fragment = ((TabbedFragment)fragment).getCurrentFragment();
                                    }
                                    if(fragment instanceof LoaderFragment) {
                                        ((LoaderFragment)fragment).reExecuteRequest();
                                    }
                                }
                            } catch (NullPointerException e) {
                            }
                        }
                        @Override
                        public void onFailure(Call<UserInfo> call, Throwable t) {

                        }
                    }).execute();
                    return;
                }
                if(url.contains(String.valueOf("authorize"))) {
                    BTApp.context.getSharedPreferences("user", 0).edit().clear().apply();
                    ProductsData.clearCacheData();
                    Intent intent = new Intent(main, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    main.startActivity(intent);
                    main.finish();
                    dialog.cancel();
                }
            }
        });
        return dialogView;
    }

    private static void showDialog() {
        running = true;
        main.runOnUiThread(() -> {
            MaterialDialogBuilder builder = new MaterialDialogBuilder(main);
            builder.setCancelable(false);
            builder.setView(initializeDialogView());
            dialog = builder.show();
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            webView.loadUrl(getLoginURL());
        });
    }

}
