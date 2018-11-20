package ru.asedias.vkbugtracker.api.webmethods;

import java.util.Map;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.SimpleCallback;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.UpdateList;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;

/**
 * Created by rorom on 18.11.2018.
 */

public class GetUpdates extends WebRequest<UpdateList> {

    private boolean liked;

    public GetUpdates(LoaderFragment fragment, boolean liked, SimpleCallback<UpdateList> simpleCallback) {
        super(fragment, simpleCallback, false);
        this.liked = liked;
    }

    public GetUpdates(boolean liked, Callback<UpdateList> callback) {
        super(callback, false);
        this.liked = liked;
    }

    @Override
    protected void generateParams() {
        this.params.put("act", "updates");
        if(liked) this.params.put("bookmarked", String.valueOf(1));
        this.call = API.WebApi.GetUpdates(this.params);
    }
}
