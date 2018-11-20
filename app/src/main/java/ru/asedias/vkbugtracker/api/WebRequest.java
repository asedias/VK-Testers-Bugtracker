package ru.asedias.vkbugtracker.api;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.api.webmethods.models.ListModel;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;
import ru.asedias.vkbugtracker.fragments.RecyclerFragment;
import ru.asedias.vkbugtracker.ui.adapters.DataAdapter;

import static ru.asedias.vkbugtracker.api.API.Cookie;
import static ru.asedias.vkbugtracker.api.API.uid;

/**
 * Created by rorom on 17.10.2018.
 */

public class WebRequest<I> {

    protected Callback<I> callback;
    protected Map<String, String> params = new HashMap<>();
    protected boolean canceled;
    protected Call<I> call;

    public WebRequest(LoaderFragment fragment, SimpleCallback<I> simpleCallback, boolean isAPI) {
        this(new Callback<I>() {
            @Override public void onResponse(Call<I> call, Response<I> response) {
                try {
                    fragment.isLoadingMore = false;
                    I data = simpleCallback.onSuccess(response.body());
                    if(data instanceof ListModel) {
                        if (fragment instanceof RecyclerFragment) {
                            if (((RecyclerFragment) fragment).getAdapter() instanceof DataAdapter) {
                                DataAdapter adapter = ((DataAdapter) ((RecyclerFragment) fragment).getAdapter());
                                if(adapter.getItemCount() == 0 || fragment.isRefreshing() || !fragment.canLoadMode()) {
                                    adapter.setData((ListModel) data);
                                } else {
                                    adapter.addData((ListModel) data);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    fragment.showError(e.fillInStackTrace());
                    return;
                }
                fragment.showContent();
            }
            @Override public void onFailure(Call<I> call, Throwable t) {
                fragment.showError(t);
            }
        }, isAPI);
    }

    protected void generateParams() { }

    public WebRequest(Callback<I> callback, boolean isAPI) {
        this.callback = callback;
        if(isAPI) {
            this.params.put("access_token", API.access_token);
            this.params.put("v", "5.85");
        } else {
            this.params.put("al", "0");
            this.params.put("al_id", uid);
        }
    }

    public void execute() {
        generateParams();
        this.call.enqueue(callback);
    }

    public void cancel() {
        this.canceled = true;
        if(this.call != null) call.cancel();
    }
}
