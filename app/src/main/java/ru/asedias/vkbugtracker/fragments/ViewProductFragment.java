package ru.asedias.vkbugtracker.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.GetProductInfo;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.adapters.ViewProductAdapter;
import ru.asedias.vkbugtracker.ui.adapters.ViewReportAdapter;

/**
 * Created by rorom on 15.12.2018.
 */

public class ViewProductFragment extends RecyclerFragment<ViewProductAdapter> {

    private int pid = 3;

    public ViewProductFragment() {
        this.mAdapter = new ViewProductAdapter();
        this.title = BugTrackerApp.String(R.string.addr_product);
    }

    public static ViewProductFragment newInstance(int pid) {
        ViewProductFragment fr = new ViewProductFragment();
        Bundle args = new Bundle();
        args.putInt("pid", pid);
        fr.setArguments(args);
        return fr;
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = super.OnCreateContentView(inflater, container, savedInstanceState);
        DividerItemDecoration decoration = new DividerItemDecoration(new ColorDrawable(637534208));
        decoration.setProvider(getAdapter());
        this.mList.addItemDecoration(decoration);
        return root;
    }

    @Override
    public WebRequest getRequest() {
        if(getArguments() != null) {
            this.pid = getArguments().getInt("pid");
        }
        return new GetProductInfo(this, this.pid, data -> {
            data.product = ProductsData.getProduct(this.pid);
            for(int i = 0; i < data.counters.size(); i++) {
                data.counters.get(i).product = this.pid;
                if(data.counters.get(i).link.contains("reporters")) {
                    data.counters.get(i).toReports = false;
                    continue;
                }
                String link = data.counters.get(i).link;
                if(link.length() > 0) {
                    data.counters.get(i).status = link.substring(link.indexOf("status=") + 7);
                }
            }
            getAdapter().setData(data);
            return data;
        });
    }
}
