package ru.asedias.vkbugtracker.ui;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import  ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.BuildConfig;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Рома on 09.07.2017.
 */

public class ImageGridParser {
    private List<Report.Photo> photos;
    private ViewGroup root;
    private FlowLayout layout;
    private int rootLayoutWidth = 0;

    public ImageGridParser(Activity activity, List<Report.Photo> photos, ViewGroup root) {
        this.photos = photos;
        this.root = root;
        this.layout = new FlowLayout(BugTrackerApp.context);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        rootLayoutWidth = metrics.widthPixels;
        if(this.photos != null && this.photos.size() > 0) parse();
    }

    public ImageGridParser(List<Report.Photo> photos, ViewGroup root, int rootLayoutWidth) {
        this.photos = photos;
        this.root = root;
        this.layout = new FlowLayout(BugTrackerApp.context);
        this.rootLayoutWidth = rootLayoutWidth;
        if(this.photos != null && this.photos.size() > 0) parse();
    }

    private void parse() {
        List<PhotoSize> sizes;
        switch(photos.size()) {
            default:
            case 1:
                sizes = getLayoutParams(1, photos.subList(0, 1));
                break;
            case 2:
                sizes = getLayoutParams(2, photos.subList(0, 2));
                break;
            case 3:
                sizes = getLayoutParams(3, photos.subList(0, 3));
                break;
            case 4:
                //sizes = getLayoutParams(4, photos.subList(0, 4));
                sizes = getLayoutParams(2, photos.subList(0, 2));
                sizes.get(sizes.size()-1).breakAfter = true;
                sizes.addAll(getLayoutParams(2, photos.subList(2, 4)));
                break;
            case 5:
                //sizes = getLayoutParams(5, photos.subList(0, 5));
                sizes = getLayoutParams(2, photos.subList(0, 2));
                sizes.get(sizes.size()-1).breakAfter = true;
                sizes.addAll(getLayoutParams(3, photos.subList(2, 5)));
                break;
            case 6:
                sizes = getLayoutParams(3, photos.subList(0, 3));
                sizes.get(sizes.size()-1).breakAfter = true;
                sizes.addAll(getLayoutParams(3, photos.subList(3, 6)));
                break;
            case 7:
                sizes = getLayoutParams(3, photos.subList(0, 3));
                sizes.get(sizes.size()-1).breakAfter = true;
                sizes.addAll(getLayoutParams(4, photos.subList(3, 7)));
                break;
            case 8:
                sizes = getLayoutParams(4, photos.subList(0, 4));
                sizes.get(sizes.size()-1).breakAfter = true;
                sizes.addAll(getLayoutParams(4, photos.subList(4, 8)));
                break;
            case 9:
                sizes = getLayoutParams(4, photos.subList(0, 4));
                sizes.get(sizes.size()-1).breakAfter = true;
                sizes.addAll(getLayoutParams(2, photos.subList(4, 6)));
                sizes.get(sizes.size()-1).breakAfter = true;
                sizes.addAll(getLayoutParams(3, photos.subList(6, 9)));
                break;
            case 10:
                sizes = getLayoutParams(2, photos.subList(0, 2));
                sizes.get(sizes.size()-1).breakAfter = true;
                sizes.addAll(getLayoutParams(3, photos.subList(2, 5)));
                sizes.get(sizes.size()-1).breakAfter = true;
                sizes.addAll(getLayoutParams(5, photos.subList(5, 10)));
                break;
        }

        for(int i = 0; i < sizes.size(); i++) {
            ImageView image = new ImageView(BugTrackerApp.context);
            PhotoSize size = sizes.get(i);
            Report.Photo photo = photos.get(i);
            FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams();
            lp.height = size.height;
            lp.width = size.width;
            lp.horizontal_spacing = (int) BugTrackerApp.dp(4);
            lp.breakAfter = size.breakAfter;
            if(size.breakAfter) {
                lp.vertical_spacing = (int) BugTrackerApp.dp(4);
            }
            Picasso pic = Picasso.with(BugTrackerApp.context);
            RequestCreator req = pic.load(photo.url_x);
            if (!TextUtils.isEmpty(photo.url_y)) {
                req = pic.load(photo.url_y);
            }
            req.placeholder(new ColorDrawable(BugTrackerApp.Color(R.color.thumb))).into(image);
            layout.addView(image, lp);
        }
        this.root.addView(layout, new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //if(sizes.size() > 0) this.root.addView(layout, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, sizes.get(0).height));
    }

    private ArrayList<PhotoSize> getLayoutParams(int count, List<Report.Photo> list) {
        ArrayList<PhotoSize> sizes = new ArrayList<>();
        float k = 0;
        float h;
        float width0;
        float width1;
        float width2;
        float width3;
        float width4;
        float width5;
        float width6;
        float width7;
        float width8;
        switch(count) {
            default:
            case 1:
                h = list.get(0).height*(rootLayoutWidth)/(list.get(0).width);
                sizes.add(new PhotoSize(Math.round(h), rootLayoutWidth));
                break;
            case 2:
                rootLayoutWidth = rootLayoutWidth - (int)BugTrackerApp.dp(4);
                h = (list.get(0).height + list.get(1).height)/2;
                width0 = (h/list.get(0).height)*list.get(0).width;
                width1 = (h/list.get(1).height)*list.get(1).width;
                k = (width0 + width1)/rootLayoutWidth;
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width0/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width1/k)));
                break;
            case 3:
                rootLayoutWidth = rootLayoutWidth - (int)BugTrackerApp.dp(8);
                h = (list.get(0).height + list.get(1).height + list.get(2).height)/3;
                width0 = (h/list.get(0).height)*list.get(0).width;
                width1 = (h/list.get(1).height)*list.get(1).width;
                width2 = (h/list.get(2).height)*list.get(2).width;
                k = (width0 + width1 + width2)/rootLayoutWidth;
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width0/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width1/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width2/k)));
                break;
            case 4:
                rootLayoutWidth = rootLayoutWidth - (int)BugTrackerApp.dp(12);
                h = (list.get(0).height + list.get(1).height + list.get(2).height + list.get(3).height)/4;
                width0 = (h/list.get(0).height)*list.get(0).width;
                width1 = (h/list.get(1).height)*list.get(1).width;
                width2 = (h/list.get(2).height)*list.get(2).width;
                width3 = (h/list.get(3).height)*list.get(3).width;
                k = (width0 + width1 + width2 + width3)/rootLayoutWidth;
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width0/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width1/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width2/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width3/k)));
                break;
            case 5:
                rootLayoutWidth = rootLayoutWidth - (int)BugTrackerApp.dp(16);
                h = (list.get(0).height + list.get(1).height + list.get(2).height + list.get(3).height + list.get(4).height)/5;
                width0 = (h/list.get(0).height)*list.get(0).width;
                width1 = (h/list.get(1).height)*list.get(1).width;
                width2 = (h/list.get(2).height)*list.get(2).width;
                width3 = (h/list.get(3).height)*list.get(3).width;
                width4 = (h/list.get(4).height)*list.get(4).width;
                k = (width0 + width1 + width2 + width3 + width4)/rootLayoutWidth;
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width0/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width1/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width2/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width3/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width4/k)));
                break;
            case 6:
                rootLayoutWidth = rootLayoutWidth - (int)BugTrackerApp.dp(20);
                h = (list.get(0).height + list.get(1).height + list.get(2).height + list.get(3).height + list.get(4).height + list.get(5).height)/6;
                width0 = (h/list.get(0).height)*list.get(0).width;
                width1 = (h/list.get(1).height)*list.get(1).width;
                width2 = (h/list.get(2).height)*list.get(2).width;
                width3 = (h/list.get(3).height)*list.get(3).width;
                width4 = (h/list.get(4).height)*list.get(4).width;
                width5 = (h/list.get(5).height)*list.get(5).width;
                k = (width0 + width1 + width2 + width3 + width4 + width5)/rootLayoutWidth;
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width0/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width1/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width2/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width3/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width4/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width5/k)));
                break;
            case 7:
                rootLayoutWidth = rootLayoutWidth - (int)BugTrackerApp.dp(24);
                h = (list.get(0).height + list.get(1).height + list.get(2).height + list.get(3).height + list.get(4).height + list.get(5).height + list.get(6).height)/7;
                width0 = (h/list.get(0).height)*list.get(0).width;
                width1 = (h/list.get(1).height)*list.get(1).width;
                width2 = (h/list.get(2).height)*list.get(2).width;
                width3 = (h/list.get(3).height)*list.get(3).width;
                width4 = (h/list.get(4).height)*list.get(4).width;
                width5 = (h/list.get(5).height)*list.get(5).width;
                width6 = (h/list.get(6).height)*list.get(6).width;
                k = (width0 + width1 + width2 + width3 + width4 + width5 + width6)/rootLayoutWidth;
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width0/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width1/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width2/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width3/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width4/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width5/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width6/k)));
                break;
            case 8:
                rootLayoutWidth = rootLayoutWidth - (int)BugTrackerApp.dp(24);
                h = (list.get(0).height + list.get(1).height + list.get(2).height + list.get(3).height + list.get(4).height + list.get(5).height + list.get(6).height + list.get(7).height)/8;
                width0 = (h/list.get(0).height)*list.get(0).width;
                width1 = (h/list.get(1).height)*list.get(1).width;
                width2 = (h/list.get(2).height)*list.get(2).width;
                width3 = (h/list.get(3).height)*list.get(3).width;
                width4 = (h/list.get(4).height)*list.get(4).width;
                width5 = (h/list.get(5).height)*list.get(5).width;
                width6 = (h/list.get(6).height)*list.get(6).width;
                width7 = (h/list.get(7).height)*list.get(7).width;
                k = (width0 + width1 + width2 + width3 + width4 + width5 + width6 + width7)/rootLayoutWidth;
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width0/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width1/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width2/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width3/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width4/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width5/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width6/k)));
                sizes.add(new PhotoSize(Math.round(h/k), Math.round(width7/k)));
                break;
        }
        return sizes;
    }

    private class PhotoSize {
        public int height;
        public int width;
        public boolean breakAfter = false;

        public PhotoSize(int height, int width) {
            this.height = height;
            this.width = width;
        }

        public PhotoSize(int height, int width, boolean breakAfter) {
            this.height = height;
            this.width = width;
            this.breakAfter = breakAfter;
        }
    }
}
