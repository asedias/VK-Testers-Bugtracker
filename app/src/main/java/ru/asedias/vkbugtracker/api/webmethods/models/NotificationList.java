package ru.asedias.vkbugtracker.api.webmethods.models;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;

/**
 * Created by rorom on 19.11.2018.
 */

public class NotificationList implements ListModel<NotificationList.Notification> {

    @Selector(".feedback_row") public List<Notification> notifications = new ArrayList<>();

    @Override
    public int getSize() {
        return notifications.size();
    }

    @Override
    public Notification get(int position) {
        return notifications.get(position);
    }

    public static class Notification {
        @Selector(value = "img", attr="src", defValue = "https://vk.com/images/pics/notifications/feedback/archive_done_40.png") public String author_photo;
        @Selector(".feedback_header") public String author_name;
        @Selector(".feedback_text") public String text;
        @Selector(".feedback_date") public String date;
        @Selector(value = ".feedback_row", attr = "class", regex = "feedback_type_([0-9]+)", defValue = "0") public int type;
    }

}
