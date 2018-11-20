package ru.asedias.vkbugtracker.ui.adapters;

import android.app.Activity;
import android.app.Notification;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.NotificationList;
import ru.asedias.vkbugtracker.api.webmethods.models.UpdateList;
import ru.asedias.vkbugtracker.ui.holders.CommentHolder;
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
