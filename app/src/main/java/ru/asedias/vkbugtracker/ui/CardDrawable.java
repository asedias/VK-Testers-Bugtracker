package ru.asedias.vkbugtracker.ui;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;

import ru.asedias.vkbugtracker.BTApp;

public class CardDrawable extends Drawable {

   private static final float SHADOW_MULTIPLIER = 1.5F;
   private float mCornerRadius;
   private Paint mCornerShadowPaint;
   private Path mCornerShadowPath;
   private boolean mDirty;
   private Paint mEdgeShadowPaint;
   private Paint mPaint;
   private final RectF mPreShadowBounds;
   private final int mShadowEndColor;
   private float mShadowSize;
   private final int mShadowStartColor;
   private final boolean mWideMode;
   private final RectF sCornerRect;


   public CardDrawable(Resources var1) {
      this(var1, -1);
   }

   public CardDrawable(Resources var1, int var2) {
      this(var1, var2, (float) BTApp.dp(2.0F));
   }

   public CardDrawable(Resources var1, int var2, float var3) {
      this(var1, var2, var3, false);
   }

   public CardDrawable(Resources var1, int var2, float var3, boolean var4) {
      this.sCornerRect = new RectF();
      this.mDirty = true;
      this.mShadowStartColor = 419430400;
      this.mShadowEndColor = 0;
      this.mShadowSize = (float) BTApp.dp(1.66F) * 1.5F;
      this.mPaint = new Paint(5);
      this.mPaint.setColor(var2);
      this.mCornerShadowPaint = new Paint(5);
      this.mCornerShadowPaint.setStyle(Style.FILL);
      this.mCornerShadowPaint.setDither(true);
      this.mCornerRadius = var3;
      this.mPreShadowBounds = new RectF();
      this.mEdgeShadowPaint = new Paint(this.mCornerShadowPaint);
      this.mWideMode = var4;
   }

   private void buildComponents(Rect var1) {
      this.mPreShadowBounds.set((float)var1.left + this.mShadowSize, (float)var1.top + this.mShadowSize, (float)var1.right - this.mShadowSize, (float)var1.bottom - this.mShadowSize);
      this.buildShadowCorners();
   }

   private void buildShadowCorners() {
      RectF var8 = new RectF(-this.mCornerRadius, -this.mCornerRadius, this.mCornerRadius, this.mCornerRadius);
      RectF var9 = new RectF(var8);
      var9.inset(-this.mShadowSize, -this.mShadowSize);
      if(this.mCornerShadowPath == null) {
         this.mCornerShadowPath = new Path();
      } else {
         this.mCornerShadowPath.reset();
      }

      this.mCornerShadowPath.setFillType(FillType.EVEN_ODD);
      this.mCornerShadowPath.moveTo(-this.mCornerRadius, 0.0F);
      this.mCornerShadowPath.rLineTo(-this.mShadowSize, 0.0F);
      this.mCornerShadowPath.arcTo(var9, 180.0F, 90.0F, false);
      this.mCornerShadowPath.arcTo(var8, 270.0F, -90.0F, false);
      this.mCornerShadowPath.close();
      float var1 = this.mCornerRadius / (this.mCornerRadius + this.mShadowSize);
      Paint var10 = this.mCornerShadowPaint;
      float var2 = this.mCornerRadius;
      float var3 = this.mShadowSize;
      int var5 = this.mShadowStartColor;
      int var6 = this.mShadowStartColor;
      int var7 = this.mShadowEndColor;
      TileMode var11 = TileMode.CLAMP;
      var10.setShader(new RadialGradient(0.0F, 0.0F, var2 + var3, new int[]{var5, var6, var7}, new float[]{0.0F, var1, 1.0F}, var11));
      var10 = this.mEdgeShadowPaint;
      var1 = -this.mCornerRadius;
      var2 = this.mShadowSize;
      var3 = -this.mCornerRadius;
      float var4 = this.mShadowSize;
      var5 = this.mShadowStartColor;
      var6 = this.mShadowStartColor;
      var7 = this.mShadowEndColor;
      var11 = TileMode.CLAMP;
      var10.setShader(new LinearGradient(0.0F, var1 + var2, 0.0F, var3 - var4, new int[]{var5, var6, var7}, new float[]{0.0F, 0.5F, 1.0F}, var11));
   }

