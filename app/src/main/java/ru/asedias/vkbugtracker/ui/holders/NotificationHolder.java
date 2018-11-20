package ru.asedias.vkbugtracker.ui.holders;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.NotificationList;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;

/**
 * Created by rorom on 12.11.2018.
 */

public class NotificationHolder extends BindableHolder<NotificationList.Notification> {

    private ImageView photo;
    private TextView name;
    private TextView meta;
    private TextView comment;
    private LinearLayout attachments;
    public TextView date;

    public NotificationHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.report_comment, null, false));
        this.photo = itemView.findViewById(R.id.photo);
        this.name = itemView.findViewById(R.id.title);
        this.meta = itemView.findViewById(R.id.meta);
        this.comment = itemView.findViewById(R.id.comment);
        this.attachments = itemView.findViewById(R.id.attachments);
        this.date = itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bind(NotificationList.Notification data) {
        super.bind(data);
        Picasso.with(BugTrackerApp.context)
                .load(data.author_photo.contains("http") ? data.author_photo : "https://vk.com"+data.author_photo)
                .transform(new CropCircleTransformation())
                .into(this.photo);
        this.name.setText(data.author_name);
        if(data.text.length() > 0) {
            this.comment.setText(data.text);
            this.comment.setVisibility(View.VISIBLE);
        } else {
            this.comment.setVisibility(View.GONE);
        }
        this.date.setText(data.date);
        this.meta.setVisibility(View.GONE);
        this.attachments.setVisibility(View.GONE);
    }
}
