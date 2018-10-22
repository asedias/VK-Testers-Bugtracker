package ru.asedias.vkbugtracker.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.RelativeLayout;

import ru.asedias.vkbugtracker.R;

public class ProportionalFrameLayout extends RelativeLayout {

   private float heightRatio = 1.0F;


   public ProportionalFrameLayout(Context var1) {
      super(var1);
   }

   public ProportionalFrameLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init(var2);
   }

   public ProportionalFrameLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init(var2);
   }

   public ProportionalFrameLayout(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.init(var2);
   }

   private void init(AttributeSet var1) {
      TypedArray var2 = this.getContext().obtainStyledAttributes(var1, new int[]{R.attr.heightRatio});
      this.heightRatio = var2.getFloat(0, 1.0F);
      var2.recycle();
   }

   public float getHeightRatio() {
      return this.heightRatio;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, (int)((float)MeasureSpec.getSize(var1) * this.heightRatio) | 1073741824);
   }

   public void setHeightRatio(float var1) {
      this.heightRatio = var1;
      this.requestLayout();
   }
}
