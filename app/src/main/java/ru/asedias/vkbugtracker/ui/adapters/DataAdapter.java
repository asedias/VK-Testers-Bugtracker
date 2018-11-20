package ru.asedias.vkbugtracker.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.api.webmethods.models.ListModel;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;

/**
 * Created by rorom on 20.11.2018.
 */

public class DataAdapter<I extends BindableHolder, V extends ListModel> extends RecyclerView.Adapter<I> {

    public V data;
    protected LayoutInflater inflater;

    public void setData(V data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(V data) {}

    @NonNull
    @Override
    public I onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.inflater = ((Activity) parent.getContext()).getLayoutInflater();
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull I holder, int position) {
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        if(data == null) return 0;
        return data.getSize();
    }
}
