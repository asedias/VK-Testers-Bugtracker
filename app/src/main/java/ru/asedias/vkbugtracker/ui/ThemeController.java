package ru.asedias.vkbugtracker.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.R;

/**
 * Created by Roma on 09.05.2019.
 */

public class ThemeController {

    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BTApp.context);
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static int currentTheme = THEME_LIGHT;
    public static final String KEY_BACKGROUND = "backgroundColor";
    public static final String KEY_BACKGROUND_CARD = "backgroundCardColor";
    public static final String KEY_PRIMARY = "primaryColor";
    public static final String KEY_TEXTCOLOR = "textColor";
    public static final String KEY_ACCENT = "accentColor";
    public static final String KEY_THEME = "theme";
    public static final String KEY_CARD_COLOR = "cardColor";
    public static final String KEY_WINDOW_BACKGROUND = "backgroundWindow";

    public ThemeController() {
        currentTheme = preferences.getInt(KEY_THEME, THEME_LIGHT);
        if(currentTheme > 1) currentTheme = THEME_LIGHT;
    }

    public static void setTheme(int theme) {
        currentTheme = theme;
        preferences.edit().putInt(KEY_THEME, theme).apply();
    }

    public static boolean isDark() {
        return currentTheme == THEME_DARK;
    }

    public static int getPrimaryColor() {
        return getValue(KEY_PRIMARY, currentTheme);
    }

    public static int getAccentColor() {
        return BTApp.Color(R.color.colorAccent);
    }

    public static int getTextSecondaryColor() {
        return BTApp.Color(R.color.colorNavIconsLight);
    }

    public static int getTextColor() {
        return getValue(KEY_TEXTCOLOR, currentTheme);
    }

    public static int getBackground() {
        return getValue(KEY_BACKGROUND, currentTheme);
    }

    public static int getCardBackground() {
        return getValue(KEY_BACKGROUND_CARD, currentTheme);
    }

    public static int getValue(String key) {
        return getValue(key, currentTheme);
    }

    public static int getValue(String key, int theme) {
        if(key.equals(KEY_BACKGROUND) || key.equals(KEY_CARD_COLOR)) {
            return theme == THEME_LIGHT ? Color.WHITE : BTApp.Color(R.color.colorDarkBackground);
        } else if(key.equals(KEY_PRIMARY)) {
            return BTApp.Color(theme == THEME_LIGHT ? R.color.colorPrimaryLight : R.color.colorPrimaryDark);
        } else if(key.equals(KEY_TEXTCOLOR)) {
            return theme == THEME_LIGHT ? Color.BLACK : Color.WHITE;
        } else if(key.equals(KEY_ACCENT)) {
            return BTApp.Color(R.color.colorAccent);
        } else if(key.equals(KEY_BACKGROUND_CARD)) {
            return theme == THEME_LIGHT ? -1315344 : Color.parseColor("#ff0a0a0a");
        } else if(key.equals(KEY_WINDOW_BACKGROUND)) {
            return theme == THEME_LIGHT ? Color.WHITE : Color.parseColor("#FF272728");
        }
        return 0;
    }

}
