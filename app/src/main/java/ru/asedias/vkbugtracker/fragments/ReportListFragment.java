package ru.asedias.vkbugtracker.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.Actions;
import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;
import ru.asedias.vkbugtracker.api.webmethods.GetProducts;
import ru.asedias.vkbugtracker.api.webmethods.GetReportList;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.adapters.ReportsAdapter;

/**
 * Created by rorom on 20.10.2018.
 */

public class ReportListFragment extends RecyclerFragment<ReportsAdapter> {

    private int btUDate = 0;
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

    public ReportListFragment() {
        this.mAdapter = new ReportsAdapter();
        this.setTitleNeeded = false;
        this.canLoadMore = true;
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = super.OnCreateContentView(inflater, container, savedInstanceState);
        DividerItemDecoration decoration = new DividerItemDecoration(new ColorDrawable(637534208));
        decoration.setProvider(getAdapter());
        decoration.setPaddingLeft(BugTrackerApp.dp(64));
        this.mList.addItemDecoration(decoration);
        return root;
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
                getAdapter().notifyDataSetChanged();
            }
            @Override public void onFailure(Call<UserInfo> call, Throwable t) {
                showError(t.getLocalizedMessage());
            }
        });
        request.execute();
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
        getActivity().unregisterReceiver(this.receiver);
    }

    @Override
    public void onRefresh() {
        this.btUDate = 0;
        getAdapter().data = new ReportList();
        super.onRefresh();
    }
}
