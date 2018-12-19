package ru.asedias.vkbugtracker.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductInfo;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;
import ru.asedias.vkbugtracker.ui.holders.ProductCounterHolder;
import ru.asedias.vkbugtracker.ui.holders.HeaderHolder;
import ru.asedias.vkbugtracker.ui.holders.ProductHeaderHolder;
import ru.asedias.vkbugtracker.ui.holders.ProductHolder;
import ru.asedias.vkbugtracker.ui.holders.VersionHolder;

/**
 * Created by rorom on 15.12.2018.
 */

public class ViewProductAdapter extends RecyclerView.Adapter<BindableHolder> implements DividerItemDecoration.Provider {

    ProductInfo data = new ProductInfo();
    private LayoutInflater mInflater;
    private static final int TYPE_HEADER = -1;
    private static final int TYPE_PRODUCT_HEADER = 0;
    private static final int TYPE_COUNTER = 1;
    private static final int TYPE_PRODUCT = 2;
    private static final int TYPE_VERSION = 3;

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
        if(position == 1) {
            return TYPE_COUNTER;
        }
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

    @Override
    public void onBindViewHolder(@NonNull BindableHolder holder, int position) {
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(getItemViewType(position) == TYPE_PRODUCT_HEADER) {
            holder.bind(data);
        } else if(getItemViewType(position) == TYPE_COUNTER) {
            holder.bind(data.counters);
        } else if(getItemViewType(position) == TYPE_HEADER) {
            if(data.products.size() > 0 && position == 2) {
                holder.bind(BugTrackerApp.String(R.string.branches));
            } else {
                holder.bind(BugTrackerApp.String(R.string.versions));
            }
        } else if(getItemViewType(position) == TYPE_PRODUCT) {
            int pre = 4 + (data.versions.size() > 0 ? data.versions.size() + 1 : 0);
            holder.bind(ProductsData.getProduct(data.products.get(getItemCount() - pre).id));
            lp.bottomMargin = BugTrackerApp.dp(8);
        } else if(getItemViewType(position) == TYPE_VERSION) {
            int pre = 4 + (data.products.size() > 0 ? data.products.size() + 1 : 0);
            holder.bind(data.versions.get(getItemCount() - pre));
        }
        holder.itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return data.product == null ? 0 :
                1 + (data.counters.size() > 0 ? 1 : 0) +
                        (data.products.size() > 0 ? 1 + data.products.size() : 0) +
                        (data.versions.size() > 0 ? 1 + data.versions.size() : 0);
    }

    @Override
    public boolean needDrawDividerAfter(int var1) {
        return getItemViewType(var1) != TYPE_HEADER;
    }

    @Nullable
    @Override
    public boolean needMarginBottom(int var1) {
        return getItemViewType(var1) == TYPE_PRODUCT;
    }
}
