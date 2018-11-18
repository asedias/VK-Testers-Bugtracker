package ru.asedias.vkbugtracker.ui.holders;

import android.content.res.ColorStateList;
import android.support.v4.widget.ImageViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;

/**
 * Created by rorom on 11.11.2018.
 */

public class DetailItemHolder extends BindableHolder<Report.Detail> {

    private ImageView icon;
    private TextView title;
    private TextView description;
    private ImageView photo;
    private int type;

    public DetailItemHolder(LayoutInflater inflater, int type) {
        super(inflater.inflate(R.layout.report_detail_item, null, false));
        this.type = type;
        this.icon = itemView.findViewById(R.id.icon);
        this.photo = itemView.findViewById(R.id.photo);
        this.title = itemView.findViewById(R.id.text);
        this.description = itemView.findViewById(R.id.description);
    }

    @Override
    public void bind(Report.Detail data) {
        super.bind(data);
        if(type == 0) {
            ColorStateList color = this.title.getTextColors();
            TextViewCompat.setTextAppearance(this.title, R.style.TextAppearance_AppCompat_Body2);
            this.title.setTextColor(color);
            this.title.setText(data.description);
            this.icon.setImageDrawable(BugTrackerApp.Drawable(R.drawable.ic_product));
            this.photo.setVisibility(View.VISIBLE);
            this.description.setVisibility(View.GONE);
        } else if(type == 1){
            this.title.setText(data.title);
            this.icon.setImageDrawable(BugTrackerApp.Drawable(R.drawable.ic_detail));
            this.photo.setVisibility(View.GONE);
            ColorStateList color = this.description.getTextColors();
            TextViewCompat.setTextAppearance(this.description, R.style.TextAppearance_AppCompat_Body2);
            this.description.setTextColor(color);
            this.description.setVisibility(View.VISIBLE);
            this.description.setText(data.description);
        } else {
            this.icon.setImageDrawable(BugTrackerApp.Drawable(R.drawable.ic_more));
            this.icon.setColorFilter(BugTrackerApp.Color(R.color.colorAccent));
            this.photo.setVisibility(View.GONE);
            this.description.setVisibility(View.GONE);
            TextViewCompat.setTextAppearance(this.title, R.style.TextAppearance_AppCompat_Body2);
            this.title.setTextColor(BugTrackerApp.Color(R.color.colorAccent));
            this.title.setText(BugTrackerApp.String(R.string.show_more_info));
        }
    }
}
