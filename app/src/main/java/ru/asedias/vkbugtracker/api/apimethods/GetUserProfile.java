package ru.asedias.vkbugtracker.api.apimethods;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.SimpleCallback;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.apimethods.models.UserProfile;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;

/**
 * Created by Roma on 10.05.2019.
 */

public class GetUserProfile extends WebRequest<UserProfile> {
    public GetUserProfile(int uid, Callback<UserProfile> callback) {
        super(callback, true);
        this.params.put("user_id", String.valueOf(uid));
        this.call = API.VKApi.GetUserProfile(params);
    }

    public GetUserProfile(LoaderFragment fragment, int uid, SimpleCallback<UserProfile> simpleCallback) {
        super(fragment, simpleCallback, true);
        this.params.put("user_id", String.valueOf(uid));
        this.call = API.VKApi.GetUserProfile(params);
    }
}
