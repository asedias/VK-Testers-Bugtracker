package ru.asedias.vkbugtracker.fragments;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.UserData;
import ru.asedias.vkbugtracker.api.WebRequest;

/**
 * Created by rorom on 20.10.2018.
 */

public class LoaderFragment extends UICFragment {

    protected View mContainer;
    protected FrameLayout mContent;
    protected WebRequest request;
    protected boolean mRequestDone;
    protected View mLoading;
    protected View mErrorView;
    protected boolean mRequestRunning;
    protected boolean isRefreshing = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(this.mContainer == null) {
            this.mContainer = inflater.inflate(R.layout.appkit_loader_fragment, container, false);
            this.mLoading = this.mContainer.findViewById(R.id.loading);
            this.mErrorView = this.mContainer.findViewById(R.id.error);
            this.mContent = this.mContainer.findViewById(R.id.content_stub);
            this.mContent.removeAllViews();
            this.mContent.addView(OnCreateContentView(inflater, container, savedInstanceState));
            mRequestDone = savedInstanceState != null && savedInstanceState.getBoolean("mRequestDone", false);
        }
        return this.mContainer;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mRequestDone", mRequestDone);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.request.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mContainer != null) {
            ViewGroup parent = (ViewGroup) this.mContainer.getParent();
            if (parent != null) {
                parent.removeView(this.mContainer);
            }
        }
    }

    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    protected void showContent() {
        this.mErrorView.setVisibility(View.GONE);
        this.mLoading.setVisibility(View.GONE);
        this.mContent.setVisibility(View.VISIBLE);
        this.mRequestDone = true;
        this.mRequestRunning = false;
        this.isRefreshing = false;
    }

    protected void showProgress() {
        this.mErrorView.setVisibility(View.GONE);
        this.mLoading.setVisibility(View.VISIBLE);
        this.mContent.setVisibility(View.GONE);
        this.mRequestDone = false;
        this.mRequestRunning = true;
        this.isRefreshing = false;
    }

    public WebRequest getRequest() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.request = getRequest();
        executeRequestIfNeeded();
    }

    public boolean isViewCreated() {
        return this.mContainer != null;
    }

    public boolean isRequestDone() {
        return mRequestDone;
    }

    public boolean isRequestRunning() { return mRequestRunning; }

    public void processUrl(Call call, Response response) {
        if(!response.raw().request().url().toString().equals(call.request().url().toString())) {
            showError("REDIRECT: " + response.raw().request().url().toString());
            //Log.e("Request", "REDIRECT: " + response.raw().request().url().toString());
        }
    }

    public void showError(String text) {
        this.mErrorView.setVisibility(View.VISIBLE);
        this.mLoading.setVisibility(View.GONE);
        this.mContent.setVisibility(View.GONE);
        ((TextView)this.mErrorView.findViewById(R.id.error_text)).setText(text);
        this.mErrorView.findViewById(R.id.error_retry).setOnClickListener(v -> {
            reExecuteRequest();
        });
        this.mRequestDone = false;
        this.mRequestRunning = false;
        this.isRefreshing = false;
    }

    public void reExecuteRequest() {
        showProgress();
        this.request.cancel();
        this.request = getRequest();
        this.request.execute();
        this.mRequestRunning = true;
    }

    public void executeRequestIfNeeded() {
        if(request != null && !isRequestDone() && !isRequestRunning()) {
            request.execute();
            this.mRequestRunning = true;
        }
    }
}
