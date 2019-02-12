package ru.asedias.vkbugtracker;

import android.animation.LayoutTransition;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import ru.asedias.vkbugtracker.fragments.NotificationsFragment;
import ru.asedias.vkbugtracker.fragments.ProductsFragment;
import ru.asedias.vkbugtracker.fragments.RecyclerFragment;
import ru.asedias.vkbugtracker.fragments.ReportListFragment;
import ru.asedias.vkbugtracker.fragments.UpdatesListFragment;
import ru.asedias.vkbugtracker.ui.BottomNavigationViewEx;
import ru.asedias.vkbugtracker.ui.Fonts;

import static ru.asedias.vkbugtracker.ThemeManager.currentTheme;

/**
 * Created by rorom on 11.02.2019.
 */

public class FragmentStackActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private BottomNavigationViewEx bottomNavView;
    private AppBarLayout appbar;
    private FrameLayout searchView;
    private HashMap<Integer, Stack<Fragment>> mStacks;
    private ArrayList<String> queue;
    private int currentID;
    private int navIconRes = R.drawable.ic_logo;
    private View.OnClickListener navClick = v -> {
        if(navIconRes == R.drawable.ic_ab_back_arrow_dark) onBackPressed();
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            mStacks = new HashMap<>();
            queue = new ArrayList<>();
            currentID = R.id.navigation_reports;
            mStacks.put(R.id.navigation_reports, new Stack<Fragment>());
            mStacks.put(R.id.navigation_products, new Stack<Fragment>());
            mStacks.put(R.id.navigation_members, new Stack<Fragment>());
            mStacks.put(R.id.navigation_updates, new Stack<Fragment>());
        }
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN + View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if(Build.VERSION.SDK_INT >= 26) {
                if(currentTheme == R.style.AppTheme) {
                    visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                }
                this.getWindow().setNavigationBarColor(BTApp.AttrColor(R.attr.colorPrimary));
            }
            this.getWindow().getDecorView().setSystemUiVisibility(visibility);
        }
        this.appbar = this.findViewById(R.id.appBarLayout);
        FrameLayout content = this.findViewById(R.id.appkit_content);
        content.getLayoutTransition().setDuration(50);
        content.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        this.tabLayout = this.findViewById(R.id.tabs);
        this.toolbar = this.findViewById(R.id.toolbar);
        this.toolbar.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        this.toolbar.inflateMenu(R.menu.main);
        this.searchView = this.findViewById(R.id.search_wrap);
        this.bottomNavView = this.findViewById(R.id.navigation);
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
            this.startActivity(new Intent(this, SettingsActivity.class));
        });
    }

    public void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.appkit_content, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }


    public void replaceFragment(Fragment fragment, int navID) {
        navID = navID == 0 ? currentID: navID;
        mStacks.get(navID).push(fragment);
        queue.remove(String.valueOf(navID));
        queue.add(String.valueOf(navID));
        if(fragment.getArguments() == null) fragment.setArguments(new Bundle());
        fragment.getArguments().putBoolean("top", mStacks.get(navID).size() == 1);
        showFragment(fragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(currentID == item.getItemId()) {
            if(mStacks.get(item.getItemId()).size() > 1) {
                mStacks.get(item.getItemId()).setSize(1);
                showFragment(mStacks.get(item.getItemId()).lastElement());
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
            showFragment(mStacks.get(item.getItemId()).lastElement());
            return true;
        }
        switch (item.getItemId()) {
            case R.id.navigation_reports: {
                replaceFragment(ReportListFragment.newInstance(), item.getItemId());
                return true;
            }
            case R.id.navigation_products: {
                replaceFragment(ProductsFragment.newInstance(), item.getItemId());
                return true;
            }
            case R.id.navigation_members: {
                replaceFragment(new NotificationsFragment(), item.getItemId());
                return true;
            }
            case R.id.navigation_updates: {
                replaceFragment(UpdatesListFragment.newInstance(false), item.getItemId());
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        mStacks.get(currentID).pop();
        if(mStacks.get(currentID).empty()) {
            queue.remove(String.valueOf(currentID));
            if(queue.size() == 0) {
                super.onBackPressed();
                return;
            }
            currentID = Integer.valueOf(queue.get(queue.size()-1));
        }
        showFragment(mStacks.get(currentID).lastElement());
        bottomNavView.getMenu().findItem(currentID).setChecked(true);
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

    public void showSearch() {
        searchView.setVisibility(View.VISIBLE);
    }

    public void hideSearch() {
        searchView.setVisibility(View.GONE);
    }

    public void showNavBack() {
        navIconRes = R.drawable.ic_ab_back_arrow_dark;
        toolbar.setNavigationIcon(navIconRes);
    }

    public void showNavLogo() {
        navIconRes = R.drawable.ic_logo;
        toolbar.setNavigationIcon(navIconRes);
    }


    public void showTabBar() {
        tabLayout.setVisibility(View.VISIBLE);
    }

    public void showTabBar(String... resids) {
        for(int i = 0; i < resids.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(resids[i]);
            tabLayout.addTab(tab);
        }
        tabLayout.setVisibility(View.VISIBLE);
    }

    public void showTabBar(int... resids) {
        for(int i = 0; i < resids.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(resids[i]);
            tabLayout.addTab(tab);
        }
        tabLayout.setVisibility(View.VISIBLE);
    }

    public void hideTabBar() {
        tabLayout.setVisibility(View.GONE);
        tabLayout.removeAllTabs();
    }
}
