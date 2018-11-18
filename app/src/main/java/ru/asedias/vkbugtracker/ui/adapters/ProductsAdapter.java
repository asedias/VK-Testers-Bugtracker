package ru.asedias.vkbugtracker.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;
import ru.asedias.vkbugtracker.ui.holders.ProductHolder;

/**
 * Created by rorom on 17.11.2018.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductHolder>  {

    private LayoutInflater mInflater;
    private ProductList data = new ProductList();

    public void setData(ProductList data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mInflater == null) {
            mInflater = ((Activity) parent.getContext()).getLayoutInflater();
        }
        return new ProductHolder(mInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.bind(data.products.get(position));
    }

    @Override
    public int getItemCount() {
        return data.products.size();
    }
}
