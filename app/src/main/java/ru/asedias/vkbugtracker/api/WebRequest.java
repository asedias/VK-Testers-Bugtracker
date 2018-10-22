package ru.asedias.vkbugtracker.api;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        this.call.enqueue(callback);
    }

    public void cancel() {
        this.canceled = true;
        if(this.call != null) call.cancel();
    }
}
