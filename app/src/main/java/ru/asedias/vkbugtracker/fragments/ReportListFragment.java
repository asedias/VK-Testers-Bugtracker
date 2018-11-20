package ru.asedias.vkbugtracker.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;
import ru.asedias.vkbugtracker.api.webmethods.GetProducts;
import ru.asedias.vkbugtracker.api.webmethods.GetReportList;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.ui.adapters.ReportsAdapter;
import ru.asedias.vkbugtracker.ui.holders.LoadingHolder;

/**
 * Created by rorom on 20.10.2018.
 */

public class ReportListFragment extends RecyclerFragment<ReportsAdapter> {

    private int btUDate = 0;

    public ReportListFragment() {
        this.mAdapter = new ReportsAdapter();
        this.setTitleNeeded = false;
    }

    @Override
    public WebRequest getRequest() {
        return new GetReportList(this, 0, btUDate, false, body -> {
            this.btUDate = Integer.parseInt(body.btUDate.get(body.btUDate.size() - 1));
            if(body.reports.size() > 0) {
                getUsers(body);
            }
            return body;
        });
    }

    private void getUsers(ReportList data) {
        String ids = "";
        for(int i = 0; i < data.reports.size(); i++) {
            ids+=data.reports.get(i).uid + ", ";
        }
        request = new GetUserInfo(ids, new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                List<UserInfo.User> users = response.body().getResponse();
                for(int i = 0; i < getAdapter().data.reports.size(); i++) {
                    for (int i2 = 0; i2 < users.size(); i2++) {
                        UserInfo.User user = users.get(i2);
                        if(getAdapter().data.reports.get(i).uid == user.getId()) {
                            getAdapter().data.reports.get(i).user = user;
                            break;
                        }
                    }
                }
                getProducts();
            }
            @Override public void onFailure(Call<UserInfo> call, Throwable t) {
                showError(t.getLocalizedMessage());
            }
        });
        request.execute();
    }

    private void getProducts() {
        this.request = new GetProducts(true, new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                ProductList data = response.body();
                for(int i = 0; i < getAdapter().data.reports.size(); i++) {
                    for (int i2 = 0; i2 < data.products.size(); i2++) {
                        ReportList.ReportItem item = getAdapter().data.reports.get(i);
                        ProductList.Product product = data.products.get(i2);
                        if(item.product_id == product.id) {
                            item.product = product;
                            break;
                        }
                    }
                }
                getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                showError(t.getLocalizedMessage());
            }
        });
        this.request.execute();
    }

    @Override
    public boolean canLoadMode() { return true; }

    @Override
    public void onRefresh() {
        this.btUDate = 0;
        getAdapter().data = new ReportList();
        super.onRefresh();
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = super.OnCreateContentView(inflater, container, savedInstanceState);
        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = mList.getLayoutManager().getItemCount();
                int lastVisibleItem = ((LinearLayoutManager)mList.getLayoutManager()).findLastVisibleItemPosition();
                int visibleThreshold = 5;
                if (!isLoadingMore && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    loadMore(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return root;
    }

    @Override
    public void showError(Throwable t) {
        if (!canLoadMode()) {
            super.showError(t);
        } else {
            if(getAdapter().data.reports.size() > 0) {
                int totalItemCount = mList.getLayoutManager().getItemCount();
                RecyclerView.ViewHolder holder = mList.findViewHolderForLayoutPosition(totalItemCount - 1);
                if(holder instanceof LoadingHolder) {
                    ((LoadingHolder) holder).showError(t);
                } else {
                    super.showError(t);
                }
                return;
            }
        }
        super.showError(t);
    }
}
