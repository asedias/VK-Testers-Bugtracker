package ru.asedias.vkbugtracker.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductInfo;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.fragments.CardRecyclerFragment;
import ru.asedias.vkbugtracker.ui.CardItemDecorator;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.LayoutHelper;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;
import ru.asedias.vkbugtracker.ui.holders.ProductCounterHolder;
import ru.asedias.vkbugtracker.ui.holders.HeaderHolder;
import ru.asedias.vkbugtracker.ui.holders.ProductHeaderHolder;
import ru.asedias.vkbugtracker.ui.holders.ProductHolder;
import ru.asedias.vkbugtracker.ui.holders.VersionHolder;

/**
 * Created by rorom on 15.12.2018.
 */

public class ViewProductAdapter extends RecyclerView.Adapter<BindableHolder> implements DividerItemDecoration.Provider, CardItemDecorator.Provider {

    ProductInfo data = new ProductInfo();
    private LayoutInflater mInflater;
    private static final int TYPE_HEADER = -1;
    private static final int TYPE_PRODUCT_HEADER = 0;
    private static final int TYPE_COUNTER = 1;
    private static final int TYPE_PRODUCT = 2;
    private static final int TYPE_VERSION = 3;
    private int pid = 0;

    public void setData(ProductInfo data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BindableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mInflater == null) {
            mInflater = ((Activity) parent.getContext()).getLayoutInflater();
        }
        if(viewType == TYPE_PRODUCT_HEADER) return new ProductHeaderHolder(mInflater);
        if(viewType == TYPE_COUNTER) return new ProductCounterHolder(mInflater);
        if(viewType == TYPE_PRODUCT) return new ProductHolder(mInflater);
        if(viewType == TYPE_VERSION) return new VersionHolder(mInflater);
        return new HeaderHolder(mInflater);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return TYPE_PRODUCT_HEADER;
        if(position == 1) return TYPE_COUNTER;
        if(data.products.size() > 0) {
            if(position == 2) return TYPE_HEADER;
            if(position <= data.products.size() + 2) {
                return TYPE_PRODUCT;
            }
            position-=1;
        }
        position-=data.products.size();
        if(data.versions.size() > 0) {
            if(position == 2) return TYPE_HEADER;
            if(position <= data.versions.size() + 2) {
                return TYPE_VERSION;
            }
        }
        return TYPE_HEADER;
    }

    private int branchStart;
    private int versionStart;
    @Override
    public void onBindViewHolder(@NonNull BindableHolder holder, int position) {
        RecyclerView.LayoutParams lp = LayoutHelper.fullWidthRecycler();
        if(getItemViewType(position) == TYPE_PRODUCT_HEADER) {
            holder.bind(data);
        } else if(getItemViewType(position) == TYPE_COUNTER) {
            holder.bind(data);
        } else if(getItemViewType(position) == TYPE_HEADER) {
            if(data.products.size() > 0 && position == 2) {
                holder.bind(BTApp.String(R.string.branches));
                branchStart = position + 1;
            } else {
                holder.bind(BTApp.String(R.string.versions));
                versionStart = position + 1;
            }
        } else if(getItemViewType(position) == TYPE_PRODUCT) {
            if(position - branchStart < data.products.size()) {
                holder.bind(ProductsData.getProduct(data.products.get(position - branchStart).id));
            }
        } else if(getItemViewType(position) == TYPE_VERSION) {
            holder.bind(data.versions.get(position - versionStart));
        }
        holder.itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return data.title == null ? 0 :
                1 + (data.counters.size() > 0 ? 1 : 0) +
                        (data.products.size() > 0 ? 1 + data.products.size() : 0) +
                        (data.versions.size() > 0 ? 1 + data.versions.size() : 0);
    }

    @Override
    public boolean needDrawDividerAfter(int var1) {
        return var1 < getItemCount() - 1 & getItemViewType(var1+1) != TYPE_HEADER & getItemViewType(var1) != TYPE_HEADER;
    }

    @Nullable
    @Override
    public boolean needMarginBottom(int var1) {
        return getItemViewType(var1) == TYPE_PRODUCT;
    }

    public int getPid() {
        return pid;
    }

    public ViewProductAdapter setPid(int pid) {
        this.pid = pid;
        return this;
    }

    @Override
    public int getBlockType(int var1) {
        if(var1 < getItemCount() - 1 & getItemViewType(var1+1) == TYPE_HEADER || var1 == getItemCount() - 1) return CardItemDecorator.BOTTOM;
        if(getItemViewType(var1) == TYPE_HEADER) return CardItemDecorator.TOP;
        if(var1 == 0) return CardItemDecorator.TOP;
        return CardItemDecorator.MIDDLE;
    }
}
