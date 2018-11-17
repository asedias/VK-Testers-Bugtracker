package ru.asedias.vkbugtracker.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import ru.asedias.vkbugtracker.fragments.FitSystemWindowsFragment;

public class FitSystemWindowsFragmentWrapperFrameLayout extends FrameLayout {

    private boolean mDrawStatusBar;
    private int mInsetTop = 0;
    @ColorInt
    private int mStatusBarColor;
    private Paint mStatusBarPaint;


    public FitSystemWindowsFragmentWrapperFrameLayout(Context var1) {
        super(var1);
    }

    public FitSystemWindowsFragmentWrapperFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FitSystemWindowsFragmentWrapperFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FitSystemWindowsFragmentWrapperFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void dispatchDraw(Canvas var1) {
        super.dispatchDraw(var1);
        if(this.mDrawStatusBar) {
            var1.drawRect(0.0F, 0.0F, (float)this.getWidth(), (float)this.mInsetTop, this.mStatusBarPaint);
        }

    }

    protected boolean fitSystemWindows(Rect var1) {
        if (!isInEditMode()) {
            if(this.mDrawStatusBar) {
                this.mInsetTop = var1.top;
                this.invalidate();
            }

            Fragment var2 = ((Activity)this.getContext()).getFragmentManager().findFragmentById(this.getId());
            if(var2 != null && !(var2 instanceof FitSystemWindowsFragment)) {
                return super.fitSystemWindows(var1);
            } else if(VERSION.SDK_INT < 21) {
                return super.fitSystemWindows(var1);
            } else {
                if(var2 != null && ((FitSystemWindowsFragment)var2).fitSystemWindows(var1)) {
                    var1.set(0, 0, 0, 0);
                }

                return super.fitSystemWindows(var1);
            }
        } else {
            return super.fitSystemWindows(var1);
        }

    }

    public int getInsetTop() {
        return this.mInsetTop;
    }

    public void setDrawStatusBar(boolean var1) {
        if(var1 != this.mDrawStatusBar) {
            this.mDrawStatusBar = var1;
            if(this.mDrawStatusBar && this.mStatusBarPaint == null) {
                this.mStatusBarPaint = new Paint();
            }

            this.mStatusBarPaint.setColor(this.mStatusBarColor);
            this.invalidate();
        }

    }

    public void setStatusBarColor(@ColorInt int var1) {
        if(var1 != this.mStatusBarColor) {
            this.mStatusBarColor = var1;
            if(this.mStatusBarPaint == null) {
                this.mStatusBarPaint = new Paint();
            }

            this.mStatusBarPaint.setColor(this.mStatusBarColor);
            this.invalidate();
        }

    }
}
