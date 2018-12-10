package ru.asedias.vkbugtracker.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.api.webmethods.models.ListModel;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;
import ru.asedias.vkbugtracker.ui.holders.LoadingHolder;

/**
 * Created by rorom on 20.11.2018.
 */

public class DataAdapter<I extends BindableHolder, V extends ListModel> extends RecyclerView.Adapter<I> {

    public V data;
    protected LayoutInflater inflater;
    public boolean isLoadingAdapter = false;
    protected static final int TYPE_LOADING = Integer.MAX_VALUE;
    private Throwable error;

    public DataAdapter(boolean isLoadingAdapter) {
        this.isLoadingAdapter = isLoadingAdapter;
    }

    public DataAdapter() {
        this.isLoadingAdapter = false;
    }

    public void setData(V data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(V data) {}

    public void setError(Throwable error) {
        this.error = error;
    }

    @NonNull
    @Override
    public I onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.inflater = ((Activity) parent.getContext()).getLayoutInflater();
        return (I) new LoadingHolder(inflater);
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_LOADING;
    }

    @Override
    public void onBindViewHolder(@NonNull I holder, int position) {
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if(position > data.getSize()) {
            holder.bind(error);
        } else {
            holder.bind(data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(data == null) return 0;
        return data.getSize() + (isLoadingAdapter ? 1 : 0);
    }
}
