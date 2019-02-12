package ru.asedias.vkbugtracker.api;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import pl.droidsonroids.jspoon.ElementConverter;
import pl.droidsonroids.jspoon.annotation.Selector;
import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;

/**
 * Created by rorom on 15.12.2018.
 */

public class DescriptionConverter implements ElementConverter<SpannableStringBuilder> {
    @Override
    public SpannableStringBuilder convert(Element node, Selector selector) {
        String temp = normalizeHrefs(node, selector).html();
        SpannableStringBuilder links = (SpannableStringBuilder) Html.fromHtml(Jsoup.parseBodyFragment(node.html(), "http://vk.com").html());
        RelativeSizeSpan[] spans = links.getSpans(0, links.length(), RelativeSizeSpan.class);
        for (RelativeSizeSpan span : spans) {
            int start = links.getSpanStart(span);
            int end = links.getSpanEnd(span);
            links.replace(end, end+1, "");
            links.removeSpan(span);
            links.setSpan(new TextAppearanceSpan(BTApp.context, R.style.TextAppearance_AppCompat_Body2), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            links.setSpan(new ForegroundColorSpan(BTApp.Color(R.color.detail_text)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        StyleSpan[] styleSpans = links.getSpans(0, links.length(), StyleSpan.class);
        for(StyleSpan span : styleSpans) links.removeSpan(span);
        return links;
    }

    public static Element normalizeHrefs(Element node, Selector selector) {
            for(Element element : node.select("a")) {
                String href = element.attr("href");
                if("href".startsWith("http")) {
                    href = "http://vk.com" + href;
                }
                element.attr("href", href);
            }
            return node;
    }
}
