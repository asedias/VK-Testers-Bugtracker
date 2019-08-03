package ru.asedias.vkbugtracker.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.ui.CardItemDecorator;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.LayoutHelper;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;
import ru.asedias.vkbugtracker.ui.holders.DetailHolder;
import ru.asedias.vkbugtracker.ui.holders.HeaderHolder;
import ru.asedias.vkbugtracker.ui.holders.ProductHolder;

/**
 * Created by rorom on 27.11.2018.
 */

public class DetailsAdapter extends RecyclerView.Adapter<BindableHolder> implements DividerItemDecoration.Provider, CardItemDecorator.Provider {

    private List<Report.Detail> data;
    private LayoutInflater mInflater;
    private final static int TYPE_HEADER = 0;
    private final static int TYPE_PRODUCT = 1;
    private final static int TYPE_DETAIL = 2;

    public void setData(List<Report.Detail> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BindableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mInflater == null) {
            mInflater = ((Activity) parent.getContext()).getLayoutInflater();
        }
        if(viewType == TYPE_PRODUCT) return new ProductHolder(mInflater);
        if(viewType == TYPE_DETAIL) return new DetailHolder(mInflater);
        return new HeaderHolder(mInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull BindableHolder holder, int position) {
        RecyclerView.LayoutParams lp = LayoutHelper.fullWidthRecycler();
        int viewType = getItemViewType(position);
        if(viewType == TYPE_PRODUCT) {
            holder.bind(ProductsData.getProductByName(data.get(0).description));
        }
        if(viewType == TYPE_HEADER) {
            int id = position == 0 ? R.string.addr_product : R.string.general_info;
            holder.bind(BTApp.String(id));
        }
        if(viewType == TYPE_DETAIL) {
            holder.bind(data.get(position - 2));
        }
        holder.itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return TYPE_HEADER;
        if(position == 1) return TYPE_PRODUCT;
        if(position == 2) return TYPE_HEADER;
        return TYPE_DETAIL;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size() + 2;
    }

    @Override
    public boolean needDrawDividerAfter(int var1) {
        return getItemViewType(var1) == TYPE_DETAIL && var1 != getItemCount() - 1;
    }

    @Nullable
    @Override
    public boolean needMarginBottom(int var1) {
        if(var1 == 1) return true;
        return false;
    }

    @Override
    public int getBlockType(int var1) {
        int type = getItemViewType(var1);
        if(type == TYPE_HEADER) return CardItemDecorator.TOP;
        if(var1 < getItemCount() - 2 && getItemViewType(var1 + 1) == TYPE_HEADER) return CardItemDecorator.BOTTOM;
        if(var1 == getItemCount() - 1) return CardItemDecorator.BOTTOM;
        return CardItemDecorator.MIDDLE;
    }
}
