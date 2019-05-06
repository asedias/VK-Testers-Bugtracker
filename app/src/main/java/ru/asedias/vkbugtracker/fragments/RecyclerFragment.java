package ru.asedias.vkbugtracker.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.ui.adapters.DataAdapter;
import ru.asedias.vkbugtracker.ui.holders.LoadingHolder;

/**
 * Created by rorom on 20.10.2018.
 */

public class RecyclerFragment<I extends RecyclerView.Adapter> extends LoaderFragment implements SwipeRefreshLayout.OnRefreshListener {

    protected RecyclerView mList;
    protected View mEmptyView;
    protected I mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    protected SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View tempContent = inflater.inflate(R.layout.appkit_recycler_fragment, null, false);
        this.mList = tempContent.findViewById(R.id.list);
        this.mEmptyView = tempContent.findViewById(R.id.empty);
        this.mSwipeRefresh = tempContent.findViewById(R.id.refresh_layout);
        this.mSwipeRefresh.setOnRefreshListener(this);
        this.mSwipeRefresh.setRefreshing(isRefreshing);
        this.mSwipeRefresh.setEnabled(false);
        this.mSwipeRefresh.setColorSchemeColors(BTApp.Color(R.color.colorAccent));
        this.mSwipeRefresh.setProgressViewOffset(true, BTApp.dp(56), BTApp.dp(112));
        this.mList.setLayoutManager(getLayoutManager());
        this.mList.setAdapter(getAdapter());
        this.mList.setPadding(0, BTApp.dp(56 + cardOffset), 0, 0);
        if(canLoadMode()) {
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
        }
        return tempContent;
    }

    public I getAdapter() {
        return mAdapter;
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        if(this.mLayoutManager == null) this.mLayoutManager = new LinearLayoutManager(getActivity());
        return mLayoutManager;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getAdapter() != null) {
            getAdapter().notifyDataSetChanged();
        }
        if(isRequestRunning()||!isRequestDone()) showProgress();
    }

    public void setScrollToTop() {
        mLayoutManager.smoothScrollToPosition(mList, null, 0);
    }

    @Override
    public void showError(Throwable t) {
        if (!canLoadMode()) {
            super.showError(t);
        } else {
            if(getAdapter() instanceof DataAdapter && ((DataAdapter)getAdapter()).data.getSize() > 0
                    && ((DataAdapter)getAdapter()).isLoadingAdapter) {
                //int totalItemCount = mList.getLayoutManager().getItemCount();
                //RecyclerView.ViewHolder holder = mList.findViewHolderForLayoutPosition(totalItemCount - 1);
                //if(holder instanceof LoadingHolder) {
                    this.mRequestDone = true;
                    this.mRequestRunning = false;
                    this.isRefreshing = false;
                    this.isLoadingMore = false;
                    //(LoadingHolder) holder).showError(t);
                    ((DataAdapter)getAdapter()).setError(t);
                    getAdapter().notifyDataSetChanged();
                //} else {
                    //super.showError(t);
                //}
            } else {
                super.showError(t);
            }
        }
    }

    @Override
    public void showContent() {
        this.isRefreshing = false;
        this.mSwipeRefresh.setRefreshing(isRefreshing);
        if(getAdapter() instanceof DataAdapter) {
            if(((DataAdapter)getAdapter()).data.getSize() == 0) {
                showEmptyText();
                return;
            }
        }
        this.mSwipeRefresh.setEnabled(true);
        super.showContent();
    }

    @Override
    public void reExecuteRequest() {
        this.request.cancel();
        this.loadMore(false);
        if(!canLoadMode()) showProgress();
    }

    @Override
    public void onRefresh() {
        this.isRefreshing = true;
        if(!isRequestRunning()) {
            this.request = getRequest();
            if (request != null) {
                mRequestRunning = true;
                request.execute();
            }
        }
    }
}
