package ru.asedias.vkbugtracker.api.webmethods.models;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;
import pl.droidsonroids.jspoon.annotation.SkipOn;


public class MembersList implements ListModel<MembersList.Member> {
    @Selector(".bt_reporter_row") public List<Member> members = new ArrayList<>();

    @Override
    public int getSize() {
        return members.size();
    }

    @Override
    public Member get(int position) {
        return members.get(position);
    }

    public static class Member {
        @SkipOn(NumberFormatException.class)
        @Selector(value = ".bt_reporter_row_pos", defValue = "0") public int pos;
        @Selector(value = ".bt_reporter_row_img", attr = "src") public String photo_url;
        @Selector(value = ".bt_reporter_row_name", defValue = "DELETED DELETED") public String full_name;
        @Selector(value = ".bt_reporter_row_name", attr = "href", defValue = "0", regex = ".+id=([0-9]*)") public int uid;
        @Selector(value = ".bt_reporter_row_count") public List<Integer> counters;
    }
}
