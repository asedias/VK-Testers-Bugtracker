package ru.asedias.vkbugtracker.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.ui.CardDrawable;
import ru.asedias.vkbugtracker.ui.CardItemDecorator;
import ru.asedias.vkbugtracker.ui.ThemeController;

public abstract class CardRecyclerFragment<I extends RecyclerView.Adapter> extends RecyclerFragment<I> {

   public static final int TABLET_MIN_WIDTH = 924;
   public static final int TABLET_PADDING = 924;
   protected CardItemDecorator decorator;

   protected boolean isTabletDecorator() {
      return this.scrW >= TABLET_MIN_WIDTH;
   }

   public void onAttach(Activity var1) {
      super.onAttach(var1);
      this.updateConfiguration();
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.updateConfiguration();
      this.updateDecorator();
   }

   @Override
   protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      View root = super.OnCreateContentView(inflater, container, savedInstanceState);
      this.updateDecorator();
      return root;
   }

   protected CardItemDecorator onCreateCardDecorator() {
      boolean var3 = this.isTabletDecorator();
      boolean var2;
      if(!var3) {
         var2 = true;
      } else {
         var2 = false;
      }

      CardItemDecorator var5 = new CardItemDecorator(this.mList, var2);
      var5.setPadding(BTApp.dp(2), BTApp.dp(3), BTApp.dp(8), 0);
      var5.setInnerMargins(0, 0, 0, BTApp.dp(4));
      int var1;
      if(var3) {
         var1 = (int) BTApp.dp((float)Math.max(16, (this.scrW - TABLET_PADDING) / 2));
      } else {
         var1 = 0;
      }

      this.mList.setPadding(var1, BTApp.dp(56 + cardOffset), var1, bottomOffset);
      return var5;
   }

   public void onDestroyView() {
      super.onDestroyView();
      this.decorator = null;
   }

   public void onViewCreated(View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
   }

   @Override
   public void onStart() {
      super.onStart();
      if(getView() != null) getView().setBackgroundColor(ThemeController.getValue(ThemeController.KEY_BACKGROUND_CARD));
   }

   protected void updateDecorator() {
      this.mList.removeItemDecoration(this.decorator);
      this.decorator = this.onCreateCardDecorator();
      this.mList.addItemDecoration(this.decorator);
   }
}
