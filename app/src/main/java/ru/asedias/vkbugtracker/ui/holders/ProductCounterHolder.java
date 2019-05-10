package ru.asedias.vkbugtracker.ui.holders;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.asedias.vkbugtracker.FragmentStackActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductInfo;
import ru.asedias.vkbugtracker.fragments.ReportListFragment;
import ru.asedias.vkbugtracker.ui.ThemeController;

/**
 * Created by rorom on 11.11.2018.
 */

public class ProductCounterHolder extends BindableHolder<ProductInfo> {

    private LinearLayout rootView;
    private LayoutInflater mInflater;

    public ProductCounterHolder(LayoutInflater inflater) {
        super(new LinearLayout(inflater.getContext()));
        this.rootView = (LinearLayout) itemView;
        this.mInflater = inflater;
    }

    @Override
    public void bind(ProductInfo info) {
        super.bind(info);
        List<ProductInfo.Counter> data = info.counters;
        this.rootView.removeAllViews();
        this.rootView.setGravity(Gravity.CENTER);
        for(int i = 0; i < data.size(); i++) {
            ProductInfo.Counter counter = data.get(i);
            View counterView = mInflater.inflate(R.layout.counter_item, null);
            TextView count = counterView.findViewById(R.id.count);
            TextView title = counterView.findViewById(R.id.label);
            count.setText(counter.title);
            count.setTextColor(ThemeController.getTextColor());
            title.setText(counter.description);
            if(counter.link.length() == 0) {
                counterView.setClickable(false);
            } else {
                counterView.setOnClickListener(v -> {
                    if(counter.toReports) {
                        ((FragmentStackActivity)v.getContext()).replaceFragment(ReportListFragment.newInstance(0, info.pid, counter.status, -1), 0);
                    }
                });
            }
            this.rootView.addView(counterView);
        }
    }
}
