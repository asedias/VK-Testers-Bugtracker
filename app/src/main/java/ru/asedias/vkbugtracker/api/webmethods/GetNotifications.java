package ru.asedias.vkbugtracker.api.webmethods;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.SimpleCallback;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.NotificationList;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;

/**
 * Created by rorom on 19.11.2018.
 */

public class GetNotifications extends WebRequest<NotificationList> {

    public GetNotifications(LoaderFragment fragment, SimpleCallback<NotificationList> callback) {
        super(fragment, callback, false);
    }

    @Override
    public void generateParams() {
        this.params.put("section", "notifications");
        this.call = API.WebApi.GetNotification(this.params);
    }
}
