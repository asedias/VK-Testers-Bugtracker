package ru.asedias.vkbugtracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.ui.BottomNavigationViewEx;
import ru.asedias.vkbugtracker.ui.MaterialDialogBuilder;

import static ru.asedias.vkbugtracker.ThemeManager.currentTheme;

public class SettingsActivity extends AppCompatPreferenceActivity {
    
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private BottomNavigationViewEx bottomNavView;
    private AppBarLayout appbar;
    private FrameLayout content;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(currentTheme);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN + View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if(Build.VERSION.SDK_INT >= 26) {
                if(currentTheme == R.style.AppTheme) {
                    visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                }
                getWindow().setNavigationBarColor(BugTrackerApp.AttrColor(R.attr.colorPrimary));
            }
            getWindow().getDecorView().setSystemUiVisibility(visibility);
        }
        View root = getLayoutInflater().inflate(R.layout.activity_main, null);
        this.appbar = root.findViewById(R.id.appBarLayout);
        this.tabLayout = root.findViewById(R.id.tabs);
        this.toolbar = root.findViewById(R.id.toolbar);
        this.bottomNavView = root.findViewById(R.id.navigation);
        this.toolbar.removeAllViews();
        this.toolbar.setNavigationIcon(R.drawable.ic_ab_back_arrow_dark);
        this.toolbar.setNavigationOnClickListener(v -> finish());
        this.toolbar.setTitle(R.string.action_settings);
        this.bottomNavView.setVisibility(View.GONE);
        this.content = root.findViewById(R.id.appkit_content);
        ((CoordinatorLayout.LayoutParams)content.getLayoutParams()).bottomMargin = 0;
        this.list = new ListView(this);
        this.list.setId(android.R.id.list);
        this.content.addView(this.list);
        setContentView(root);
        addPreferencesFromResource(R.xml.pref_general);

        getPreferenceScreen().findPreference("dark_theme").setOnPreferenceChangeListener((preference, newValue) -> {
            int newTheme = (Boolean)newValue ? R.style.AppTheme_Dark : R.style.AppTheme;
            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("currentTheme", newTheme).apply();
            Toast.makeText(this, R.string.restart_settings, Toast.LENGTH_SHORT).show();
            return true;
        });

        Preference debug = getPreferenceScreen().findPreference("debug_settings");
        getPreferenceScreen().findPreference("debug").setOnPreferenceChangeListener((preference, newValue) -> {
            UserData.debugEnabled = (Boolean)newValue;
            if(!UserData.debugEnabled) {
                getPreferenceScreen().removePreference(debug);
            } else {
                getPreferenceScreen().addPreference(debug);
            }
            ((BaseAdapter)getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
            return true;
        });

        if(!UserData.debugEnabled) getPreferenceScreen().removePreference(debug);

        getPreferenceScreen().findPreference("logout").setOnPreferenceClickListener(preference -> {
            MaterialDialogBuilder builder = new MaterialDialogBuilder(this);
            builder.setTitle(R.string.prefs_logout);
            builder.setMessage(R.string.logout_description);
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                BugTrackerApp.context.getSharedPreferences("user", 0).edit().clear().apply();
                ProductsData.clearCacheData();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                dialog.cancel();
            });
            builder.show();
            return true;
        });

    }

}
