package ru.asedias.vkbugtracker.api.webmethods;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.WebCardUser;

/**
 * Created by Roma on 11.05.2019.
 */

public class GetUserCard extends WebRequest<WebCardUser> {

    int uid;
    public GetUserCard(int uid, Callback<WebCardUser> callback) {
        super(callback, false);
        this.uid = uid;
    }

    @Override
    public void generateParams() {
        this.params.put("act", "reporter");
        this.params.put("id", String.valueOf(uid));
        this.call = API.WebApi.GetUserCard(this.params);
    }
}
