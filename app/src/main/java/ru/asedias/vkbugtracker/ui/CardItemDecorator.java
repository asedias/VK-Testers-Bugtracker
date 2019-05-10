package ru.asedias.vkbugtracker.ui;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ru.asedias.vkbugtracker.BTApp;

public class CardItemDecorator extends ItemDecoration {

   public static final int BACKGROUND_COLOR = 0;//-1315344;
   public static final int BOTTOM = 4;
   public static final int FIRST_ROW = 32;
   public static final int LAST_ROW = 64;
   public static final int LEFT = 8;
   public static final int MIDDLE = 1;
   public static final int NONE = 0;
   public static final int RIGHT = 16;
   public static final int TOP = 2;
   private Paint bgPaint;
   private int firstCardOffset;
   private boolean fullWidth;
   private final CardDrawable mDrawable;
   private final LayoutManager mManager;
   private final Provider mProvider;
   private int marginBottom;
   private int marginLeft;
   private int marginRight;
   private int marginTop;
   private int paddingAfter;
   private int paddingBefore;
   private int paddingFirst;
   private int paddingLast;
   private ArrayList sortedChildren;
   private Rect tmpRect;
   private Comparator viewComparator;


   public CardItemDecorator(Resources var1, Provider var2, LayoutManager var3, boolean var4) {
      this.tmpRect = new Rect();
      this.fullWidth = false;
      this.firstCardOffset = 0;
      this.sortedChildren = new ArrayList();
      this.viewComparator = (Comparator<View>) (var11, var21) -> var11.equals(var21)?0: var11.getTop() - var21.getTop();
      if(var2 != null) {
         this.mProvider = var2;
         this.mManager = var3;
         float var5 = BTApp.dp(2.0F);
         this.fullWidth = var4;
         this.mDrawable = new CardDrawable(var1, ThemeController.getValue(ThemeController.KEY_CARD_COLOR), var5, var4);
         this.bgPaint = new Paint();
         this.bgPaint.setColor(BACKGROUND_COLOR);
      } else {
         throw new NullPointerException("provider must be not null");
      }
   }

   public CardItemDecorator(RecyclerView var1, Provider var2, boolean var3) {
      this(var1.getResources(), var2, var1.getLayoutManager(), var3);
   }

   public CardItemDecorator(RecyclerView var1, boolean var2) {
      this(var1, (Provider)var1.getAdapter(), var2);
   }

   private void drawBackground(Canvas var1, Rect var2, int var3, int var4) {
      if(var2.bottom < var2.top) {
         try {
            throw new Exception("bad bounds " + this.tmpRect);
         } catch (Exception var6) {
            Log.w("vk", var6);
         }
      }

      this.mDrawable.getPadding(this.tmpRect);
      Rect var5 = this.tmpRect;
      var5.top += var3;
      var5 = this.tmpRect;
      var5.bottom += var4;
      if(this.tmpRect.left > 0) {
         var1.drawRect(0.0F, (float)(var2.top + this.tmpRect.top), (float)(var2.left + this.tmpRect.left), (float)(var2.bottom - this.tmpRect.bottom), this.bgPaint);
         var1.drawRect((float)(var2.left + this.tmpRect.left), (float)(var2.top + this.tmpRect.top - Math.min(0, var3)), (float)(var2.left + this.tmpRect.left + BTApp.dp(2.0F)), (float)(var2.top + this.tmpRect.top - Math.min(0, var3) + BTApp.dp(2.0F)), this.bgPaint);
         var1.drawRect((float)(var2.left + this.tmpRect.left), (float)(var2.bottom - this.tmpRect.bottom - BTApp.dp(2.0F)), (float)(var2.left + this.tmpRect.left + BTApp.dp(2.0F)), (float)(var2.bottom - this.tmpRect.bottom), this.bgPaint);
      }

      if(this.tmpRect.right > 0) {
         var1.drawRect((float)(var2.right - this.tmpRect.left), (float)(var2.top + this.tmpRect.top), (float)var1.getWidth(), (float)(var2.bottom - this.tmpRect.bottom), this.bgPaint);
         var1.drawRect((float)(var2.right - this.tmpRect.right - BTApp.dp(2.0F)), (float)(var2.top + this.tmpRect.top - Math.min(0, var3)), (float)(var2.right - this.tmpRect.right), (float)(var2.top + this.tmpRect.top - Math.min(0, var3) + BTApp.dp(2.0F)), this.bgPaint);
         var1.drawRect((float)(var2.right - this.tmpRect.right - BTApp.dp(2.0F)), (float)(var2.bottom - this.tmpRect.bottom - BTApp.dp(2.0F)), (float)(var2.right - this.tmpRect.right), (float)(var2.bottom - this.tmpRect.bottom), this.bgPaint);
      }

      if(this.tmpRect.top > 0 && var2.top > -this.tmpRect.top) {
         var1.drawRect(0.0F, (float)(var2.top - var3), (float)var1.getWidth(), (float)(var2.top + this.tmpRect.top - Math.min(0, var3)), this.bgPaint);
      }

      if(this.tmpRect.bottom > 0 && var2.bottom < var1.getHeight()) {
         var1.drawRect(0.0F, (float)(var2.bottom - this.tmpRect.bottom), (float)var1.getWidth(), (float)(var2.bottom + var4), this.bgPaint);
      }

   }

