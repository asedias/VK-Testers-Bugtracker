package ru.asedias.vkbugtracker.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.UserData;

/**
 * Created by rorom on 20.10.2018.
 */

public class UIHelper {

    public static void Setup(MainActivity Main) {
        if (Build.VERSION.SDK_INT >= 21) {
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN + View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if(Build.VERSION.SDK_INT >= 26) {
                visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                Main.getWindow().setNavigationBarColor(-1);
            }
            Main.getWindow().getDecorView().setSystemUiVisibility(visibility);
            Toolbar toolbar = Main.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.main);
            BottomNavigationViewEx bottom = Main.findViewById(R.id.navigation);
            bottom.setTextSize(10);
            bottom.setTextTypeface(Fonts.Medium);
            bottom.enableAnimation(false);
            bottom.enableShiftingMode(false);
            bottom.enableItemShiftingMode(false);
            LoadUserPhoto(toolbar);
        }
    }

    public static void LoadUserPhoto(Toolbar toolbar) {
        Picasso.with(BugTrackerApp.context)
                .load(UserData.getPhoto())
                .transform(new CropCircleTransformation())
                .placeholder(BugTrackerApp.Drawable(R.drawable.placeholder_user))
                .error(BugTrackerApp.Drawable(R.drawable.ic_settings))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        int p = BugTrackerApp.dp(15);
                        Bitmap bmOffsets = Bitmap.createBitmap(bitmap.getWidth() + p, bitmap.getHeight() + p, bitmap.getConfig());
                        Canvas canvas = new Canvas(bmOffsets);
                        canvas.drawBitmap(bitmap, p/2, p/2, null);
                        toolbar.getMenu().getItem(0).setIcon(new BitmapDrawable(BugTrackerApp.context.getResources(), bmOffsets));
                        toolbar.invalidate();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        toolbar.getMenu().getItem(0).setIcon(errorDrawable);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        toolbar.getMenu().getItem(0).setIcon(placeHolderDrawable);
                    }
                });
    }
}
