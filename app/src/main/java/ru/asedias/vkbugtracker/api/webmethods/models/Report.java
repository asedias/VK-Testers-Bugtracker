package ru.asedias.vkbugtracker.api.webmethods.models;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;

/**
 * Created by rorom on 08.11.2018.
 */

public class Report {
    @Selector(".bt_report_one_author") public Author author;
    @Selector(".bt_report_one_title") public String title;
    @Selector(".bt_report_one_descr") public String description;
    @Selector(".bt_report_footer") public Footer footer;
    @Selector(".bt_report_one_attachs > .wall_module > .media_desc > .page_doc_row") public List<Attachment> attachments = new ArrayList<>();
    @Selector(".bt_report_one_attachs > .wall_module > .page_post_sized_thumbs > .page_post_thumb_wrap") public List<Photo> photos = new ArrayList<>();
    @Selector(".bt_report_one_info_row") public List<Detail> details = new ArrayList<>();
    @Selector(".bt_report_cmt") public List<Comment> comments = new ArrayList<>();

    public static class Author {
        @Selector(value = ".bt_report_one_author__img", attr="src", defValue = "https://vk.com/images/camera_200.png") public String author_photo;
        @Selector(".bt_report_one_author_content > a") public String author_name;
        @Selector(".bt_report_one_author_content > div") public String date;
    }

    public static class Attachment { //page_doc_row
        @Selector(value = ".page_doc_icon", attr="class", regex="([0-9]+)", defValue = "0") public int type;
        @Selector(".page_doc_title") public String title;
        @Selector(".page_doc_description_row") public String description;
    }

    public static class Photo { //page_post_thumb_wrap image_cover
        @Selector(value = ".page_post_thumb_wrap", attr="onclick", regex="(\\{\"temp\":\\{.+,queue:[0-9]+\\})") public String json;
        public int height;
        public int width;
        public String url_x;
        public String url_y;
        public String url_z;
        public String url_w;
    }

    public static class Detail { //bt_report_one_info_row
        @Selector(".bt_report_one_info_row_label") public String title;
        @Selector(".bt_report_one_info_row_value") public String description;
        @Selector(".bugtracker_device_marketing_name") public String device_market_name;
        @Selector(".bugtracker_device_platform") public String device_platform;
        @Selector(".bugtracker_device_model") public String device_model;
        @Selector(".bugtracker_no_device") public String no_device;
    }

    public static class Footer { //bt_report_footer
        @Selector(".reproducing > .user_img") public List<User> users;
        @Selector(".moder_view_status") public String view_status;
        @Selector(".reproducing > .common_count") public int reproduce_count;
        public static class User { //user_img visible
            @Selector(value = ".user_img", attr="src") public String photo;
            @Selector(value = ".user_img", attr="id", regex="([0-9]+)") public int uid;
        }
    }

    public static class Comment { //bt_report_cmt
        @Selector(value = ".bt_report_cmt_img", attr="src") public String author_photo;
        @Selector(".bt_report_cmt_author") public String author_name;
        @Selector(".bt_report_cmt_meta_row") public List<String> meta_content;
        @Selector(".bt_report_cmt_text") public String text;
        @Selector(".page_doc_row") List<Attachment> attachments;
        @Selector(".page_post_sized_thumbs > .page_post_thumb_wrap") public List<Photo> photos = new ArrayList<>();
        @Selector(".bt_report_cmt_date") public String date;
    }

}
