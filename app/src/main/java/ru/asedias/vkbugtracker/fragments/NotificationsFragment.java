package ru.asedias.vkbugtracker.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.GetNotifications;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.adapters.NotificationsAdapter;

/**
 * Created by rorom on 18.11.2018.
 */

public class NotificationsFragment extends RecyclerFragment<NotificationsAdapter> {

    public NotificationsFragment() {
        this.mAdapter = new NotificationsAdapter();
        this.title = BTApp.String(R.string.prefs_updates);
        this.setTitleNeeded = true;
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = super.OnCreateContentView(inflater, container, savedInstanceState);
        DividerItemDecoration decoration = new DividerItemDecoration(new ColorDrawable(637534208));
        decoration.setPaddingLeft(BTApp.dp(80));
        this.mList.addItemDecoration(decoration);
        return root;
    }

    @Override
    public WebRequest getRequest() {
        return new GetNotifications(this, data -> {
            for(int i = 0; i < data.getSize(); i++) {
                if(data.get(i).type != 56) {
                    data.notifications.remove(i--);
                }
            }
            return data;
        });
    }

}
