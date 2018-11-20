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

public class ProductsAdapter extends DataAdapter<ProductHolder, ProductList>  {

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        return new ProductHolder(inflater);
    }
}
