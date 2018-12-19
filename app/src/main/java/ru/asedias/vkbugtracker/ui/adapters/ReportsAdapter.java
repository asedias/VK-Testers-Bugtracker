package ru.asedias.vkbugtracker.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;
import ru.asedias.vkbugtracker.ui.holders.BindableHolderInterface;
import ru.asedias.vkbugtracker.ui.holders.HeaderHolder;
import ru.asedias.vkbugtracker.ui.holders.LoadingHolder;
import ru.asedias.vkbugtracker.ui.holders.ReportItemHolder;

/**
 * Created by rorom on 20.10.2018.
 */

public class ReportsAdapter extends DataAdapter<BindableHolder, ReportList> implements DividerItemDecoration.Provider {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_REPORT = 1;
    private boolean showProduct = true;

    public ReportsAdapter() {
        super(true);
        this.data = new ReportList();
    }

    public void setShowProduct(boolean showProduct) {
        this.showProduct = showProduct;
    }

    @Override
    public void addData(ReportList data) {
        setError(null);
        data.reports.remove(0);
        if(data.reports.size() == 0) isLoadingAdapter = false;
        this.data.reports.addAll(data.reports);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BindableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        if(viewType == TYPE_HEADER) return new HeaderHolder(inflater);
        if(viewType == TYPE_REPORT) return new ReportItemHolder(inflater.inflate(R.layout.report_item, null, false), inflater);
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return TYPE_HEADER;
        } else {
            if(isLoadingAdapter) {
                if(position != getItemCount() - 1) {
                    return TYPE_REPORT;
                }
            } else {
                return TYPE_REPORT;
            }
        }
        return super.getItemViewType(position);
        //return position == 0 ? TYPE_HEADER : position != getItemCount() - 1 ? TYPE_REPORT : super.getItemViewType(position);
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
                ((ReportItemHolder)holder).showProduct(this.showProduct);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return data.getSize() > 0 ? 1 + super.getItemCount() : 0;
    }

    @Override
    public boolean needDrawDividerAfter(int var1) {
        return getItemViewType(var1) == TYPE_REPORT;
    }

    @Nullable
    @Override
    public boolean needMarginBottom(int var1) {
        return false;
    }
}
