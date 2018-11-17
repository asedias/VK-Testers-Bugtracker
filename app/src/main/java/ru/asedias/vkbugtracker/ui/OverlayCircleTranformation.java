package ru.asedias.vkbugtracker.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.squareup.picasso.Transformation;

import java.util.Locale;

import ru.asedias.vkbugtracker.BugTrackerApp;

/**
 * Created by rorom on 14.11.2018.
 */

public class OverlayCircleTranformation implements Transformation {

    private float overlay;

    public OverlayCircleTranformation(float overlay) { this.overlay = overlay; }

    @Override public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Path overlapShape = new Path();
        overlapShape.setFillType(Path.FillType.EVEN_ODD);
        float t = size/2;
        float tpad = t + BugTrackerApp.dp(1.5F);
        overlapShape.addCircle(t, t, t + 0.5F, Path.Direction.CW);
        overlapShape.addCircle(-tpad + 2.0F * tpad * (1.0F + overlay), t, tpad + 0.5F, Path.Direction.CW);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        canvas.drawPath(overlapShape, paint);

        source.recycle();

        return bitmap;
    }

    @Override
    public String key() {
        return String.format(Locale.getDefault(), "%s(%f)", StackBlurTransformation.class.getCanonicalName(), overlay);
    }
}
