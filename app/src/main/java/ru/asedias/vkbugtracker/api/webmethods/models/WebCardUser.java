package ru.asedias.vkbugtracker.api.webmethods.models;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;

/**
 * Created by Roma on 11.05.2019.
 */

public class WebCardUser {
    @Selector(value = ".bt_reporter_icon_img", attr = "src") public String photo_url;
    @Selector(value = ".bt_reporter_name", defValue = "DELETED DELETED") public String user_name;
    @Selector(value = ".bt_reporter_content_block", defValue = "ERROR") public String information;
    @Selector(value = "script", attr = "outerHtml") public List<String> scripts;
    @Selector(value = ".bt_reporter_product_img", attr = "src") public List<String> products_img;
    @Selector(value = ".ui_crumb_count") public int products_count;
    public List<ProfileActivity> activities = new ArrayList<>();
}
