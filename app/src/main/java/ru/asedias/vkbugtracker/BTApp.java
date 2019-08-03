package ru.asedias.vkbugtracker;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.fragments.LoginFragment;
import ru.asedias.vkbugtracker.ui.Fonts;
import ru.asedias.vkbugtracker.ui.ThemeController;

/**
 * Created by rorom on 11.04.2018.
 */

public class BTApp extends Application {

    public static Context context;
    public static DisplayMetrics mMetrics = new DisplayMetrics();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        new UserData(); new Fonts(); new API(); new ThemeController();
        if(LoginFragment.isLoggedOnAndActual()) {
            ProductsData.updateProducts(false);
        }
        Thread.setDefaultUncaughtExceptionHandler(new RestartExceptionHandler(BTApp.context));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static void setAppDisplayMetrix(Activity activity) {
        activity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    public static int dp(int dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return (int)px;
    }
    public static float dp(float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static String QuantityString(@PluralsRes int id, int quantity, Object... formatArgs) {
        return context.getResources().getQuantityString(id, quantity, formatArgs);
    }

    public static Drawable Drawable(@DrawableRes int id) {
        return context.getResources().getDrawable(id);
    }

    public static ColorStateList ColorStateList(@ColorRes int id) {
        return context.getResources().getColorStateList(id);
    }

    public static String[] StringArray(@ArrayRes int id) { return context.getResources().getStringArray(id); }

    public static String String(@StringRes int id) {
        return context.getResources().getString(id);
    }

    public static int Color(@ColorRes int id) {
        return context.getResources().getColor(id);
    }

    public static int AttrColor(@AttrRes int id) {
        int[] attr = new int[] {id};
        TypedArray a = context.obtainStyledAttributes(attr);
        int value = a.getDimensionPixelSize(0, 0);
        a.recycle();
        return value;
    }

    public static Drawable AttrDrawable(@AttrRes int id) {
        int[] attr = new int[] {id};
        TypedArray a = context.getTheme().obtainStyledAttributes(R.style.AppTheme, attr);
        Drawable drawable = context.getResources().getDrawable(a.getResourceId(0, R.drawable.btn_blue_rounded));
        a.recycle();
        return drawable;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getSystemBarsHeight() {
        return BTApp.getStatusBarHeight() + BTApp.getNavigationBarHeight();
    }

    public static int getNavigationBarHeight() {
        int id = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return id > 0 && context.getResources().getBoolean(id) ? result : 0;
    }

}
