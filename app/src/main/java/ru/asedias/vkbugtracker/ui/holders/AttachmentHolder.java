package ru.asedias.vkbugtracker.ui.holders;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;

/**
 * Created by rorom on 11.11.2018.
 */

public class AttachmentHolder extends BindableHolder<Report.Attachment> {

    private ImageView icon;
    private TextView name;
    private TextView subtitle;

    public AttachmentHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.report_doc, null, false));
        this.icon = itemView.findViewById(R.id.photo);
        this.name = itemView.findViewById(R.id.title);
        this.subtitle = itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bind(Report.Attachment data) {
        super.bind(data);
        this.name.setText(data.title);
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
