package ru.asedias.vkbugtracker.api.webmethods.models;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;
import ru.asedias.vkbugtracker.api.TextConverter;

/**
 * Created by rorom on 15.12.2018.
 */

public class ProductInfo {

    @Selector(value = ".bt_prod_one_photo__img", attr = "src", defValue = "https://vkontakte.ru/images/camera_200.png") public String photo;
    @Selector(value = ".bt_prod_one_title", defValue = "") public String title;
    @Selector(value = ".bt_prod_one_descr", defValue = "", converter = TextConverter.class) public String description;
    @Selector(".bt_prod_one_actions > .flat_button") public List<ActionButton> actionButtons = new ArrayList<>();
    @Selector(".ui_actions_menu_item") public List<ActionDropDown> actionDropDowns = new ArrayList<>();
    @Selector(".bt_prod_one_actions") public List<String> actionProds = new ArrayList<>();
    @Selector(".bt_prod_version") public List<Version> versions = new ArrayList<>();
    @Selector(".bt_reporter_product") public List<Product> products = new ArrayList<>();
    @Selector(".page_counter") public List<Counter> counters = new ArrayList<>();
    public ProductList.Product product;

    public static class Version {
        @Selector(".bt_prod_version_title__version") public String title;
        @Selector(".bt_prod_version_date") public String date;
        @Selector(value = ".bt_prod_version_release_notes", converter = TextConverter.class) public String release_notes;
        @Selector(".bt_icon_count") public String reports_count;
        @Selector(".bt_icon_solved") public String solved_count;
        @Selector(".bt_icon_vuln") public String vuln_count;
    }

    public static class Counter {
        @Selector(".count") public String title;
        @Selector(".label") public String description;
        @Selector(value = ".page_counter", attr = "href") public String link;
        public boolean toReports = true;
        public int product = 0;
        public String status = "100";
    }

    public static class Product extends ProductList.Product {
        @Selector(value = ".bt_reporter_product_title", defValue = "") public String title;
        @Selector(value = "img", attr = "src", defValue = "https://vkontakte.ru/images/camera_200.png") public String photo;
        @Selector(value = ".bt_reporter_product_a_img", attr = "href", regex = "\\/bugs\\?act=product&id=([0-9]*)", defValue = "0") public int id;
        @Selector(".bt_reporter_product_nreports") public String reports_count;
    }

    public static class ActionButton {
        @Selector(".flat_button") public String title;
        @Selector(value = ".flat_button", attr = "onclick") public String onclick;
    }

    public static class ActionDropDown extends ActionButton {
        @Selector(".ui_actions_menu_item") public String title;
        @Selector(value = ".ui_actions_menu_item", attr = "onclick") public String onclick;
    }
}
