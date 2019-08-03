package ru.asedias.vkbugtracker.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ProfileActivity;

/**
 * Created by rorom on 26.05.2018.
 */

public class ProfileActivityView extends View {

    private List<ProfileActivity> data = new ArrayList<>();
    private int top_padding = (int) BTApp.dp(48);
    private int text_offset = (int) BTApp.dp(28);
    private int cube_spacing = (int) BTApp.dp(3);
    private int cube_width = (int) BTApp.dp(13);
    private int offset_width = BTApp.dp(16);
    private TextPaint textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
    private Paint cubePaint = new Paint();
    private RectF cubeRect = new RectF();
    private List<Integer> months = new ArrayList<>();
    DateFormatSymbols dfs = new DateFormatSymbols();
    private int cubel;

    public ProfileActivityView(Context context) {
        super(context);
    }

    public ProfileActivityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileActivityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProfileActivityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setData(List<ProfileActivity> data) {
        this.data = data;
        this.textPaint.setTextAlign(Paint.Align.LEFT);
        this.textPaint.setTypeface(Fonts.Medium);
        this.textPaint.setColor(ThemeController.getTextSecondaryColor());
        //this.textPaint.setColor(BTApp.Color(R.color.colorPrimary));
        this.textPaint.setTextSize(BTApp.dp(14));
        this.cubePaint.setColor(BTApp.Color(R.color.colorPrimary));
        this.requestLayout();
        //invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        months.clear();
        this.offset_width = BTApp.dp(16);
        for(int i = 0; i < this.data.size(); i++) {
            ProfileActivity cube = this.data.get(i);
            int x = this.offset_width;
            int y = getYOffsetFor(cube.dayOfWeek);
            this.cubeRect.set(x, y, x + cube_width, y + cube_width);
            if(cube.alpha > 0) {
                this.cubePaint.setColor(BTApp.Color(R.color.colorPrimary));
            } else {
                this.cubePaint.setColor(BTApp.Color(R.color.blue_gray));
            }
            this.cubePaint.setAlpha((int)(40+(cube.alpha*215)));
            canvas.drawRect(this.cubeRect, this.cubePaint);
            if(cube.dayOfWeek == 7) updateOffsets();
            if(!months.contains(cube.month)) {
                months.add(cube.month);
                canvas.drawText(BTApp.StringArray(R.array.months)[cube.month], x, text_offset, this.textPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getViewWidth();
        int height = getViewHeight();
        super.onMeasure(width, height);
        this.setMeasuredDimension(width, height);
    }



    public int getViewHeight() {
        return this.top_padding + (cube_width + cube_spacing) * 7;
    }

    public int getViewWidth() {
        return BTApp.dp(40) + (cube_width + cube_spacing) * data.size() / 7;
    }

    private void updateOffsets() {
        this.offset_width += cube_width + cube_spacing;
    }

    private int getYOffsetFor(int day) {
        return this.top_padding + (cube_width + cube_spacing) * (day - 1);
    }
}
