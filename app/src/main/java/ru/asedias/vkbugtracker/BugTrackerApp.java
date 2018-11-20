package ru.asedias.vkbugtracker;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import pl.droidsonroids.retrofit2.JspoonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.ui.Fonts;

/**
 * Created by rorom on 11.04.2018.
 */

public class BugTrackerApp extends Application {

    public static Context context;
    public static DisplayMetrics mMetrics = new DisplayMetrics();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        new UserData(); new Fonts(); new API();
        //new ThemeManager();
        Thread.setDefaultUncaughtExceptionHandler(new RestartExceptionHandler(BugTrackerApp.context));
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

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavigationBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
