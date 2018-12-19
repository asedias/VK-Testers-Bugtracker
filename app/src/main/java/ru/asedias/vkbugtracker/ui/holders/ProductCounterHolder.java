package ru.asedias.vkbugtracker.ui.holders;

import android.content.res.ColorStateList;
import android.support.v4.widget.TextViewCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductInfo;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.fragments.ReportDetailsFragment;
import ru.asedias.vkbugtracker.fragments.ReportListFragment;
import ru.asedias.vkbugtracker.fragments.ViewProductFragment;
import ru.asedias.vkbugtracker.fragments.ViewReportFragment;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.Fonts;
import ru.asedias.vkbugtracker.ui.UIController;

/**
 * Created by rorom on 11.11.2018.
 */

public class ProductCounterHolder extends BindableHolder<List<ProductInfo.Counter>> {

    private LinearLayout rootView;
    private LayoutInflater mInflater;

    public ProductCounterHolder(LayoutInflater inflater) {
        super(new LinearLayout(inflater.getContext()));
        this.rootView = (LinearLayout) itemView;
        this.mInflater = inflater;
    }

    @Override
    public void bind(List<ProductInfo.Counter> data) {
        super.bind(data);
        this.rootView.removeAllViews();
        this.rootView.setGravity(Gravity.CENTER);
        for(int i = 0; i < data.size(); i++) {
            ProductInfo.Counter counter = data.get(i);
            View counterView = mInflater.inflate(R.layout.counter_item, null);
            TextView count = counterView.findViewById(R.id.count);
            TextView title = counterView.findViewById(R.id.label);
            count.setText(counter.title);
            title.setText(counter.description);
            if(counter.link.length() == 0) {
                counterView.setClickable(false);
            } else {
                counterView.setOnClickListener(v -> {
                    if(counter.toReports) {
                        UIController uic = ((MainActivity)v.getContext()).getController();
                        uic.ReplaceFragment(ReportListFragment.newInstance(0, counter.product, counter.status, 0), 0);
                    }
                });
            }
            this.rootView.addView(counterView);
        }
    }
}
