package ru.asedias.vkbugtracker;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rorom on 13.05.2018.
 */

public class ThemeManager {
    public static int currentTheme = R.style.AppTheme;
    public static int currentPrimary = LightPrimary();
    public static int currentTextColor = Color.BLACK;
    public static int currentBackground = Color.WHITE;
    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BTApp.context);
    private static String KEY_THEME = "currentTheme";

    public ThemeManager() {
        currentTheme = preferences.getInt(KEY_THEME, R.style.AppTheme);
        if(currentTheme != R.style.AppTheme && currentTheme != R.style.AppTheme_Dark) {
            currentTheme = R.style.AppTheme_Dark;
        }
        saveTheme();
        updateValues();
    }

    private static void updateValues() {
        currentPrimary = currentTheme == R.style.AppTheme ? LightPrimary() : DarkPrimary();
        currentTextColor = currentTheme == R.style.AppTheme ? Color.BLACK : Color.WHITE;
        currentBackground = currentTheme == R.style.AppTheme ? Color.WHITE : DarkBackground();
    }

    private static void updateTheme() {
        currentTheme = (currentTheme == R.style.AppTheme) ? R.style.AppTheme_Dark : R.style.AppTheme;
        updateValues();
        saveTheme();
    }

    private static void saveTheme() {
        preferences.edit().putInt(KEY_THEME, currentTheme).apply();
    }

    private static int LightPrimary() {
        return BTApp.Color(R.color.colorPrimaryLight);
    }

    private static int DarkPrimary() {
        return BTApp.Color(R.color.colorPrimaryDark);
    }

    private static int DarkBackground() { return BTApp.Color(R.color.colorDarkBackground); }

    private static void getAllViews(ViewGroup viewGroup) {
        int newPrimary = currentTheme == R.style.AppTheme ? DarkPrimary() : LightPrimary();
        int newTextColor = currentTheme == R.style.AppTheme ? Color.WHITE : Color.BLACK;
        for(int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            boolean staticColorView = view.getId() == android.support.design.R.id.design_menu_item_text ||
                            view.getId() == R.id.title_holder ||
                            view.getId() == R.id.profile_subtitle ||
                            view.getId() == R.id.tag ||
                            view.getId() == R.id.time;
            boolean change = view instanceof TextView && ((TextView)view).getCurrentTextColor() == currentTextColor;
            if(view instanceof TextView && change) {
                ObjectAnimator anim = ObjectAnimator.ofInt((TextView)view, "textColor", currentTextColor, newTextColor);
                anim.setEvaluator(new ArgbEvaluator());
                anim.setDuration(400).start();
            } else if(view instanceof ViewGroup) {
                if(view.getId() == R.id.toolbar || view.getId() == R.id.navigation) {
                    ObjectAnimator anim = ObjectAnimator.ofInt(view, "backgroundColor", currentPrimary, newPrimary);
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.setDuration(400).start();
                }
                if(view.getId() != R.id.navigation) getAllViews((ViewGroup) view);
            }
        }
    }

    public static void changeTheme(final MainActivity Main) {
        /*int newPrimary = currentTheme == R.style.AppTheme ? DarkPrimary() : LightPrimary();
        ObjectAnimator anim = ObjectAnimator.ofInt(Main.getWindow().getDecorView(), "backgroundColor", currentPrimary, newPrimary);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setDuration(400).start();
        anim.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) { }
            @Override public void onAnimationEnd(Animator animation) {
                if (Build.VERSION.SDK_INT >= 21) {
                    int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN + View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    if(Build.VERSION.SDK_INT >= 26) {
                        if(currentTheme != R.style.AppTheme) {
                            visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                        }
                        Main.getWindow().setNavigationBarColor(BTApp.AttrColor(R.attr.colorPrimary));
                    }
                    Main.getWindow().getDecorView().setSystemUiVisibility(visibility);
                }
                updateTheme();
            }

            @Override public void onAnimationCancel(Animator animation) { }
            @Override public void onAnimationRepeat(Animator animation) { }
        });
        getAllViews((ViewGroup) Main.getWindow().getDecorView());*/
        updateTheme();
        Main.recreate();
    }

}
