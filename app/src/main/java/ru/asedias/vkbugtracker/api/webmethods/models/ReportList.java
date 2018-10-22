package ru.asedias.vkbugtracker.api.webmethods.models;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;

/**
 * Created by rorom on 17.10.2018.
 */

public class ReportList {
    @Selector(".bt_reports_found") public String reports_found;
    @Selector(".bt_report_row") public List<ReportItem> reports = new ArrayList<>();

    public static class ReportItem {
        @Selector(".bt_report_title_link") public String title;
        @Selector(value = ".bt_report_title_link", attr = "href", regex = "\\/bugtracker\\?act=show&id=([0-9]*)") public int id;
        @Selector(".bt_tag_label") public List<ReportTag> tags;
        @Selector(value = ".bt_tag_label", attr = "onclick", regex = "BugTracker\\.addSearchFilter\\('product', ([0-9]*)") public int product_id;
        @Selector(".bt_report_info__value") public String status;
        @Selector(".bt_report_info_details") public String details;
        @Selector(value = ".bt_report_info_details > a", attr = "href", regex = "\\/bugtracker\\?act=reporter&id=([0-9]*)") public int uid;
        public UserInfo.User user;
        public ProductList.Product product;

        public static class ReportTag {
            @Selector(".bt_tag_label") public String label;
            @Selector(value = ".bt_tag_label", attr = "onclick") public String onclick;
        }
    }
}
