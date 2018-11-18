package ru.asedias.vkbugtracker.api.webmethods.models;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;

/**
 * Created by rorom on 18.11.2018.
 */

public class UpdateList {
    @Selector(".bt_report_cmt") public List<Update> updates = new ArrayList<>();

    public static class Update extends Report.Comment {
        @Selector(".bt_report_cmt_info > a") public List<String> info;
    }
}
