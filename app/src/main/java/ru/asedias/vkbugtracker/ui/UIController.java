package ru.asedias.vkbugtracker.ui;

import android.animation.LayoutTransition;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.SettingsActivity;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.fragments.NotificationsFragment;
import ru.asedias.vkbugtracker.fragments.ProductsFragment;
import ru.asedias.vkbugtracker.fragments.RecyclerFragment;
import ru.asedias.vkbugtracker.fragments.ReportListFragment;
import ru.asedias.vkbugtracker.fragments.UpdatesListFragment;

import static ru.asedias.vkbugtracker.ThemeManager.currentTheme;

/**
 * Created by rorom on 20.10.2018.
 */

public class UIController implements BottomNavigationView.OnNavigationItemSelectedListener {

    private MainActivity main;
    private FragmentManager fm;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private BottomNavigationViewEx bottomNavView;
    private AppBarLayout appbar;
    private FrameLayout searchView;
    private static HashMap<Integer, Stack<Fragment>> mStacks;
    private static List<String> queue;
    private static int currentID;
    private int navIconRes = R.drawable.ic_logo;
    private View.OnClickListener navClick = v -> {
        if(navIconRes == R.drawable.ic_ab_back_arrow_dark) main.onBackPressed();
    };

    public UIController Setup(MainActivity Main) {
        this.main = Main;
        this.fm = Main.getFragmentManager();
        if (Build.VERSION.SDK_INT >= 21) {
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN + View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if(Build.VERSION.SDK_INT >= 26) {
                if(currentTheme == R.style.AppTheme) {
                    visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                }
                Main.getWindow().setNavigationBarColor(BugTrackerApp.AttrColor(R.attr.colorPrimary));
            }
            Main.getWindow().getDecorView().setSystemUiVisibility(visibility);
        }
        this.appbar = Main.findViewById(R.id.appBarLayout);
        FrameLayout content = Main.findViewById(R.id.appkit_content);
        content.getLayoutTransition().setDuration(50);
        content.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        this.tabLayout = Main.findViewById(R.id.tabs);
        this.toolbar = Main.findViewById(R.id.toolbar);
        this.toolbar.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        this.toolbar.inflateMenu(R.menu.main);
        this.searchView = Main.findViewById(R.id.search_wrap);
        this.bottomNavView = Main.findViewById(R.id.navigation);
        this.bottomNavView.enableAnimation(false);
        this.bottomNavView.enableShiftingMode(false);
        this.bottomNavView.enableItemShiftingMode(false);
        this.bottomNavView.setIconSize(24, 24);
        this.bottomNavView.setIconsMarginTop(24);
        this.bottomNavView.setTextSize(14);
        this.bottomNavView.setTextTypeface(Fonts.Medium);
        this.bottomNavView.setOnNavigationItemSelectedListener(this);
        this.toolbar.setNavigationOnClickListener(navClick);
        this.toolbar.getMenu().getItem(0).getActionView().setOnClickListener(v -> {
            Main.startActivity(new Intent(Main, SettingsActivity.class));
            //ThemeManager.changeTheme(Main);
        });
        if(mStacks == null) {
            mStacks = new HashMap<>();
            queue = new ArrayList<>();
            currentID = R.id.navigation_reports;
            mStacks.put(R.id.navigation_reports, new Stack<Fragment>());
            mStacks.put(R.id.navigation_products, new Stack<Fragment>());
            mStacks.put(R.id.navigation_members, new Stack<Fragment>());
            mStacks.put(R.id.navigation_updates, new Stack<Fragment>());
        }
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

    public int getFragmentSize(int navID) {
        if(mStacks.get(navID) != null) {
            return mStacks.get(navID).size();
        }
        return 0;
    }

    public boolean onBackPressed() {
        mStacks.get(currentID).pop();
        if(mStacks.get(currentID).empty()) {
            queue.remove(String.valueOf(currentID));
            if(queue.size() == 0) return false;
            currentID = Integer.valueOf(queue.get(queue.size()-1));
        }
        ShowFragment(mStacks.get(currentID).lastElement());
        getBottomNavView().getMenu().findItem(currentID).setChecked(true);
        return true;
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

    public void ShowNavBack() {
        navIconRes = R.drawable.ic_ab_back_arrow_dark;
        getToolbar().setNavigationIcon(navIconRes);
    }

    public void ShowNavLogo() {
        navIconRes = R.drawable.ic_logo;
        getToolbar().setNavigationIcon(navIconRes);
    }


    public void ShowTabBar() {
        tabLayout.setVisibility(View.VISIBLE);
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

    public void ShowFragment(Fragment fragment) {
        fm.beginTransaction().replace(R.id.appkit_content, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    public void ReplaceFragment(Fragment fragment, int navID) {
        navID = navID == 0 ? currentID: navID;
        mStacks.get(navID).push(fragment);
        queue.remove(String.valueOf(navID));
        queue.add(String.valueOf(navID));
        if(fragment.getArguments() == null) fragment.setArguments(new Bundle());
        fragment.getArguments().putBoolean("top", mStacks.get(navID).size() == 1);
        ShowFragment(fragment);
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

    public int getCurrentID() {
        return currentID;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(currentID == item.getItemId()) {
            if(mStacks.get(item.getItemId()).size() > 1) {
                mStacks.get(item.getItemId()).setSize(1);
                ShowFragment(mStacks.get(item.getItemId()).lastElement());
            } else {
                Fragment fr = mStacks.get(item.getItemId()).lastElement();
                if(fr instanceof RecyclerFragment) {
                    ((RecyclerFragment)fr).setScrollToTop();
                }
            }
            return true;
        }
        currentID = item.getItemId();
        if(mStacks.get(item.getItemId()).size() > 0) {
            queue.remove(String.valueOf(item.getItemId()));
            queue.add(String.valueOf(item.getItemId()));
            ShowFragment(mStacks.get(item.getItemId()).lastElement());
            return true;
        }
        switch (item.getItemId()) {
            case R.id.navigation_reports: {
                ReplaceFragment(ReportListFragment.newInstance(), item.getItemId());
                return true;
            }
            case R.id.navigation_products: {
                ReplaceFragment(ProductsFragment.newInstance(), item.getItemId());
                return true;
            }
            case R.id.navigation_members: {
                ReplaceFragment(new NotificationsFragment(), item.getItemId());
                return true;
            }
            case R.id.navigation_updates: {
                ReplaceFragment(UpdatesListFragment.newInstance(false), item.getItemId());
                return true;
            }
        }
        return false;
    }
}
