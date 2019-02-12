package ru.asedias.vkbugtracker.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.GetProducts;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.adapters.ProductsAdapter;

/**
 * Created by rorom on 17.11.2018.
 */

public class ProductListFragment extends RecyclerFragment<ProductsAdapter> {

    private boolean all;
    private boolean needUpdate;

    public ProductListFragment() {
        this.mAdapter = new ProductsAdapter();
        this.title = BTApp.String(R.string.prefs_products);
        this.setTitleNeeded = false;
        this.needUpdate = ProductsData.getCacheData().getSize() == 0;
    }

    public static ProductListFragment newInstance(boolean all) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putBoolean("all", all);
        args.putBoolean("root", false);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = super.OnCreateContentView(inflater, container, savedInstanceState);
        DividerItemDecoration decoration = new DividerItemDecoration(new ColorDrawable(637534208));
        decoration.setPaddingLeft(BTApp.dp(80));
        this.mList.addItemDecoration(decoration);
        return root;
    }

    @Override
    public WebRequest getRequest() {
        if(this.getArguments() != null) {
            this.all = getArguments().getBoolean("all", false);
        }
        if(!needUpdate) {
            this.getAdapter().setData(ProductsData.getProducts(!all));
            this.showContent();
            return null;
        }
        return new GetProducts(this, this.all, data -> {
            return data;
        });
    }

    @Override
    public void onRefresh() {
        this.needUpdate = true;
        super.onRefresh();
    }
}
