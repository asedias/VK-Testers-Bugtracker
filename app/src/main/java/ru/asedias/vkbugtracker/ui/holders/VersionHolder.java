package ru.asedias.vkbugtracker.ui.holders;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.widget.TextView;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductInfo;

/**
 * Created by rorom on 19.12.2018.
 */

public class VersionHolder extends BindableHolder<ProductInfo.Version> {

    private TextView title;
    private TextView changelog;

    public VersionHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.product_version_item, null, false));
        this.title = itemView.findViewById(R.id.title);
        this.changelog = itemView.findViewById(R.id.changelog);
    }

    @Override
    public void bind(ProductInfo.Version data) {
        super.bind(data);
        this.changelog.setText(Html.fromHtml(data.release_notes));
        this.changelog.setClickable(true);
        this.changelog.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder ss = new SpannableStringBuilder(data.title);
        ss.append(" ");
        ss.append(data.date);
        ss.setSpan(new ForegroundColorSpan(BTApp.Color(R.color.darker_gray)), data.title.length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.title.setText(ss);
    }
}
