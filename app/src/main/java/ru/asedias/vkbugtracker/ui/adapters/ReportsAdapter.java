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
import ru.asedias.vkbugtracker.ui.holders.LoadingHolder;
import ru.asedias.vkbugtracker.ui.holders.ReportItemHolder;

/**
 * Created by rorom on 20.10.2018.
 */

public class ReportsAdapter extends DataAdapter<BindableHolder, ReportList> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_REPORT = 1;
    private static final int TYPE_LOADING = 2;

    public ReportsAdapter() {
        this.data = new ReportList();
    }

    @Override
    public void addData(ReportList data) {
        data.reports.remove(0);
        this.data.reports.addAll(data.reports);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BindableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        if(viewType == TYPE_HEADER) return new HeaderHolder(inflater);
        if(viewType == TYPE_REPORT) return new ReportItemHolder(inflater.inflate(R.layout.report_item, null, false), inflater);
        return new LoadingHolder(inflater);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : position != getItemCount() - 1 ? TYPE_REPORT : TYPE_LOADING;
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
            default:
                holder.bind(null);
        }
    }

    @Override
    public int getItemCount() {
        return data.reports.size() > 0 ? data.reports.size() + 2 : 0;
    }
}
