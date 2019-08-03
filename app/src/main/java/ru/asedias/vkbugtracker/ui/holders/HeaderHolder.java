package ru.asedias.vkbugtracker.ui.holders;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.ui.ThemeController;

/**
 * Created by rorom on 20.10.2018.
 */

public class HeaderHolder extends BindableHolder {
    public HeaderHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.report_header, null, false));
    }

    @Override
    public void bind(Object data) {
        super.bind(data);
        ((TextView) this.itemView).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) this.itemView).setClickable(true);
        if (data instanceof CharSequence) {
            ((TextView) this.itemView).setText((CharSequence) data);
        } else if(data instanceof SpannableStringBuilder) {
            ((TextView) this.itemView).setText((SpannableStringBuilder) data);
        }
    }
}
