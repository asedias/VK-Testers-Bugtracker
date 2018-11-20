package ru.asedias.vkbugtracker.api.webmethods.models;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;

/**
 * Created by rorom on 17.10.2018.
 */

public class ReportList implements ListModel<ReportList.ReportItem> {
//
    @Selector(value = "script", attr = "outerHtml", regex = "cur.btUDate=\"([0-9]+)\";") public List<String> btUDate;
    @Selector(".bt_reports_found") public String reports_found;
    @Selector(".bt_report_row") public List<ReportItem> reports = new ArrayList<>();

    @Override
    public int getSize() {
        return reports.size();
    }

    @Override
    public ReportItem get(int position) {
        return reports.get(position);
    }

    public static class ReportItem {
        @Selector(value = ".bt_report_title_link", defValue = "") public String title;
        @Selector(value = ".bt_report_title_link", attr = "href", regex = "\\/bugtracker\\?act=show&id=([0-9]*)", defValue = "0") public int id;
        @Selector(".bt_tag_label") public List<ReportTag> tags;
        @Selector(value = ".bt_tag_label", attr = "onclick", regex = "BugTracker\\.addSearchFilter\\('product', ([0-9]*)") public int product_id;
        @Selector(".bt_report_info__value") public String status;
        @Selector(".bt_report_info_details") public String details;
        @Selector(value = ".bt_report_info_details > a", attr = "href", regex = "\\/bugtracker\\?act=reporter&id=([0-9]*)") public int uid;
        public UserInfo.User user = new UserInfo.User();
        public ProductList.Product product = new ProductList.Product();

        public static class ReportTag {
            @Selector(".bt_tag_label") public String label;
            @Selector(value = ".bt_tag_label", attr = "onclick") public String onclick;
        }
    }
}
