package ru.asedias.vkbugtracker;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ortiz.touch.TouchImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.ui.OnScrollPhotoListener;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;


/**
 * Created by rorom on 21.12.2018.
 */

public class PhotoViewerActivity extends AppCompatActivity {

    private FrameLayout rootView;
    private Toolbar toolbar;
    private ViewPager pager;
    private List<Report.Photo> photos;
    private int startPosition;

    private Drawable background;
    private OnScrollPhotoListener scrollPhotoListener = (photo, distanceY) -> {
        float multiplier = ((float)Math.abs(photo.getScrollY()) / BTApp.mMetrics.heightPixels);
        Log.d("APLHA", photo.getScrollY() + ": " + (255 - 255 * multiplier));
        this.background.setAlpha((int) (255 - 255 * multiplier));
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#20000000"));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        this.toolbar = new Toolbar(this);
        this.rootView = new FrameLayout(this);
        this.pager = new ViewPager(this);
        this.background = new ColorDrawable(Color.BLACK);
        if(getIntent() != null) {
            this.photos = getIntent().getParcelableArrayListExtra("photos");
            this.startPosition = getIntent().getIntExtra("pos", 0);
        }
        //getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //this.rootView.setBackgroundColor(Color.BLACK);

        this.pager.setAdapter(new PhotoAdapter());
        this.pager.setCurrentItem(startPosition);
        this.pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override public void onPageSelected(int position) {
                toolbar.setTitle(pager.getAdapter().getPageTitle(position));
            }
        });
        this.rootView.addView(this.pager);
        this.rootView.setBackgroundDrawable(this.background);

        this.toolbar.setBackgroundResource(R.drawable.scrim_top_photoviewer);
        this.toolbar.setNavigationIcon(R.drawable.ic_ab_back_arrow_white);
        this.toolbar.setNavigationOnClickListener(v -> finish());
        this.toolbar.setTitle(pager.getAdapter().getPageTitle(startPosition));
        this.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        int height = BTApp.dp(48);
        if(Build.VERSION.SDK_INT >= 19) {
            int status = BTApp.getStatusBarHeight();
            height += status;
            this.toolbar.setPadding(0, status, 0, 0);
        }

        this.rootView.addView(this.toolbar, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        setContentView(this.rootView);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        close(this.pager.findViewWithTag(this.pager.getAdapter().getPageTitle(this.pager.getCurrentItem())));
    }

    public void close(ImageView photo) {
        int y = photo.getScrollY() >= 0 ? BTApp.mMetrics.heightPixels : -BTApp.mMetrics.heightPixels;
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(photo, "scrollY", y);
        yTranslate.addUpdateListener(animation -> {
            scrollPhotoListener.onScroll(photo, (int) animation.getAnimatedValue());
        });
        yTranslate.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) { }
            @Override public void onAnimationEnd(Animator animation) { finish(); }
            @Override public void onAnimationCancel(Animator animation) { }
            @Override public void onAnimationRepeat(Animator animation) { }
        });
        ObjectAnimator fade = ObjectAnimator.ofFloat(this.toolbar, "alpha", 0);
        set.playTogether(yTranslate, fade);
        set.setDuration(300);
        set.start();
    }

    public class PhotoAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return photos.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return String.format(BTApp.String(R.string.picker_d_of_d), position + 1, photos.size());
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PhotoHolder holder = new PhotoHolder();
            holder.bind(photos.get(position));
            float height = BTApp.mMetrics.heightPixels - BTApp.getSystemBarsHeight();
            float width = photos.get(position).width*height/(photos.get(position).height);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)width, (int)height);
            pager.addView(holder.itemView, layoutParams);
            holder.photo.setTag(getPageTitle(position));
            return holder.itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            pager.removeView((View) object);
        }
    }

    private class PhotoHolder extends BindableHolder<Report.Photo> {

        TouchImageView photo;
        ProgressBar loading;

        public PhotoHolder() {
            super(new FrameLayout(PhotoViewerActivity.this));
            this.photo = new TouchImageView(PhotoViewerActivity.this);
            this.loading = new ProgressBar(PhotoViewerActivity.this);
        }

        @Override
        public void bind(Report.Photo data) {
            super.bind(data);
            FrameLayout photoRoot = (FrameLayout) this.itemView;
            setSystemBarsPadding();
            photoRoot.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    photoRoot.addView(photo);
                    photo.setOnClosePhotoListener(PhotoViewerActivity.this::close);
                    photo.setOnScrollPhotoListener(scrollPhotoListener);
                    photoRoot.addView(loading, new FrameLayout.LayoutParams(BTApp.dp(50), BTApp.dp(50), Gravity.CENTER));
                    Picasso.with(PhotoViewerActivity.this)
                            .load(data.getMax())
                            .error(BTApp.Drawable(R.drawable.ic_about))
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    photo.setImageDrawable(new BitmapDrawable(bitmap));
                                    photo.invalidate();
                                    loading.setVisibility(View.GONE);
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    photo.setImageDrawable(errorDrawable);
                                    photo.invalidate();
                                    loading.setVisibility(View.GONE);
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    loading.setVisibility(View.VISIBLE);
                                }
                            });
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    photoRoot.removeAllViews();
                }
            });
        }

        private void setSystemBarsPadding() {
            if(Build.VERSION.SDK_INT >= 19) {
                int status = BTApp.getStatusBarHeight();
                int nav = BTApp.getNavigationBarHeight();
                this.photo.setPadding(0, status, 0, nav);
            }
        }
    }
}
