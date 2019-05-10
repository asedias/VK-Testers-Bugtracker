package ru.asedias.vkbugtracker.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Roma on 10.05.2019.
 */

public class LayoutHelper {
    
    public static ViewGroup.LayoutParams params(int width, int height) {
        return new ViewGroup.LayoutParams(width, height);
    }
    
    public static ViewGroup.LayoutParams matchParent() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
    
    public static ViewGroup.LayoutParams wrapContent() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    
    public static ViewGroup.LayoutParams fullWidth() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    
    public static ViewGroup.LayoutParams fullHeight() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static RecyclerView.LayoutParams paramsRecycler(int width, int height) {
        return new RecyclerView.LayoutParams(width, height);
    }

    public static RecyclerView.LayoutParams matchParentRecycler() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
    }

    public static RecyclerView.LayoutParams wrapContentRecycler() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    public static RecyclerView.LayoutParams fullWidthRecycler() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    public static RecyclerView.LayoutParams fullHeightRecycler() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT);
    }
    
    public static ViewGroup.MarginLayoutParams margins(ViewGroup.LayoutParams source, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(source);
        mlp.leftMargin = left;
        mlp.topMargin = top;
        mlp.rightMargin = right;
        mlp.bottomMargin = bottom;
        return mlp;
    }

    public static void paddingLeft(View view, int left) {
        view.setPadding(left, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void paddingTop(View view, int top) {
        view.setPadding(view.getPaddingLeft(), top, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void paddingRight(View view, int right) {
        view.setPadding(view.getPaddingLeft(), right, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void paddingBottom(View view, int bottom) {
        view.setPadding(view.getPaddingLeft(), bottom, view.getPaddingRight(), view.getPaddingBottom());
    }
    
}
