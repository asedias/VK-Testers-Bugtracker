package ru.asedias.vkbugtracker.ui.holders.reportview;

import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.OverlayCircleTranformation;
import ru.asedias.vkbugtracker.ui.ThemeController;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;

/**
 * Created by rorom on 11.11.2018.
 */

public class FooterHolder extends BindableHolder<Report.Footer> {

    private TextView reproduce;
    private ImageView like;
    private ImageView share;
    private FrameLayout photos;
    private LayoutInflater inflater;
    private int rid = 0;

    public FooterHolder(LayoutInflater inflater, int rid) {
        super(inflater.inflate(R.layout.report_footer, null, false));
        this.inflater = inflater;
        this.rid = rid;
        this.reproduce = itemView.findViewById(R.id.reproduce);
        this.like = itemView.findViewById(R.id.like);
        this.share = itemView.findViewById(R.id.share);
        this.photos = itemView.findViewById(R.id.photosWrap);
    }

    @Override
    public void bind(Report.Footer data) {
        super.bind(data);
        photos.removeAllViews();
        int size = BTApp.dp(24);
        int margin = BTApp.dp(3);
        int i = 0;
        if(data.users != null && data.users.size() > 0) {
            for(i = 0; i < data.users.size()-1; i++) {
                ImageView photo = new ImageView(itemView.getContext());
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
                lp.leftMargin = i*(size - margin);
                photos.addView(photo, lp);
                Picasso.with(BTApp.context)
                        .load(data.users.get(i).photo.contains("http") ? data.users.get(i).photo : "https://vk.com" + data.users.get(i).photo)
                        .transform(new CropCircleTransformation())
                        .transform(new OverlayCircleTranformation(0.64F))
                        .into(photo);
            }
        }
        this.reproduce.setTextColor(ThemeController.getTextColor());
        TextView count = (TextView) inflater.inflate(R.layout.footer_text, null);
        count.setText(String.valueOf(data.reproduce_count));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
        lp.leftMargin = i*size - i*margin;
        photos.addView(count, lp);
        share.setOnClickListener(v -> {
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            String link = String.format(Locale.getDefault(), "%s%d", API.URL_BASE + "?act=show&id=", rid);
            share.putExtra(Intent.EXTRA_TEXT, link);
            share.setType("text/plain");
            itemView.getContext().startActivity(Intent.createChooser(share, link));
        });
    }
}
