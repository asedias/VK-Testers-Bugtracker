package ru.asedias.vkbugtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import ru.asedias.vkbugtracker.fragments.ReportListFragment;
import ru.asedias.vkbugtracker.ui.UIController;

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
            this.controller.ReplaceFragment(new ReportListFragment(), R.id.navigation_reports);
        }
    }

    @Override
    public void onBackPressed() {
        if(!controller.onBackPressed()) super.onBackPressed();
    }

    public UIController getController() {
        return this.controller;
    }
}
