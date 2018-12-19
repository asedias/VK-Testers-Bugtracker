package ru.asedias.vkbugtracker.ui.adapters;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.api.webmethods.models.NotificationList;
import ru.asedias.vkbugtracker.ui.holders.NotificationHolder;

/**
 * Created by rorom on 18.11.2018.
 */

public class NotificationsAdapter extends DataAdapter<NotificationHolder, NotificationList> {

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        return new NotificationHolder(inflater);
    }
}
