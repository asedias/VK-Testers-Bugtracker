package ru.asedias.vkbugtracker.ui.holders.reportview;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.ImageGridParser;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;

/**
 * Created by rorom on 12.11.2018.
 */

public class CommentHolder extends BindableHolder<Report.Comment> {

    private ImageView photo;
    private TextView name;
    private TextView meta;
    private TextView comment;
    private LinearLayout attachments;
    public TextView date;
    private LayoutInflater inflater;

    public CommentHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.report_comment, null, false));
        this.inflater = inflater;
        this.photo = itemView.findViewById(R.id.photo);
        this.name = itemView.findViewById(R.id.title);
        this.meta = itemView.findViewById(R.id.meta);
        this.comment = itemView.findViewById(R.id.comment);
        this.attachments = itemView.findViewById(R.id.attachments);
        this.date = itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bind(Report.Comment data) {
        super.bind(data);
        Picasso.with(BugTrackerApp.context)
                .load(data.author_photo.contains("http") ? data.author_photo : "https://vk.com"+data.author_photo)
                .transform(new CropCircleTransformation())
                .into(this.photo);
        this.name.setText(data.author_name);
        this.comment.setMovementMethod(LinkMovementMethod.getInstance());
        if(data.text.length() > 0) {
            this.comment.setText(Html.fromHtml(data.text));
            this.comment.setVisibility(View.VISIBLE);
        } else {
            this.comment.setVisibility(View.GONE);
        }
        this.date.setText(data.date);
        if(data.meta_content != null) {
            this.meta.setVisibility(View.VISIBLE);
            this.meta.setText("");
            for(int i = 0; i < data.meta_content.size(); i++) {
                SpannableStringBuilder sb = new SpannableStringBuilder();
                if(i != 0 && i+1==data.meta_content.size()) sb.append("\n");
                String[] metas = data.meta_content.get(i).split("–");
                sb.append(metas[0]);
                sb.append("-");
                sb.append(metas[1]);
                sb.setSpan(new TextAppearanceSpan(BugTrackerApp.context, R.style.TextAppearance_AppCompat_Body2), metas[0].length() + 1, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(new ForegroundColorSpan(BugTrackerApp.Color(R.color.colorAccent)), metas[0].length() + 1, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                this.meta.append(sb);
                this.meta.setTextColor(BugTrackerApp.Color(R.color.colorAccent));
            }
        } else {
            this.meta.setVisibility(View.GONE);
        }
        this.attachments.removeAllViews();
        if(data.photos.size() > 0 || data.attachments.size() > 0) {
            this.attachments.setVisibility(View.VISIBLE);
            new ImageGridParser(data.photos, this.attachments, BugTrackerApp.mMetrics.widthPixels - BugTrackerApp.dp(96));
            for(int i = 0; i < data.attachments.size(); i++) {
                AttachmentHolder holder = new AttachmentHolder(inflater);
                holder.bind(data.attachments.get(i));
                this.attachments.addView(holder.itemView);
            }
        } else {
            this.attachments.setVisibility(View.GONE);
        }
    }
}
