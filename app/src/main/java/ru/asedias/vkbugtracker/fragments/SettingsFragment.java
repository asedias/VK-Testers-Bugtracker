package ru.asedias.vkbugtracker.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.ui.MaterialDialogBuilder;
import ru.asedias.vkbugtracker.ui.adapters.SettingsAdapter;

import static ru.asedias.vkbugtracker.ui.ThemeController.KEY_BACKGROUND;
import static ru.asedias.vkbugtracker.ui.ThemeController.KEY_BACKGROUND_CARD;
import static ru.asedias.vkbugtracker.ui.ThemeController.KEY_PRIMARY;
import static ru.asedias.vkbugtracker.ui.ThemeController.KEY_TEXTCOLOR;
import static ru.asedias.vkbugtracker.ui.ThemeController.THEME_DARK;
import static ru.asedias.vkbugtracker.ui.ThemeController.THEME_LIGHT;
import static ru.asedias.vkbugtracker.ui.ThemeController.currentTheme;
import static ru.asedias.vkbugtracker.ui.ThemeController.getValue;
import static ru.asedias.vkbugtracker.ui.ThemeController.isDark;
import static ru.asedias.vkbugtracker.ui.ThemeController.setTheme;

/**
 * Created by Roma on 08.05.2019.
 */

public class SettingsFragment extends CardRecyclerFragment<SettingsAdapter> {

    private List<SettingsAdapter.SettingsItem> data = new ArrayList<>();
    public SettingsFragment() {
        this.title = BTApp.String(R.string.action_settings);
        this.setTitleNeeded = true;
        this.showBottom = false;
        buildData();
        this.mAdapter = new SettingsAdapter(this.data);
    }

    @Override
    public WebRequest getRequest() {
        this.showContent();
        this.mSwipeRefresh.setEnabled(false);
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    private void buildData() {
        this.data.add(new SettingsAdapter.SettingsItem(R.string.general_settings));
        /*this.data.add(new SettingsItem(R.string.debug_switch, BTApp.Drawable(R.drawable.ic_detail), UserData.debugEnabled, (buttonView, isChecked) -> {
            UserData.debugEnabled = isChecked;
            notifyDataSetChanged();
        }));
        if(UserData.debugEnabled) {
            this.data.add(new SettingsItem(R.string.debug_settings));*/
        SharedPreferences def = PreferenceManager.getDefaultSharedPreferences(BTApp.context);
        this.data.add(new SettingsAdapter.SettingsItem(R.string.dark_theme, isDark(), (buttonView, isChecked) -> {
            setTheme(isChecked ? THEME_DARK : THEME_LIGHT);
            animateChanges();
            //Toast.makeText(parentActivity, R.string.restart_settings, Toast.LENGTH_SHORT).show();
        }));
        //}
        //this.data.add(new SettingsItem(R.string.change_theme, () -> {}));
        this.data.add(new SettingsAdapter.SettingsItem(R.string.account_settings));
        this.data.add(new SettingsAdapter.SettingsItem(R.string.prefs_logout, () -> {
            MaterialDialogBuilder builder = new MaterialDialogBuilder(this.parent);
            builder.setTitle(R.string.prefs_logout);
            builder.setMessage(R.string.logout_description);
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                ProductsData.clearCacheData();
                UserData.clear();
                parent.clearStacks();
                parent.replaceFragment(new LoginFragment(), R.id.navigation_reports);
                dialog.cancel();
            });
            builder.show();
        }));
    }

    private void animateChanges() {
        int newTheme = currentTheme;
        int prevTheme = isDark() ? THEME_LIGHT : THEME_DARK;
        Log.e("THEME", "Change from " + prevTheme + " to " + newTheme);
        ValueAnimator v1 = ValueAnimator.ofInt(getValue(KEY_PRIMARY, prevTheme), getValue(KEY_PRIMARY, newTheme));
        v1.setEvaluator(new ArgbEvaluator());
        v1.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            this.parent.getAppbarCard().setCardBackgroundColor(value);
            this.parent.getBottomNavView().setBackgroundColor(value);
            this.parent.getDrawerList().setBackgroundColor(value);
        });
        getAllText((ViewGroup) this.parent.getFragmentManager().findFragmentById(R.id.appkit_content).getView());
        ValueAnimator v2 = ValueAnimator.ofInt(getValue(KEY_BACKGROUND, prevTheme), getValue(KEY_BACKGROUND, newTheme));
        v2.setEvaluator(new ArgbEvaluator());
        v2.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            this.parent.getWindow().setBackgroundDrawable(new ColorDrawable(value));
        });
        ValueAnimator v3 = ValueAnimator.ofInt(getValue(KEY_BACKGROUND_CARD, prevTheme), getValue(KEY_BACKGROUND_CARD, newTheme));
        v3.setEvaluator(new ArgbEvaluator());
        v3.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            this.parent.getFragmentManager().findFragmentById(R.id.appkit_content).getView().setBackgroundColor(value);
        });
        ValueAnimator v4 = ValueAnimator.ofInt(getValue(KEY_TEXTCOLOR, prevTheme), getValue(KEY_TEXTCOLOR, newTheme));
        v4.setEvaluator(new ArgbEvaluator());
        v4.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            this.parent.getToolbar().setTitleTextColor(value);
        });
        AnimatorSet set = new AnimatorSet();
        set.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                updateDecorator();
            }
            @Override public void onAnimationEnd(Animator animation) { }
            @Override public void onAnimationCancel(Animator animation) { }
            @Override public void onAnimationRepeat(Animator animation) { }
        });
        set.setDuration(200);
        set.playTogether(v1, v2, v3, v4);
        set.start();
        //notifyDataSetChanged();
        this.parent.getDrawerList().getAdapter().notifyDataSetChanged();
    }

    private static void getAllText(ViewGroup viewGroup) {
        int newTheme = currentTheme;
        int prevTheme = isDark() ? THEME_LIGHT : THEME_DARK;
        for(int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if(view instanceof TextView && ((TextView)view).getCurrentTextColor() == getValue(KEY_TEXTCOLOR, prevTheme)) {
                ObjectAnimator anim = ObjectAnimator.ofInt((TextView)view, "textColor", getValue(KEY_TEXTCOLOR, prevTheme), getValue(KEY_TEXTCOLOR, newTheme));
                anim.setEvaluator(new ArgbEvaluator());
                anim.setDuration(400).start();
            } else if(view instanceof ViewGroup) {
                getAllText((ViewGroup) view);
            }
        }
    }

}
