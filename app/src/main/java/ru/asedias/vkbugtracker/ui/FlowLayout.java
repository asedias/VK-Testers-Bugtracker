package ru.asedias.vkbugtracker.ui;

/**
 * Created by Рома on 09.07.2017.
 */


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.BTApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class FlowLayout extends ViewGroup {

    // $FF: synthetic field
    static final boolean $assertionsDisabled;
    private Vector lineHeights = new Vector();
    private List lparams;
    private int measuredHeight = 0;
    public int pwidth = (int) BTApp.dp(5);


    static {
        boolean var0;
        if(!FlowLayout.class.desiredAssertionStatus()) {
            var0 = true;
        } else {
            var0 = false;
        }

        $assertionsDisabled = var0;
    }

    public FlowLayout(Context var1) {
        super(var1);
    }

    public FlowLayout(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams var1) {
        return var1 instanceof LayoutParams;
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams((int) BTApp.dp(2), (int) BTApp.dp(2));
    }

    public int getFullHeight() {
        int var1 = 0;

        for(Iterator var2 = this.lineHeights.iterator(); var2.hasNext(); var1 += ((Integer)var2.next()).intValue()) {
            ;
        }

        return var1;
    }

    public List layoutWithParams(List var1, int var2, int var3) {
        this.lparams = var1;
        ArrayList var17 = new ArrayList();
        int var14 = var1.size();
        int var9 = this.getPaddingLeft();
        int var7 = this.getPaddingTop();
        boolean var16 = false;
        this.lineHeights.clear();
        int var4 = 0;
        int var5 = 0;
        var3 = 0;

        int var6;
        int var8;
        int var10;
        int var11;
        int var12;
        int var13;
        LayoutParams var18;
        for(var10 = 0; var10 < var14; var7 = var6) {
            var18 = (LayoutParams)var1.get(var10);
            if(var18.width <= 0) {
                var11 = var2;
            } else {
                var11 = var18.width;
            }

            int var15 = var18.height;
            if(var15 < 0) {
                throw new IllegalArgumentException("Height should be constant");
            }

            label78: {
                if(!var16) {
                    var12 = var4;
                    var13 = var5;
                    var8 = var9;
                    var6 = var7;
                    if(var9 + var11 <= this.pwidth + var2) {
                        break label78;
                    }
                }

                var8 = this.getPaddingLeft();
                var6 = var7 + Math.max(var5, var4);
                this.lineHeights.add(Integer.valueOf(Math.max(var5, var4)));
                var13 = 0;
                var12 = 0;
            }

            var5 = Math.max(var13, var18.vertical_spacing + var15);
            if(var18.floating) {
                var6 += var18.vertical_spacing + var15;
                var4 = var12 + var18.vertical_spacing + var15;
                var3 = Math.max(var3, var8 + var11);
            } else {
                var4 = 0;
                var8 += var18.horizontal_spacing + var11;
            }

            var16 = var18.breakAfter;
            var3 = Math.max(var3, var8 - var18.horizontal_spacing);
            ++var10;
            var9 = var8;
        }

        if(var5 > 0) {
            this.lineHeights.add(Integer.valueOf(var5));
        }

        var6 = this.getPaddingLeft();
        var4 = this.getPaddingTop();
        var3 = 0;
        boolean var19 = false;
        var16 = false;
        var11 = 0;

        for(var9 = 0; var9 < var14; var11 = var10) {
            var18 = (LayoutParams)var1.get(var9);
            if(var18.width <= 0) {
                var12 = var2;
            } else {
                var12 = var18.width;
            }

            var13 = var18.height;
            if(var13 < 0) {
                throw new IllegalArgumentException("Height should be constant");
            }

            var7 = var4;
            if(!var18.floating) {
                var7 = var4;
                if(var19) {
                    var7 = var3;
                }
            }

            label67: {
                if(!var16) {
                    var10 = var11;
                    var8 = var6;
                    var4 = var7;
                    if(var6 + var12 <= this.pwidth + var2) {
                        break label67;
                    }
                }

                var8 = this.getPaddingLeft();
                var4 = var7 + ((Integer)this.lineHeights.elementAt(var11)).intValue();
                var10 = var11 + 1;
            }

            var6 = var8;
            if(var18.center) {
                var6 = this.getWidth() / 2 - var12 / 2;
            }

            //Log.v("vk", var6 + ";" + var4 + ";" + var12 + ";" + var13);
            var17.add(new Rect(var6, var4, var6 + var12, var4 + var13));
            if(var18.floating) {
                boolean var20 = var19;
                if(!var19) {
                    var3 = var4;
                    var20 = true;
                }

                var4 += var18.vertical_spacing + var13;
                var19 = var20;
            } else {
                var19 = false;
                var6 += var18.horizontal_spacing + var12;
            }

            var16 = var18.breakAfter;
            ++var9;
        }

        this.measuredHeight = this.getFullHeight();
        return var17;
    }

    protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
        int var15 = this.getChildCount();
        int var9 = this.getPaddingLeft();
        var5 = this.getPaddingTop();
        int var8 = 0;
        var1 = false;
        boolean var6 = false;
        var3 = 0;

        int var10;
        for(int var12 = 0; var12 < var15; var5 = var10) {
            View var17 = this.getChildAt(var12);
            boolean var16 = var1;
            int var14 = var3;
            int var7 = var8;
            boolean var13 = var6;
            int var11 = var9;
            var10 = var5;
            if(var17.getVisibility() != View.GONE) {
                LayoutParams var18 = (LayoutParams)var17.getLayoutParams();
                if(var18.width <= 0) {
                    var14 = var17.getMeasuredWidth();
                } else {
                    var14 = var18.width;
                }

                int var20;
                if(var18.height <= 0) {
                    var20 = var17.getMeasuredHeight();
                } else {
                    var20 = var18.height;
                }

                var10 = var5;
                if(!var18.floating) {
                    var10 = var5;
                    if(var6) {
                        var10 = var3;
                    }
                }

                label45: {
                    if(!var1) {
                        var7 = var8;
                        var11 = var9;
                        var5 = var10;
                        if(var9 + var14 <= this.pwidth + (var4 - var2)) {
                            break label45;
                        }
                    }

                    var11 = this.getPaddingLeft();
                    var5 = var10 + ((Integer)this.lineHeights.elementAt(var8)).intValue();
                    var7 = var8 + 1;
                }

                var8 = var11;
                if(var18.center) {
                    var8 = this.getWidth() / 2 - var14 / 2;
                }

                var17.layout(var8, var5, var8 + var14, var5 + var20);
                boolean var19;
                if(var18.floating) {
                    var19 = var6;
                    if(!var6) {
                        var3 = var5;
                        var19 = true;
                    }

                    var5 += var18.vertical_spacing + var20;
                } else {
                    var19 = false;
                    var8 += var18.horizontal_spacing + var14;
                }

                var16 = var18.breakAfter;
                var10 = var5;
                var11 = var8;
                var13 = var19;
                var14 = var3;
            }

            ++var12;
            var1 = var16;
            var3 = var14;
            var8 = var7;
            var6 = var13;
            var9 = var11;
        }

    }

    protected void onMeasure(int var1, int var2) {
        if(!$assertionsDisabled && MeasureSpec.getMode(var1) == MeasureSpec.UNSPECIFIED) {
            throw new AssertionError();
        } else {
            int var16 = MeasureSpec.getSize(var1) - this.getPaddingLeft() - this.getPaddingRight();
            int var17 = MeasureSpec.getSize(var2) - this.getPaddingTop() - this.getPaddingBottom();
            int var18 = this.getChildCount();
            int var3 = 0;
            int var6 = this.getPaddingLeft();
            int var5 = this.getPaddingTop();
            int var4 = 0;
            int var12;
            if(MeasureSpec.getMode(var2) == Integer.MIN_VALUE) {
                var12 = MeasureSpec.makeMeasureSpec(var17, Integer.MIN_VALUE);
            } else {
                var12 = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            }

            this.lineHeights.clear();
            boolean var19 = false;
            int var7 = 0;
            int var13 = 0;

            while(true) {
                int var8;
                if(this.lparams != null) {
                    var8 = this.lparams.size();
                } else {
                    var8 = 0;
                }

                if(var13 >= Math.max(var18, var8)) {
                    Iterator var23;
                    if(MeasureSpec.getMode(var2) == MeasureSpec.UNSPECIFIED) {
                        var2 = Math.max(var3, var7);
                        var23 = this.lineHeights.iterator();

                        while(true) {
                            var6 = var2;
                            if(!var23.hasNext()) {
                                break;
                            }

                            var2 += ((Integer)var23.next()).intValue();
                        }
                    } else {
                        var6 = var17;
                        if(MeasureSpec.getMode(var2) == Integer.MIN_VALUE) {
                            var6 = var17;
                            if(var5 + var3 < var17) {
                                var2 = var3;
                                var23 = this.lineHeights.iterator();

                                while(true) {
                                    var6 = var2;
                                    if(!var23.hasNext()) {
                                        break;
                                    }

                                    var2 += ((Integer)var23.next()).intValue();
                                }
                            }
                        }
                    }

                    if(MeasureSpec.getMode(var1) == MeasureSpec.EXACTLY) {
                        this.setMeasuredDimension(var16, var6);
                        return;
                    }

                    this.setMeasuredDimension(var4, var6);
                    return;
                }

                int var9;
                int var10;
                int var11;
                int var14;
                boolean var20;
                label124: {
                    View var22 = this.getChildAt(var13);
                    if(var22 == null || var22.getVisibility() == View.GONE) {
                        var20 = var19;
                        var14 = var7;
                        var11 = var3;
                        var8 = var4;
                        var10 = var6;
                        var9 = var5;
                        if(var22 != null) {
                            break label124;
                        }
                    }

                    LayoutParams var21;
                    if(var22 != null) {
                        var21 = (LayoutParams)var22.getLayoutParams();
                    } else {
                        var21 = (LayoutParams)this.lparams.get(var13);
                    }

                    if(var22 != null && var21.width == -1) {
                        var22.measure(MeasureSpec.makeMeasureSpec(var16, MeasureSpec.EXACTLY), var12);
                    } else if(var22 != null) {
                        if(var21.width <= 0) {
                            var8 = MeasureSpec.makeMeasureSpec(var16, Integer.MIN_VALUE);
                        } else {
                            var8 = MeasureSpec.makeMeasureSpec(var21.width, MeasureSpec.EXACTLY);
                        }

                        var22.measure(var8, var12);
                    }

                    if(var21.width <= 0) {
                        if(var22 != null) {
                            var8 = var22.getMeasuredWidth();
                        } else {
                            var8 = var16;
                        }
                    } else {
                        var8 = var21.width;
                    }

                    if(var21.height <= 0) {
                        if(var22 != null) {
                            var11 = var22.getMeasuredHeight();
                        } else {
                            var11 = 0;
                        }
                    } else {
                        var11 = var21.height;
                    }

                    int var15;
                    label98: {
                        if(!var19) {
                            var14 = var7;
                            var15 = var3;
                            var10 = var6;
                            var9 = var5;
                            if(var6 + var8 <= this.pwidth + var16) {
                                break label98;
                            }
                        }

                        var10 = this.getPaddingLeft();
                        var9 = var5 + Math.max(var3, var7);
                        this.lineHeights.add(Integer.valueOf(Math.max(var3, var7)));
                        var15 = 0;
                        var14 = 0;
                    }

                    var5 = Math.max(var15, var21.vertical_spacing + var11);
                    if(var21.floating) {
                        var9 += var21.vertical_spacing + var11;
                        var3 = var14 + var21.vertical_spacing + var11;
                        var4 = Math.max(var4, var10 + var8);
                    } else {
                        var3 = 0;
                        var10 += var21.horizontal_spacing + var8;
                    }

                    var20 = var21.breakAfter;
                    var8 = Math.max(var4, var10 - var21.horizontal_spacing);
                    var11 = var5;
                    var14 = var3;
                }

                ++var13;
                var19 = var20;
                var7 = var14;
                var3 = var11;
                var4 = var8;
                var6 = var10;
                var5 = var9;
            }
        }
    }

    public void resetParams() {
        this.lparams = null;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public boolean breakAfter;
        public boolean center;
        public boolean floating;
        public int height;
        public int horizontal_spacing;
        public int vertical_spacing;
        public int width;


        public LayoutParams() {
            super(0, 0);
        }

        public LayoutParams(int var1, int var2) {
            super(0, 0);
            this.horizontal_spacing = var1;
            this.vertical_spacing = var2;
        }

        public static LayoutParams CreateLayoutParamsForZhukovsLayout() {
            return new LayoutParams((int) BTApp.dp(2), (int) BTApp.dp(2));
        }
    }
}
