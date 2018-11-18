package ru.asedias.vkbugtracker.api.webmethods;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.UpdateList;

/**
 * Created by rorom on 18.11.2018.
 */

public class GetUpdates extends WebRequest<UpdateList> {
    public GetUpdates(boolean liked, Callback<UpdateList> callback) {
        super(callback, false);
        this.params.put("act", "updates");
        if(liked) this.params.put("bookmarked", String.valueOf(1));
        this.call = API.WebApi.GetUpdates(this.params);
    }
}
