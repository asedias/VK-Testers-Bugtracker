package ru.asedias.vkbugtracker.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;
import ru.asedias.vkbugtracker.ui.holders.BindableHolderInterface;
import ru.asedias.vkbugtracker.ui.holders.HeaderHolder;
import ru.asedias.vkbugtracker.ui.holders.ReportItemHolder;

/**
 * Created by rorom on 20.10.2018.
 */

public class ReportsAdapter extends RecyclerView.Adapter<BindableHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_REPORT = 1;
    private LayoutInflater mInflater;
    private ReportList data = new ReportList();

    public void setData(ReportList data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void appendItems(List<ReportList.ReportItem> data) {
        this.data.reports.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BindableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mInflater == null) {
            mInflater = ((Activity) parent.getContext()).getLayoutInflater();
        }
        if(viewType == TYPE_HEADER) return new HeaderHolder(mInflater);
        return new ReportItemHolder(mInflater.inflate(R.layout.report_item, null), mInflater);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_REPORT;
    }

    @Override
    public void onBindViewHolder(@NonNull BindableHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_HEADER:
                holder.bind(data.reports_found);
                break;
            case TYPE_REPORT:
                holder.bind(data.reports.get(position-1));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.reports.size() > 0 ? data.reports.size() + 1 : 0;
    }
}
