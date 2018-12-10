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

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.fragments.ReportDetailsFragment;
import ru.asedias.vkbugtracker.fragments.ViewReportFragment;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.UIController;

/**
 * Created by rorom on 11.11.2018.
 */

public class DetailItemHolder extends BindableHolder<Report.Detail> {

    private ImageView icon;
    private TextView title;
    private TextView description;
    private ImageView photo;
    private int type;
    private List<Report.Detail> data;

    public DetailItemHolder(LayoutInflater inflater, int type) {
        super(inflater.inflate(R.layout.report_detail_item, null, false));
        this.type = type;
        this.icon = itemView.findViewById(R.id.icon);
        this.photo = itemView.findViewById(R.id.photo);
        this.title = itemView.findViewById(R.id.text);
        this.description = itemView.findViewById(R.id.description);
    }

    public DetailItemHolder(LayoutInflater inflater, List<Report.Detail> data) {
        super(inflater.inflate(R.layout.report_detail_item, null, false));
        this.data = data;
        this.type = 2;
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
            this.photo.setVisibility(View.VISIBLE);
            this.icon.setImageDrawable(BugTrackerApp.Drawable(R.drawable.ic_product));
            Picasso.with(BugTrackerApp.context)
                    .load(ProductsData.getProductByName(data.description).photo)
                    .transform(new CropCircleTransformation())
                    .placeholder(BugTrackerApp.Drawable(R.drawable.ic_product))
                    .into(this.photo);
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

    @Override
    public void click(View v) {
        if(type == 2) {
            UIController uic = ((MainActivity)v.getContext()).getController();
            uic.ReplaceFragment(new ReportDetailsFragment().setDetails(data), 0);
        }
    }
}