   private void drawShadow(Canvas var1) {
      int var6 = var1.save();
      float var3 = 2.0F * (this.mCornerRadius + this.mShadowSize);
      float var2 = -this.mCornerRadius - this.mShadowSize;
      Rect var7 = this.getBounds();
      float var4;
      if(!this.mWideMode) {
         var1.translate(this.mPreShadowBounds.left + this.mCornerRadius, this.mPreShadowBounds.top + this.mCornerRadius);
         var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
         var1.drawRect(0.0F, var2, (float)var7.width() - var3, -this.mCornerRadius, this.mEdgeShadowPaint);
         var1.rotate(180.0F);
         var1.translate((float)(-var7.width()) + var3, (float)(-var7.height()) + var3);
         var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
         var4 = (float)var7.width();
         float var5 = -this.mCornerRadius;
         var1.drawRect(0.0F, var2, var4 - var3, this.mShadowSize + var5, this.mEdgeShadowPaint);
         var1.rotate(90.0F);
         var1.translate(0.0F, (float)(-var7.width()) + var3);
         var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
         var1.drawRect(0.0F, var2, (float)var7.height() - var3, -this.mCornerRadius, this.mEdgeShadowPaint);
         var1.rotate(180.0F);
         var1.translate((float)(-var7.height()) + var3, (float)(-var7.width()) + var3);
         var1.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
         var1.drawRect(0.0F, var2, (float)var7.height() - var3, -this.mCornerRadius, this.mEdgeShadowPaint);
      } else {
         var1.translate(0.0F, this.mPreShadowBounds.top + this.mCornerRadius);
         var1.drawRect(0.0F, var2, (float)var7.width(), -this.mCornerRadius, this.mEdgeShadowPaint);
         var1.rotate(180.0F);
         var1.translate((float)(-var7.right), (float)(-var7.height()) + var3);
         var3 = (float)var7.width();
         var4 = -this.mCornerRadius;
         var1.drawRect(0.0F, var2, var3, this.mShadowSize + var4, this.mEdgeShadowPaint);
      }

      var1.restoreToCount(var6);
   }

   public void draw(Canvas var1) {
      if(this.mDirty) {
         this.buildComponents(this.getBounds());
         this.mDirty = false;
      }

      this.drawShadow(var1);
      float var2;
      if(!this.mWideMode) {
         int var4 = var1.save();
         var2 = this.mShadowSize * 0.6666666F;
         float var3 = this.mShadowSize - var2;
         var1.translate(0.0F, -var2);
         RectF var5 = this.mPreShadowBounds;
         var5.bottom += var2;
         var5 = this.mPreShadowBounds;
         var5.left -= var3;
         var5 = this.mPreShadowBounds;
         var5.right += var3;
         this.mPreShadowBounds.top = (float)Math.round(this.mPreShadowBounds.top);
         this.mPreShadowBounds.bottom = (float)Math.round(this.mPreShadowBounds.bottom);
         this.drawRoundRect(var1, this.mPreShadowBounds, this.mCornerRadius, this.mPaint);
         var5 = this.mPreShadowBounds;
         var5.bottom -= var2;
         var5 = this.mPreShadowBounds;
         var5.left += var3;
         var5 = this.mPreShadowBounds;
         var5.right -= var3;
         var1.translate(0.0F, var2);
         var1.restoreToCount(var4);
      } else {
         var2 = this.mShadowSize;
         var1.drawRect(0.0F, this.mPreShadowBounds.top - var2 * 0.6666666F, (float)var1.getWidth(), this.mPreShadowBounds.bottom, this.mPaint);
      }
   }

   public void drawRoundRect(Canvas var1, RectF var2, float var3, Paint var4) {
      if(VERSION.SDK_INT >= 17) {
         var1.drawRoundRect(var2, var3, var3, var4);
      } else {
         float var5 = var3 * 2.0F;
         float var6 = var2.width() - var5;
         float var7 = var2.height();
         this.sCornerRect.set(var2.left, var2.top, var2.left + 2.0F * var3, var2.top + 2.0F * var3);
         var1.drawArc(this.sCornerRect, 180.0F, 90.0F, true, var4);
         this.sCornerRect.offset(var6, 0.0F);
         var1.drawArc(this.sCornerRect, 270.0F, 90.0F, true, var4);
         this.sCornerRect.offset(0.0F, var7 - var5);
         var1.drawArc(this.sCornerRect, 0.0F, 90.0F, true, var4);
         this.sCornerRect.offset(-var6, 0.0F);
         var1.drawArc(this.sCornerRect, 90.0F, 90.0F, true, var4);
         var1.drawRect(var2.left + var3, var2.top, var2.right - var3, var2.top + var3, var4);
         var1.drawRect(var2.left + var3, var2.bottom - var3, var2.right - var3, var2.bottom, var4);
         var1.drawRect(var2.left, var2.top + var3, var2.right, var2.bottom - var3, var4);
      }
   }

   public int getOpacity() {
      return PixelFormat.OPAQUE;
   }

   public boolean getPadding(@NonNull Rect var1) {
      int var3 = (int)Math.ceil((double)(this.mShadowSize * 0.33333334F));
      int var2;
      if(this.mWideMode) {
         var2 = 0;
      } else {
         var2 = (int)Math.ceil((double)(this.mShadowSize - (float)var3));
      }

      var1.set(var2, var3, var2, (int)Math.ceil((double)this.mShadowSize));
      return true;
   }

   public int getRadius() {
      return (int)this.mCornerRadius;
   }

   public float getShadowSize() {
      return this.mShadowSize;
   }

   protected void onBoundsChange(Rect var1) {
      super.onBoundsChange(var1);
      this.mDirty = true;
   }

   public void setCardColor(int color) {
      this.mPaint.setColor(color);
      this.invalidateSelf();
   }

   public void setAlpha(int var1) {}

   public void setColorFilter(ColorFilter var1) {}
}
