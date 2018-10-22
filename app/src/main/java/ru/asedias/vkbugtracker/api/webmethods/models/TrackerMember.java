package ru.asedias.vkbugtracker.api.webmethods.models;

import pl.droidsonroids.jspoon.annotation.Selector;



public class TrackerMember {
    @Selector(value = ".bt_reporter_icon_img", attr = "src") public String photo_url;
    @Selector(".bt_reporter_name > .mem_link") public String name;
    @Selector(".bt_reporter_content_block") public String subtitle;
}
