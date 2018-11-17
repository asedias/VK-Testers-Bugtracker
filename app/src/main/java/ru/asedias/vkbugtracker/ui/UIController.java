package ru.asedias.vkbugtracker.ui;

import android.animation.LayoutTransition;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.UserData;

/**
 * Created by rorom on 20.10.2018.
 */

public class UIController {

    private FragmentManager fm;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private BottomNavigationViewEx bottomNavView;
    private AppBarLayout appbar;
    private FrameLayout searchView;

    public UIController Setup(MainActivity Main) {
        fm = Main.getFragmentManager();
        /*fm.addOnBackStackChangedListener(() -> {
            if(fm.getBackStackEntryCount() == 0) {
                Main.finish();
            }
        });*/
        if (Build.VERSION.SDK_INT >= 21) {
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN + View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if(Build.VERSION.SDK_INT >= 26) {
                visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                Main.getWindow().setNavigationBarColor(-1);
            }
            Main.getWindow().getDecorView().setSystemUiVisibility(visibility);
        }
        appbar = Main.findViewById(R.id.appBarLayout);
        FrameLayout content = Main.findViewById(R.id.appkit_content);
        content.getLayoutTransition().setDuration(50);
        content.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        tabLayout = Main.findViewById(R.id.tabs);
        toolbar = Main.findViewById(R.id.toolbar);
        toolbar.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        toolbar.inflateMenu(R.menu.main);
        searchView = Main.findViewById(R.id.search_wrap);
        bottomNavView = Main.findViewById(R.id.navigation);
        bottomNavView.enableAnimation(false);
        bottomNavView.enableShiftingMode(false);
        bottomNavView.enableItemShiftingMode(false);
        bottomNavView.setIconSize(24, 24);
        bottomNavView.setIconsMarginTop(24);
        //bottomNavView.setTextVisibility(false);
        bottomNavView.setTextSize(14);
        bottomNavView.setTextTypeface(Fonts.Medium);
        LoadUserPhoto();
        return this;
    }

    public void LoadUserPhoto() {
        Picasso.with(BugTrackerApp.context)
                .load(UserData.getPhoto())
                .transform(new CropCircleTransformation())
                .placeholder(BugTrackerApp.Drawable(R.drawable.placeholder_user))
                .error(BugTrackerApp.Drawable(R.drawable.ic_settings))
                .into((ImageView) toolbar.getMenu().getItem(0).getActionView());
    }

    public FragmentManager getFragmentManager() {
        return fm;
    }

    public void ShowSearch() {
        searchView.setVisibility(View.VISIBLE);
    }

    public void HideSearch() {
        searchView.setVisibility(View.GONE);
    }

    public void ShowTabBar(String... resids) {
        for(int i = 0; i < resids.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(resids[i]);
            tabLayout.addTab(tab);
        }
        tabLayout.setVisibility(View.VISIBLE);
    }

    public void ShowTabBar(int... resids) {
        for(int i = 0; i < resids.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(resids[i]);
            tabLayout.addTab(tab);
        }
        tabLayout.setVisibility(View.VISIBLE);
    }

    public void HideTabBar() {
        tabLayout.setVisibility(View.GONE);
        tabLayout.removeAllTabs();
    }

    public void ReplaceFragment(Fragment fragment) {
        fm.beginTransaction().replace(R.id.appkit_content, fragment).addToBackStack(fragment.getTag()).commit();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public BottomNavigationViewEx getBottomNavView() {
        return bottomNavView;
    }

    public AppBarLayout getAppbar() {
        return appbar;
    }
}
