package ru.asedias.vkbugtracker.ui.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rorom on 20.10.2018.
 */

public class HeaderHolder extends BindableHolder<String> {
    public HeaderHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(String data) {
        ((TextView)this.itemView).setText(data);
    }
}
