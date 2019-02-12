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
import ru.asedias.vkbugtracker.api.webmethods.GetUpdates;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.adapters.UpdatesAdapter;

/**
 * Created by rorom on 18.11.2018.
 */

public class UpdatesListFragment extends RecyclerFragment<UpdatesAdapter> {

    private boolean liked;

    public UpdatesListFragment() {
        this.mAdapter = new UpdatesAdapter();
        this.title = BTApp.String(R.string.prefs_updates);
        this.setTitleNeeded = true;
    }

    public static UpdatesListFragment newInstance(boolean liked) {
        UpdatesListFragment fragment = new UpdatesListFragment();
        Bundle args = new Bundle();
        args.putBoolean("liked", liked);
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
            this.liked = getArguments().getBoolean("all", false);
        }
        return new GetUpdates(this, this.liked, data -> data);
    }

}
