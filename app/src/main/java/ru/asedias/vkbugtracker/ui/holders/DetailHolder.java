package ru.asedias.vkbugtracker.ui.holders;

import android.view.LayoutInflater;
import android.widget.TextView;

import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;

/**
 * Created by rorom on 20.10.2018.
 */

public class DetailHolder extends BindableHolder<Report.Detail> {

    private TextView title;
    private TextView subtitle;

    public DetailHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.detail_item, null, false));
        this.title = itemView.findViewById(R.id.title);
        this.subtitle = itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bind(Report.Detail data) {
        super.bind(data);
        this.title.setText(data.title);
        this.subtitle.setText(data.description);
    }
}
