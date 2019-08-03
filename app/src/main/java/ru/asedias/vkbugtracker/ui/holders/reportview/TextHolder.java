package ru.asedias.vkbugtracker.ui.holders.reportview;

import android.support.v4.widget.TextViewCompat;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.ui.Fonts;
import ru.asedias.vkbugtracker.ui.ThemeController;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;

/**
 * Created by rorom on 20.10.2018.
 */

public class TextHolder extends BindableHolder {
    public TextHolder(LayoutInflater inflater) {
        super(new TextView(inflater.getContext()));
    }

    @Override
    public void bind(Object data) {
        super.bind(data);
        ((TextView) this.itemView).setPadding(BTApp.dp(16), BTApp.dp(8), BTApp.dp(16), BTApp.dp(8));
        TextViewCompat.setTextAppearance((TextView)this.itemView, R.style.TextAppearance_AppCompat_Body1);
        ((TextView) this.itemView).setTextSize(15);
        ((TextView) this.itemView).setTypeface(Fonts.Regular);
        ((TextView) this.itemView).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) this.itemView).setClickable(true);
        ((TextView) this.itemView).setTextColor(ThemeController.getTextColor());
        if (data instanceof CharSequence) {
            ((TextView) this.itemView).setText((CharSequence) data);
        } else if(data instanceof SpannableStringBuilder) {
            ((TextView) this.itemView).setText((SpannableStringBuilder) data);
        }
    }
}
