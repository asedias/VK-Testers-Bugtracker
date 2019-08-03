package ru.asedias.vkbugtracker.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import ru.asedias.vkbugtracker.R;

/**
 * Created by Рома on 02.08.2019.
 */

public class KeyColorTextView extends AppCompatTextView {

    private String KEY = "";

    public KeyColorTextView(Context context) {
        super(context);
    }

    public KeyColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public KeyColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet var1) {
        TypedArray var2 = this.getContext().obtainStyledAttributes(var1, new int[]{R.attr.themeKey});
        this.KEY = var2.getString(0);
        var2.recycle();
    }

    public String getThemeKey() {
        return KEY;
    }

    public void setThemeKey(String KEY) {
        this.KEY = KEY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isInEditMode()) {
            this.setTextColor(ThemeController.getValue(this.getThemeKey()));
        } else {
            this.setTextColor(Color.BLACK);
        }
        super.onDraw(canvas);
    }
}
