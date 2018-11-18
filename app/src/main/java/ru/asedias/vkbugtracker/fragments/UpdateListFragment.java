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
import ru.asedias.vkbugtracker.api.webmethods.GetUpdates;
import ru.asedias.vkbugtracker.api.webmethods.models.UpdateList;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.adapters.UpdatesAdapter;

/**
 * Created by rorom on 18.11.2018.
 */

public class UpdateListFragment extends RecyclerFragment<UpdatesAdapter> {

    private boolean liked;

    public UpdateListFragment() {
        this.mAdapter = new UpdatesAdapter();
        this.title = BugTrackerApp.String(R.string.prefs_updates);
        this.setTitleNeeded = true;
    }

    public static UpdateListFragment newInstance(boolean liked) {
        UpdateListFragment fragment = new UpdateListFragment();
        Bundle args = new Bundle();
        args.putBoolean("liked", liked);
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
            this.liked = getArguments().getBoolean("all", false);
        }
        return new GetUpdates(this.liked, new Callback<UpdateList>() {
            @Override
            public void onResponse(Call<UpdateList> call, Response<UpdateList> response) {
                UpdateList data = response.body();
                getAdapter().setData(data);
                showContent();
            }

            @Override
            public void onFailure(Call<UpdateList> call, Throwable t) {
                showError(t.getLocalizedMessage());
            }
        });
    }

}