   private int getViewBottom(View var1) {
      return this.mManager.getDecoratedBottom(var1) + Math.round(var1.getTranslationY());
   }

   private int getViewTop(View var1) {
      return this.mManager.getDecoratedTop(var1) + Math.round(var1.getTranslationY());
   }

   public void getItemOffsets(Rect var1, View var2, RecyclerView var3, State var4) {
      int var8 = var3.getChildAdapterPosition(var2);
      if(var8 >= var3.getAdapter().getItemCount()) {
         var1.set(0, 0, 0, 0);
      } else {
         int var5;
         int var6;
         int block;
         label71: {
            this.mDrawable.getPadding(var1);
            block = this.mProvider.getBlockType(var8);
            LayoutManager var9 = var3.getLayoutManager();
            if(!(var9 instanceof GridLayoutManager) || ((GridLayoutManager)var9).getSpanCount() != 1) {
               var5 = block;
               if(!(var9 instanceof LinearLayoutManager)) {
                  break label71;
               }

               var5 = block;
               if(var9 instanceof GridLayoutManager) {
                  break label71;
               }
            }

            var6 = block;
            if(var8 == 0) {
               var6 = block | 32;
            }

            var5 = var6;
            if(var8 == var3.getAdapter().getItemCount() - 1) {
               var5 = var6 | 64;
            }
         }

         block = var1.top;
         if((var5 & 32) == 32) {
            var6 = this.paddingFirst;
         } else {
            var6 = this.paddingBefore;
         }

         var1.top = var6 + block;
         block = var1.bottom;
         if((var5 & 64) == 64) {
            var6 = this.paddingLast;
         } else {
            var6 = this.paddingAfter;
         }

         var1.bottom = var6 + block;
         if((var5 & 6) != 6) {
            if((var5 & 2) == 2) {
               var1.bottom = 0;
            } else if((var5 & 4) == 4) {
               var1.top = 0;
            } else if((var5 & 1) == 1) {
               var1.bottom = 0;
               var1.top = 0;
            }
         }

         if((var5 & 2) == 2) {
            var1.top += this.marginTop;
         }

         if((var5 & 4) == 4) {
            var1.bottom += this.marginBottom;
         }

         if((var5 & 8) == 8) {
            var1.right = 0;
            var1.left += this.marginLeft;
         }

         if((var5 & 16) == 16) {
            var1.left = 0;
            var1.right += this.marginRight;
         }
      }

   }

