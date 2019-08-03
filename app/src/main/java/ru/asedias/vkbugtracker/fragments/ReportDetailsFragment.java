package ru.asedias.vkbugtracker.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.DialogRequest;
import ru.asedias.vkbugtracker.api.SimpleCallback;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.adapters.DetailsAdapter;

/**
 * Created by rorom on 27.11.2018.
 */

public class ReportDetailsFragment extends CardRecyclerFragment<DetailsAdapter> {

    private List<Report.Detail> details;

    public ReportDetailsFragment() {
        this.mAdapter = new DetailsAdapter();
        this.title = BTApp.String(R.string.report_information);
        this.setTitleNeeded = true;
    }

    public ReportDetailsFragment setDetails(List<Report.Detail> details) {
        this.details = details;
        return this;
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = super.OnCreateContentView(inflater, container, savedInstanceState);
        DividerItemDecoration decoration = new DividerItemDecoration(new ColorDrawable(637534208));
        decoration.setProvider(getAdapter());
        this.mList.addItemDecoration(decoration);
        /*new DialogRequest(parent)
                .setRequest(new GetUserInfo(86185582, null))
                .setCallback(data -> {
                    Toast.makeText(parent, ((UserInfo)data).get(0).getFirstName(), Toast.LENGTH_LONG).show();
                    return data;
                })
                .show();*/
        return root;
    }


    @Override
    public WebRequest getRequest() {
        this.getAdapter().setData(this.details);
        this.showContent();
        this.mSwipeRefresh.setEnabled(false);
        return null;
    }
}
