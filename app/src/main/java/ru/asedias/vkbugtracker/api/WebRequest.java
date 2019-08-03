package ru.asedias.vkbugtracker.api;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.api.webmethods.ParamsRequestInterface;
import ru.asedias.vkbugtracker.api.webmethods.models.ListModel;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;
import ru.asedias.vkbugtracker.fragments.RecyclerFragment;
import ru.asedias.vkbugtracker.ui.adapters.DataAdapter;

/**
 * Created by rorom on 17.10.2018.
 */

public class WebRequest<I> implements ParamsRequestInterface {

    protected Callback<I> callback;
    protected Map<String, String> params = new HashMap<>();
    protected boolean canceled;
    protected Call<I> call;
    protected boolean isAPI;

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
                                if(!fragment.isRefreshing() && adapter.getItemCount() > 0 ) {
                                    adapter.addData((ListModel) data);
                                } else {
                                    adapter.setData((ListModel) data);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if(fragment instanceof RecyclerFragment) {
                        ((RecyclerFragment)fragment).showError(e.fillInStackTrace());
                    } else {
                        fragment.showError(e.fillInStackTrace());
                    }
                    return;
                }
                fragment.showContent();
            }
            @Override public void onFailure(Call<I> call, Throwable t) {
                t.printStackTrace();
                if(fragment instanceof RecyclerFragment) {
                    ((RecyclerFragment)fragment).showError(t);
                } else {
                    fragment.showError(t);
                }
            }
        }, isAPI);
    }

    public void generateParams() { }

    public WebRequest(Callback<I> callback, boolean isAPI) {
        this.callback = callback;
        this.isAPI = isAPI;
    }


    public void execute() {
        if(isAPI) {
            this.params.put("access_token", UserData.getAccessToken());
            this.params.put("v", "5.85");
        } else {
            if(!this.params.containsKey("al")) this.params.put("al", "0");
            this.params.put("al_id", String.valueOf(UserData.getUID()));
        }
        generateParams();
        this.call.enqueue(callback);
    }

    public void cancel() {
        this.canceled = true;
        if(this.call != null) call.cancel();
    }
}