   public void onDraw(Canvas var1, RecyclerView var2, State var3) {
      super.onDraw(var1, var2, var3);
      int var7 = Integer.MIN_VALUE;
      int var14 = var2.getLeft() + var2.getPaddingLeft();
      int var15 = var2.getRight() - var2.getPaddingRight();
      int var11 = 0;
      int var12 = 0;

      int var4;
      for(var4 = 0; var4 < var2.getChildCount(); ++var4) {
         this.sortedChildren.add(var2.getChildAt(var4));
      }

      Collections.sort(this.sortedChildren, this.viewComparator);

      int var8;
      for(int var10 = 0; var10 < this.sortedChildren.size(); var11 = var8) {
         View var16 = (View)this.sortedChildren.get(var10);
         var8 = var2.getChildAdapterPosition(var16);
         int var5;
         if(var8 < 0) {
            var8 = var11;
            var4 = Integer.MIN_VALUE;
            var5 = var12;
         } else {
            boolean var13;
            if(var8 == var2.getAdapter().getItemCount() - 1) {
               var13 = true;
            } else {
               var13 = false;
            }

            if(var8 >= var2.getAdapter().getItemCount()) {
               var1.drawRect(0.0F, (float)this.getViewTop(var16), (float)var1.getWidth(), (float)this.getViewBottom(var16), this.bgPaint);
               break;
            }

            int var6;
            label207: {
               var5 = this.mProvider.getBlockType(var8);
               if(!(this.mManager instanceof GridLayoutManager) || ((GridLayoutManager)this.mManager).getSpanCount() != 1) {
                  var6 = var5;
                  if(!(this.mManager instanceof LinearLayoutManager)) {
                     break label207;
                  }

                  var6 = var5;
                  if(this.mManager instanceof GridLayoutManager) {
                     break label207;
                  }
               }

               var4 = var5;
               if(var8 == 0) {
                  var4 = var5 | CardItemDecorator.FIRST_ROW;
               }

               var6 = var4;
               if(var8 == var2.getAdapter().getItemCount() - 1) {
                  var6 = var4 | CardItemDecorator.LAST_ROW;
               }
            }

            int var9;
            CardDrawable var17;
            Rect var18;
            if((var6 & 6) == 6) {
               var5 = this.getViewTop(var16);
               var4 = var5;
               if((var6 & CardItemDecorator.FIRST_ROW) == CardItemDecorator.FIRST_ROW) {
                  var4 = var5 + this.firstCardOffset;
               }

               var9 = this.getViewBottom(var16);
               var5 = var9;
               var17 = this.mDrawable;
               if((var6 & CardItemDecorator.FIRST_ROW) == CardItemDecorator.FIRST_ROW) {
                  var7 = this.paddingFirst;
               } else {
                  var7 = this.paddingBefore;
               }

               if((var6 & CardItemDecorator.LAST_ROW) == CardItemDecorator.LAST_ROW) {
                  var8 = this.paddingLast;
               } else {
                  var8 = this.paddingAfter;
               }

               var17.setBounds(var14, var4 + var7, var15, var9 - var8);
               var18 = this.mDrawable.getBounds();
               if((var6 & CardItemDecorator.FIRST_ROW) == CardItemDecorator.FIRST_ROW) {
                  var4 = this.paddingFirst;
               } else {
                  var4 = this.paddingBefore;
               }

               if((var6 & CardItemDecorator.LAST_ROW) == CardItemDecorator.LAST_ROW) {
                  var6 = this.paddingLast;
               } else {
                  var6 = this.paddingAfter;
               }

               this.drawBackground(var1, var18, var4, var6);
               this.mDrawable.draw(var1);
               var4 = Integer.MIN_VALUE;
               var8 = 0;
            } else if((var6 & CardItemDecorator.TOP) == CardItemDecorator.TOP) {
               label200: {
                  var4 = this.getViewTop(var16);
                  var9 = var6;
                  var7 = var4;
                  if((var6 & CardItemDecorator.FIRST_ROW) == CardItemDecorator.FIRST_ROW) {
                     var7 = var4 + this.firstCardOffset;
                  }

                  if(var10 != var2.getChildCount() - 1) {
                     var5 = var12;
                     var4 = var7;
                     var8 = var6;
                     if(!var13) {
                        break label200;
                     }
                  }

                  var11 = (int) (this.getViewBottom(var16) + BTApp.dp(2.0F));
                  var5 = var12;
                  var4 = var7;
                  var8 = var6;
                  if(var11 >= var12) {
                     var5 = var11;
                     var17 = this.mDrawable;
                     if((var6 & CardItemDecorator.FIRST_ROW) == CardItemDecorator.FIRST_ROW) {
                        var4 = this.paddingFirst;
                     } else {
                        var4 = this.paddingBefore;
                     }

                     if((var6 & CardItemDecorator.LAST_ROW) == CardItemDecorator.LAST_ROW) {
                        var8 = this.paddingLast;
                     } else {
                        var8 = this.paddingAfter;
                     }

                     var17.setBounds(var14, var7 + var4, var15, var11 - var8);
                     var18 = this.mDrawable.getBounds();
                     if((var6 & CardItemDecorator.FIRST_ROW) == CardItemDecorator.FIRST_ROW) {
                        var4 = this.paddingFirst;
                     } else {
                        var4 = this.paddingBefore;
                     }

                     if((var6 & CardItemDecorator.LAST_ROW) == CardItemDecorator.LAST_ROW) {
                        var6 = this.paddingLast;
                     } else {
                        var6 = this.paddingAfter;
                     }

                     this.drawBackground(var1, var18, var4, var6);
                     this.mDrawable.draw(var1);
                     var4 = var7;
                     var8 = var9;
                  }
               }
            } else {
               int var19;
               if((var6 & 1) == 1) {
                  label202: {
                     var9 = var7;
                     if(var7 == Integer.MIN_VALUE) {
                        var9 = (int) (this.getViewTop(var16) - BTApp.dp(5.0F));
                     }

                     if(var10 != var2.getChildCount() - 1) {
                        var5 = var12;
                        var4 = var9;
                        var8 = var11;
                        if(!var13) {
                           break label202;
                        }
                     }

                     var19 = (int) (this.getViewBottom(var16) + BTApp.dp(2.0F));
                     var5 = var12;
                     var4 = var9;
                     var8 = var11;
                     if(var19 >= var12) {
                        var5 = var19;
                        var17 = this.mDrawable;
                        if((var11 & CardItemDecorator.FIRST_ROW) == CardItemDecorator.FIRST_ROW) {
                           var4 = this.paddingFirst;
                        } else {
                           var4 = this.paddingBefore;
                        }

                        if((var6 & CardItemDecorator.LAST_ROW) == CardItemDecorator.LAST_ROW) {
                           var7 = this.paddingLast;
                        } else {
                           var7 = this.paddingAfter;
                        }

                        var17.setBounds(var14, var9 + var4, var15, var19 - var7);
                        var18 = this.mDrawable.getBounds();
                        if((var11 & CardItemDecorator.FIRST_ROW) == CardItemDecorator.FIRST_ROW) {
                           var4 = this.paddingFirst;
                        } else {
                           var4 = this.paddingBefore;
                        }

                        if((var6 & CardItemDecorator.LAST_ROW) == CardItemDecorator.LAST_ROW) {
                           var6 = this.paddingLast;
                        } else {
                           var6 = this.paddingAfter;
                        }

                        this.drawBackground(var1, var18, var4, var6);
                        this.mDrawable.draw(var1);
                        var4 = var9;
                        var8 = var11;
                     }
                  }
               } else if((var6 & CardItemDecorator.BOTTOM) == CardItemDecorator.BOTTOM) {
                  var9 = var7;
                  if(var7 == Integer.MIN_VALUE) {
                     var9 = this.getViewTop(var16);
                  }

                  var19 = this.getViewBottom(var16);
                  var5 = var12;
                  var4 = var9;
                  var8 = var11;
                  if(var19 >= var12) {
                     var5 = var19;
                     var17 = this.mDrawable;
                     if((var11 & CardItemDecorator.FIRST_ROW) == CardItemDecorator.FIRST_ROW) {
                        var4 = this.paddingFirst;
                     } else {
                        var4 = this.paddingBefore;
                     }

                     if((var6 & CardItemDecorator.LAST_ROW) == CardItemDecorator.LAST_ROW) {
                        var7 = this.paddingLast;
                     } else {
                        var7 = this.paddingAfter;
                     }

                     var17.setBounds(var14, var9 + var4, var15, var19 - var7);
                     if(this.mDrawable.getBounds().bottom > this.mDrawable.getBounds().top) {
                        var18 = this.mDrawable.getBounds();
                        if((var11 & CardItemDecorator.FIRST_ROW) == CardItemDecorator.FIRST_ROW) {
                           var4 = this.paddingFirst;
                        } else {
                           var4 = this.paddingBefore;
                        }

                        if((var6 & CardItemDecorator.LAST_ROW) == CardItemDecorator.LAST_ROW) {
                           var6 = this.paddingLast;
                        } else {
                           var6 = this.paddingAfter;
                        }

                        this.drawBackground(var1, var18, var4, var6);
                        this.mDrawable.draw(var1);
                     }

                     var4 = Integer.MIN_VALUE;
                     var8 = 0;
                  }
               } else {
                  var5 = var12;
                  var4 = var7;
                  var8 = var11;
                  if(var6 == 0) {
                     var1.drawRect(0.0F, (float)this.getViewTop(var16), (float)var1.getWidth(), (float)this.getViewBottom(var16), this.bgPaint);
                     var5 = var12;
                     var4 = var7;
                     var8 = var11;
                  }
               }
            }
         }

         ++var10;
         var12 = var5;
         var7 = var4;
      }

      if(var12 < var2.getHeight()) {
         var1.drawRect(0.0F, (float)var12, (float)var1.getWidth(), (float)var2.getHeight(), this.bgPaint);
      }

      this.sortedChildren.clear();
   }

   public void setBackgroundColor(int var1) {
      this.bgPaint.setColor(var1);
   }

   public void setFirstCardOffset(int var1) {
      this.firstCardOffset = var1;
   }

   public void setInnerMargins(int left, int top, int right, int bottom) {
      this.marginLeft = left;
      this.marginRight = right;
      this.marginTop = top;
      this.marginBottom = bottom;
   }

   public void setPadding(int left, int right, int top, int bottom) {
      this.paddingBefore = left;
      this.paddingAfter = right;
      this.paddingFirst = top;
      this.paddingLast = bottom;
   }

   public CardDrawable getCardDrawable() {
      return this.mDrawable;
   }

   public interface Provider {

      int getBlockType(int var1);
   }
}
