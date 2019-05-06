package ru.asedias.vkbugtracker;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
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
    private FrameLayout appbarBgContainer;
    private CardView appbarCard;
    private TabLayout tabLayout;
    private BottomNavigationViewEx bottomNavView;
    private AppBarLayout appbar;
    private DrawerLayout drawerLayout;
    private View searchText;
    private HashMap<Integer, Stack<Fragment>> mStacks;
    private ArrayList<String> queue;
    private int currentID;
    protected int navIconRes = R.drawable.ic_logo;
    protected View.OnClickListener navClick = v -> {
        if(navIconRes == R.drawable.ic_ab_back_arrow_dark) {
            onBackPressed();
        } else {
            if(drawerLayout.getDrawerLockMode(Gravity.START) != DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            drawerLayout.openDrawer(Gravity.START);
        }
    };
    private int toolbarState = STATE_FULL_WIDTH;
    private final static int STATE_FULL_WIDTH = 0;
    private final static int STATE_IN_CARD = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            mStacks = new HashMap<>();
            queue = new ArrayList<>();
            currentID = R.id.navigation_reports;
            mStacks.put(R.id.navigation_reports, new Stack<Fragment>());
            mStacks.put(R.id.navigation_products, new Stack<Fragment>());
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
        this.tabLayout = this.findViewById(R.id.tabs);
        this.appbarBgContainer = this.findViewById(R.id.appbarBackground);
        this.appbarCard = this.findViewById(R.id.appbarCard);
        this.toolbar = this.findViewById(R.id.toolbar);
        this.bottomNavView = this.findViewById(R.id.navigation);
        this.bottomNavView.enableAnimation(false);
        this.bottomNavView.enableShiftingMode(false);
        this.bottomNavView.enableItemShiftingMode(false);
        this.bottomNavView.setIconSize(24, 24);
        this.bottomNavView.setIconsMarginTop(24);
        this.bottomNavView.setTextSize(14);
        this.bottomNavView.setTextTypeface(Fonts.Medium);
        this.bottomNavView.setOnNavigationItemSelectedListener(this);
        new QBadgeView(this)
                .setBadgeNumber(11)
                .setGravityOffset(42, 4, true)
                .bindTarget(this.bottomNavView.getBottomNavigationItemView(2))
                .setBadgeTextSize(11, true)
                .setBadgePadding(4, true)
                .setShowShadow(false);
        this.toolbar.setNavigationOnClickListener(navClick);
        this.toolbar.inflateMenu(R.menu.menu);
        this.drawerLayout = this.findViewById(R.id.drawer_layout);
        this.searchText = this.findViewById(R.id.search_text);
    }

    public void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.appkit_content, fragment).commit();
    }


    public void replaceFragment(Fragment fragment, int navID) {
        navID = navID == 0 ? currentID: navID;
        if(navID != currentID) bottomNavView.setCurrentItem(bottomNavView.getMenuItemPosition(bottomNavView.getMenu().findItem(navID)));
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
            case R.id.navigation_updates: {
                replaceFragment(UpdatesListFragment.newInstance(false), item.getItemId());
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
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
        } else {
            drawerLayout.closeDrawers();
        }

    }

    public void setDrawerEnabled(boolean state) {
        drawerLayout.setDrawerLockMode(state ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public int getToolbarState() {
        return toolbarState;
    }

    public void setToolbarState(int toolbarState) {
        this.toolbarState = toolbarState;
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

    private int cardCurHeight;
    private AnimatorSet cardCurAnimSet;

    public void toolbarToCard() { this.toolbarToCard(0); }

    public void toolbarToCard(int offsetDP) {
        if(getToolbarState() != STATE_IN_CARD) {
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator radiusAnim = ObjectAnimator.ofFloat(appbarCard, "radius", BTApp.dp(8));
            int newHeight = BTApp.dp(50 + offsetDP);
            ValueAnimator heightAnim = ValueAnimator.ofInt(cardCurHeight, newHeight);
            heightAnim.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                appbarCard.getLayoutParams().height = value;
                appbarCard.requestLayout();
            });
            cardCurHeight = newHeight;
            ValueAnimator paddingAnim = ValueAnimator.ofInt(0, BTApp.dp(16));
            paddingAnim.addUpdateListener(animation -> {
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) appbarCard.getLayoutParams();
                mlp.rightMargin = (int) animation.getAnimatedValue();
                mlp.leftMargin = (int) animation.getAnimatedValue();
                mlp.topMargin = (int) ((int) animation.getAnimatedValue() / 2.6);
                toolbar.setPadding((int) animation.getAnimatedValue(), (int) ((int)animation.getAnimatedValue()/5.0F), (int) ((int)animation.getAnimatedValue()*1.4F), 0);
            });
            ValueAnimator shadowAnim = ValueAnimator.ofFloat(BTApp.dp(8), BTApp.dp(4));
            shadowAnim.addUpdateListener(animation -> {
                ViewCompat.setElevation(appbarCard, (Float) animation.getAnimatedValue());
            });
            animatorSet.setDuration(300);
            animatorSet.playTogether(radiusAnim, heightAnim, paddingAnim, shadowAnim);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animation) { }
                @Override public void onAnimationEnd(Animator animation) {
                    setToolbarState(STATE_IN_CARD);
                }
                @Override public void onAnimationCancel(Animator animation) { }
                @Override public void onAnimationRepeat(Animator animation) { }
            });
            if(cardCurAnimSet != null) cardCurAnimSet.cancel();
            cardCurAnimSet = animatorSet;
            cardCurAnimSet.start();
            searchText.setVisibility(View.VISIBLE);
        } else {
            int newHeight = BTApp.dp(50 + offsetDP);
            ValueAnimator heightAnim = ValueAnimator.ofInt(cardCurHeight, newHeight);
            heightAnim.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                appbarCard.getLayoutParams().height = value;
                appbarCard.requestLayout();
            });
            cardCurHeight = newHeight;
            heightAnim.setDuration(300);
            heightAnim.start();
        }
    }

    public void toolbarToFullWidth() { this.toolbarToFullWidth(0); }

    public void toolbarToFullWidth(int offsetDP) {
        if(getToolbarState() != STATE_FULL_WIDTH) {
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(appbarCard, "radius", 0);
            int newHeight = BTApp.dp(56 + offsetDP);
            ValueAnimator animator2 = ValueAnimator.ofInt(cardCurHeight, newHeight);
            animator2.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                appbarCard.getLayoutParams().height = value;
                appbarCard.requestLayout();
            });
            cardCurHeight = newHeight;
            ValueAnimator animator3 = ValueAnimator.ofInt(BTApp.dp(16), 0);
            animator3.addUpdateListener(animation -> {
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) appbarCard.getLayoutParams();
                mlp.rightMargin = (int) animation.getAnimatedValue();
                mlp.leftMargin = (int) animation.getAnimatedValue();
                mlp.topMargin = (int) ((int) animation.getAnimatedValue() / 2.6);
                toolbar.setPadding((int) animation.getAnimatedValue(), (int) ((int)animation.getAnimatedValue()/5.0F), (int) ((int)animation.getAnimatedValue()*1.4F), 0);
            });
            ValueAnimator animator4 = ValueAnimator.ofFloat(BTApp.dp(4), BTApp.dp(8));
            animator4.addUpdateListener(animation -> {
                ViewCompat.setElevation(appbarCard, (Float) animation.getAnimatedValue());
            });
            animatorSet.setDuration(300);
            animatorSet.playTogether(animator1, animator2, animator3, animator4);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animation) { }
                @Override public void onAnimationEnd(Animator animation) {
                    setToolbarState(STATE_FULL_WIDTH);
                }
                @Override public void onAnimationCancel(Animator animation) { }
                @Override public void onAnimationRepeat(Animator animation) { }
            });
            if(cardCurAnimSet != null) cardCurAnimSet.cancel();
            cardCurAnimSet = animatorSet;
            cardCurAnimSet.start();
            searchText.setVisibility(View.GONE);
        } else {
            int newHeight = BTApp.dp(56 + offsetDP);
            ValueAnimator heightAnim = ValueAnimator.ofInt(cardCurHeight, newHeight);
            heightAnim.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                appbarCard.getLayoutParams().height = value;
                appbarCard.requestLayout();
            });
            cardCurHeight = newHeight;
            heightAnim.setDuration(300);
            heightAnim.start();
        }
    }

    public void showNavBack() {
        navIconRes = R.drawable.ic_ab_back_arrow_dark;
        toolbar.setNavigationIcon(navIconRes);
    }

    public void showNavLogo(boolean menu) {
        navIconRes = menu ? R.drawable.ic_menu : R.drawable.ic_logo;
        toolbar.setNavigationIcon(navIconRes);
    }


    public void showTabBar() {
        tabLayout.setVisibility(View.VISIBLE);
        /*ValueAnimator margin = ValueAnimator.ofInt(BTApp.dp(-48), 0);
        margin.addUpdateListener(animation -> {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) tabLayout.getLayoutParams();
            mlp.topMargin = (int) animation.getAnimatedValue();
        });
        ValueAnimator alpha = ValueAnimator.ofFloat(0F, 1F);
        alpha.addUpdateListener(animation -> tabLayout.setAlpha((Float) animation.getAnimatedValue()));
        AnimatorSet set = new AnimatorSet();
        set.playTogether(margin, alpha);
        set.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) { }
            @Override public void onAnimationEnd(Animator animation) { }
            @Override public void onAnimationCancel(Animator animation) { }
            @Override public void onAnimationRepeat(Animator animation) { }
        });
        set.setDuration(300);
        set.start();*/
    }

    public void showTabBar(String... resids) {
        for(int i = 0; i < resids.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(resids[i]);
            tabLayout.addTab(tab);
        }
        showTabBar();
    }

    public void showTabBar(int... resids) {
        for(int i = 0; i < resids.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(resids[i]);
            tabLayout.addTab(tab);
        }
        showTabBar();
    }

    public void hideTabBar() {
        /*ValueAnimator margin = ValueAnimator.ofInt(0, BTApp.dp(-48));
        margin.addUpdateListener(animation -> {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) tabLayout.getLayoutParams();
            mlp.topMargin = (int) animation.getAnimatedValue();
        });
        ValueAnimator alpha = ValueAnimator.ofFloat(1F, 0F);
        alpha.addUpdateListener(animation -> tabLayout.setAlpha((Float) animation.getAnimatedValue()));
        AnimatorSet set = new AnimatorSet();
        set.playTogether(margin, alpha);
        set.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) { }
            @Override public void onAnimationEnd(Animator animation) {
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) tabLayout.getLayoutParams();
                mlp.topMargin = 0;
                tabLayout.setAlpha(1F);
                tabLayout.setVisibility(View.GONE);
                tabLayout.removeAllTabs();
            }
            @Override public void onAnimationCancel(Animator animation) { }
            @Override public void onAnimationRepeat(Animator animation) { }
        });
        set.setDuration(300);
        set.start();*/
        tabLayout.setVisibility(View.GONE);
    }
}
