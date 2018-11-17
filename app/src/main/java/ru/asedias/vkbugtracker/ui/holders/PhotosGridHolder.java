package ru.asedias.vkbugtracker.ui.holders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.ui.FlowLayout;
import ru.asedias.vkbugtracker.ui.ImageGridParser;

/**
 * Created by rorom on 11.11.2018.
 */

public class PhotosGridHolder extends BindableHolder<List<Report.Photo>> {
    private Activity activity;
    public PhotosGridHolder(Activity context) {
        super(new FrameLayout(context));
        this.activity = context;
    }

    @Override
    public void bind(List<Report.Photo> data) {
        if(data.size() > 0) new ImageGridParser(activity, data, (ViewGroup) itemView);
    }
}
