package ru.asedias.vkbugtracker.ui.holders.reportview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;

/**
 * Created by rorom on 11.11.2018.
 */

public class AttachmentHolder extends BindableHolder<Report.Attachment> {

    public ImageView icon;
    public TextView title;
    public TextView subtitle;

    public AttachmentHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.report_doc, null, false));
        this.icon = itemView.findViewById(R.id.photo);
        this.title = itemView.findViewById(R.id.title);
        this.subtitle = itemView.findViewById(R.id.subtitle);
    }

    public AttachmentHolder(int id, LayoutInflater inflater) {
        super(inflater.inflate(id, null, false));
        this.icon = itemView.findViewById(R.id.photo);
        this.title = itemView.findViewById(R.id.title);
        this.subtitle = itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bind(Report.Attachment data) {
        super.bind(data);
        this.title.setText(data.title);
        this.subtitle.setText(data.description);
        int icon_id = R.drawable.ic_doc;
        switch (data.type) {
            case 1: {
                icon_id = R.drawable.ic_doc_text;
                break;
            }
            case 6: {
                icon_id = R.drawable.ic_doc_video;
                break;
            }
        }
        this.icon.setImageResource(icon_id);
    }
}
