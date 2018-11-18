package ru.asedias.vkbugtracker.ui;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class DividerItemDecoration extends ItemDecoration {

   private Drawable bottom;
   private int bottomHeight;
   private Drawable divider;
   private int height;
   private Provider provider;
   private Drawable top;
   private int topHeight;
   private boolean useDecoratedVBounds;
   private int paddingLeft = 0;
   private int paddingRight = 0;

   public DividerItemDecoration(Drawable var1) {
      this(var1, var1.getIntrinsicHeight());
   }

   public DividerItemDecoration(Drawable var1, int var2) {
      this.divider = var1;
      this.height = var2;
   }

   public DividerItemDecoration(Drawable var1, int var2, Drawable var3, int var4, Drawable var5, int var6) {
      this(var1, var2);
      this.top = var3;
      this.topHeight = var4;
      this.bottom = var5;
      this.bottomHeight = var6;
   }

   public DividerItemDecoration(Drawable var1, Drawable var2, Drawable var3) {
      this(var1, var1.getIntrinsicHeight(), var2, var2.getIntrinsicHeight(), var3, var3.getIntrinsicHeight());
   }

   private int getItemBottom(View var1, RecyclerView var2) {
      return this.useDecoratedVBounds?var2.getLayoutManager().getDecoratedBottom(var1):var1.getBottom() + this.height;
   }

   private int getItemTop(View var1, RecyclerView var2) {
      return this.useDecoratedVBounds?var2.getLayoutManager().getDecoratedTop(var1):var1.getTop();
   }

   public void getItemOffsets(Rect var1, View var2, RecyclerView var3, State var4) {
      var1.set(0, 0, 0, 0);
      int var5 = var3.getChildAdapterPosition(var2);
      if(var5 == 0) {
         var1.top += this.topHeight;
      }

      if(var5 == var3.getAdapter().getItemCount() - 1) {
         if(this.bottomHeight > 0) {
            var1.bottom += this.bottomHeight;
         }
      } else if(this.provider == null || var5 < var3.getAdapter().getItemCount() && this.provider.needDrawDividerAfter(var5)) {
         var1.bottom += this.height;
         return;
      }

   }

   public DividerItemDecoration setPaddingLeft(int marginLeft) {
      this.paddingLeft = marginLeft;
      return this;
   }
   public DividerItemDecoration setPaddingRight(int marginRight) {
      this.paddingRight = marginRight;
      return this;
   }

   public boolean isUseDecoratedVBounds() {
      return this.useDecoratedVBounds;
   }

   public void onDrawOver(Canvas var1, RecyclerView var2, State var3) {
      LayoutManager var7 = var2.getLayoutManager();

      for(int var4 = 0; var4 < var7.getChildCount(); ++var4) {
         View var6 = var7.getChildAt(var4);
         int margin = 0;
         RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) var6.getLayoutParams();
         int var5 = var7.getPosition(var6);
         if(this.provider != null && this.provider.needMarginBottom(var5)) {
            margin = lp.bottomMargin;
         }
         int left = var6.getLeft() + this.paddingLeft;
         int right = var6.getRight() - this.paddingRight;
         if(var5 == 0 && this.top != null) {
            this.top.setBounds(left, var6.getTop() - this.topHeight, right, var6.getTop());
            this.top.draw(var1);
         }

         if(var5 == var2.getAdapter().getItemCount() - 1) {
            if(this.bottom != null) {
               this.bottom.setBounds(left, var6.getBottom(), right, var6.getBottom() + this.bottomHeight);
               this.bottom.draw(var1);
            }
         } else if(this.provider == null || var5 < var2.getAdapter().getItemCount() && this.provider.needDrawDividerAfter(var5)) {
            this.divider.setBounds(left, this.getItemBottom(var6, var2) - this.height + margin, right, this.getItemBottom(var6, var2) + margin);
            this.divider.draw(var1);
         }
      }

   }

   public DividerItemDecoration setProvider(Provider var1) {
      this.provider = var1;
      return this;
   }

   public void setUseDecoratedVBounds(boolean var1) {
      this.useDecoratedVBounds = var1;
   }

   public interface Provider {

      boolean needDrawDividerAfter(int var1);

      @Nullable boolean needMarginBottom(int var1);
   }
}
