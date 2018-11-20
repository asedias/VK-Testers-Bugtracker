package ru.asedias.vkbugtracker.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;

/**
 * Created by rorom on 20.11.2018.
 */

public class FragmentCallback<I> implements Callback<I> {

    private LoaderFragment fragment;
    private I data;

    public FragmentCallback(LoaderFragment fragment) {
        this.fragment = fragment;
    }

    public I getData() {
        return data;
    }

    @Override
    public void onResponse(Call<I> call, Response<I> response) {
        try {
            this.data = response.body();
        } catch (Exception e) {
            this.fragment.showError(e.fillInStackTrace());
            return;
        }
        this.fragment.showContent();
    }

    @Override
    public void onFailure(Call<I> call, Throwable t) {
        this.fragment.showError(t);
    }
}
