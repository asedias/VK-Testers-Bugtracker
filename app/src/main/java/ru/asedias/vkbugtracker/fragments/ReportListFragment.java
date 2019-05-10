package ru.asedias.vkbugtracker.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.Actions;
import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;
import ru.asedias.vkbugtracker.api.webmethods.GetReportList;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.adapters.ReportsAdapter;
import ru.asedias.vkbugtracker.ui.holders.reportview.AttachmentHolder;

/**
 * Created by rorom on 20.10.2018.
 */

public class ReportListFragment extends RecyclerFragment<ReportsAdapter> {

    private int btUDate = 0;
    private int pid = 0;
    private int uid = 0;
    private String status = "100";
    private int version = 0;
    private boolean bookmark = false;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Actions.ACTION_PDB_UPDATED)) {
                if(getAdapter() != null) {
                    getAdapter().notifyDataSetChanged();
                }
            }
        }
    };
    private AttachmentHolder toolbarViewHolder;

    public static ReportListFragment newInstance() {
        ReportListFragment fr = new ReportListFragment();
        Bundle args = new Bundle();
        fr.setArguments(args);
        return fr;
    }

    public static ReportListFragment newInstance(boolean bookmark) {
        ReportListFragment fr = new ReportListFragment();
        Bundle args = new Bundle();
        args.putBoolean("bookmark", bookmark);
        fr.setArguments(args);
        return fr;
    }

    public static ReportListFragment newInstance(int uid, int pid, String status, int version) {
        ReportListFragment fr = new ReportListFragment();
        Bundle args = new Bundle();
        args.putInt("mid", uid);
        args.putInt("pid", pid);
        if(status.length() > 0) args.putString("status", status);
        if(version >= 0) args.putInt("version", version);
        fr.setArguments(args);
        return fr;
    }

    public ReportListFragment() {
        this.mAdapter = new ReportsAdapter();
        this.canLoadMore = true;
        this.title = BTApp.String(R.string.search);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitleNeeded = !this.top;
        this.title = BTApp.String(R.string.all_reports);
        this.uid = getArguments().getInt("mid");
        this.pid = getArguments().getInt("pid");
        this.status = getArguments().getString("status", "100");
        this.version = getArguments().getInt("version");
        this.bookmark = getArguments().getBoolean("bookmark", false);
        if(pid > 0) {
            this.title = "";
            getAdapter().setShowProduct(false);
            ProductList.Product product = ProductsData.getProduct(pid);
            toolbarViewHolder = new AttachmentHolder(R.layout.appkit_toolbar_view, getActivity().getLayoutInflater());
            toolbarViewHolder.title.setText(product.title);
            toolbarViewHolder.subtitle.setText(R.string.title_dashboard);
            parent.getToolbar().addView(toolbarViewHolder.itemView);
            Picasso.with(getActivity())
                    .load(product.photo)
                    .placeholder(BTApp.Drawable(R.drawable.placeholder_users))
                    .transform(new CropCircleTransformation())
                    .into(toolbarViewHolder.icon);
        } else if(this.uid > 0) {
            this.title = BTApp.String(R.string.title_dashboard);
        } else if(this.bookmark) {
            this.title = BTApp.String(R.string.my_bookmarks);
        }
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = super.OnCreateContentView(inflater, container, savedInstanceState);
        DividerItemDecoration decoration = new DividerItemDecoration(new ColorDrawable(637534208));
        decoration.setProvider(getAdapter());
        decoration.setPaddingLeft(BTApp.dp(64));
        this.mList.addItemDecoration(decoration);
        return root;
    }

    @Override
    public WebRequest getRequest() {
        return new GetReportList(this, btUDate, this.bookmark, body -> {
            if(this.btUDate == 0) getAdapter().setData(new ReportList());
            if(this.pid > 0) for(ReportList.ReportItem report : body.reports) report.product_id = this.pid;
            if(body.reports.size() == 0) return body;
            if(body.reports.size() < 49){
                this.canLoadMore = false;
                this.mAdapter.isLoadingAdapter = false;
            } else {
                this.btUDate = Integer.parseInt(body.btUDate.get(body.btUDate.size() - 1));
            }
            getUsers(body);
            return body;
        }).setUid(this.uid).setStatus(this.status).setProduct(this.pid).setVersion(this.version);
    }

    private void getUsers(ReportList data) {
        StringBuilder ids = new StringBuilder();
        for(int i = 0; i < data.reports.size(); i++) {
            ids.append(data.reports.get(i).uid).append(", ");
        }
        new GetUserInfo(ids.toString(), new Callback<UserInfo>() {
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
                getAdapter().notifyDataSetChanged();
            }
            @Override public void onFailure(Call<UserInfo> call, Throwable t) {
                showError(t.getLocalizedMessage());
            }
        }).execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Actions.ACTION_PDB_UPDATED);
        getActivity().registerReceiver(this.receiver, filter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            getActivity().unregisterReceiver(this.receiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        if(toolbarViewHolder != null) parent.getToolbar().removeView(toolbarViewHolder.itemView);
    }

    @Override
    public void onRefresh() {
        this.btUDate = 0;
        this.canLoadMore = true;
        this.mAdapter.isLoadingAdapter = true;
        super.onRefresh();
    }
}
