package ru.asedias.vkbugtracker.api.apimethods;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;

/**
 * Created by rorom on 20.10.2018.
 */

public class GetUserInfo extends WebRequest<UserInfo> {

    public GetUserInfo(int uid, Callback<UserInfo> callback) {
        super(callback, true);
        this.params.put("fields", "photo_50,photo_100,photo_200");
        this.params.put("user_ids", String.valueOf(uid));
        this.call = API.VKApi.GetUserInfo(params);
    }

    public GetUserInfo(String uids, Callback<UserInfo> callback) {
        super(callback, true);
        this.params.put("fields", "photo_50,photo_100,photo_200");
        this.params.put("user_ids", uids);
        this.call = API.VKApi.GetUserInfo(params);
    }
}
