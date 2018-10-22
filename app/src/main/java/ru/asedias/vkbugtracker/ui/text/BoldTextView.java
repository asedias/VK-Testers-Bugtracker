package ru.asedias.vkbugtracker.ui.text;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import ru.asedias.vkbugtracker.ui.Fonts;

/**
 * Created by rorom on 13.05.2018.
 */

public class BoldTextView extends android.support.v7.widget.AppCompatTextView {
    public BoldTextView(Context context) {
        super(context);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isInEditMode()) this.setTypeface(Fonts.Bold);
        super.onDraw(canvas);
    }

}
