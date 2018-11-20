package ru.asedias.vkbugtracker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;
import ru.asedias.vkbugtracker.fragments.ReportListFragment;
import ru.asedias.vkbugtracker.ui.UIController;

import static ru.asedias.vkbugtracker.api.API.Prefs;

public class MainActivity extends AppCompatActivity {

    UIController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme_Dark);
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
        if(savedInstanceState == null) {
            if(this.getIntent().getBooleanExtra("crash", false)) {
                showCrashLog();
            } else {
                getUserInfo();
                this.controller.ReplaceFragment(new ReportListFragment(), R.id.navigation_reports);
            }
        }
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
        AlertDialog.Builder var1 = new AlertDialog.Builder(this);
        var1.setTitle("Crash Log");
        var1.setMessage(this.getIntent().getStringExtra("crash_info"));
        var1.setPositiveButton("COPY", (var11, var2) -> {
            ((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("crash", getIntent().getStringExtra("crash_info")));
            var11.dismiss();
        });
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
