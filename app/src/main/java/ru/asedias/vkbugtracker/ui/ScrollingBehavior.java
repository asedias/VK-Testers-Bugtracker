package ru.asedias.vkbugtracker.ui;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ru.asedias.vkbugtracker.BTApp;

/**
 * Created by rorom on 13.02.2019.
 */

public class ScrollingBehavior extends CoordinatorLayout.Behavior<View> {

    public ScrollingBehavior() {
    }

    public ScrollingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        lp.height = BTApp.mMetrics.heightPixels + BTApp.dp(56) - BTApp.getStatusBarHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.requestLayout();
        child.setTranslationY(dependency.getY());
        return super.onDependentViewChanged(parent, child, dependency);
    }

}
