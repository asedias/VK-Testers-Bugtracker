package ru.asedias.vkbugtracker.ui.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ru.asedias.vkbugtracker.R;

/**
 * Created by rorom on 20.10.2018.
 */

public class HeaderHolder extends BindableHolder<String> {
    public HeaderHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.report_header, null));
    }

    @Override
    public void bind(String data) {
        super.bind(data);
        ((TextView)this.itemView).setText(data);
    }
}
