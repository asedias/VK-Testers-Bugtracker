package ru.asedias.vkbugtracker.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.GetProducts;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.adapters.ProductsAdapter;

/**
 * Created by rorom on 17.11.2018.
 */

public class ProductListFragment extends RecyclerFragment<ProductsAdapter> {

    private boolean all;

    public ProductListFragment() {
        this.mAdapter = new ProductsAdapter();
        this.title = BugTrackerApp.String(R.string.prefs_products);
        this.setTitleNeeded = false;
    }

    public static ProductListFragment newInstance(boolean all) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putBoolean("all", all);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = super.OnCreateContentView(inflater, container, savedInstanceState);
        DividerItemDecoration decoration = new DividerItemDecoration(new ColorDrawable(637534208));
        decoration.setPaddingLeft(BugTrackerApp.dp(80));
        this.mList.addItemDecoration(decoration);
        return root;
    }

    @Override
    public WebRequest getRequest() {
        if(this.getArguments() != null) {
            this.all = getArguments().getBoolean("all", false);
        }
        return new GetProducts(this, this.all, data -> data);
    }
}
