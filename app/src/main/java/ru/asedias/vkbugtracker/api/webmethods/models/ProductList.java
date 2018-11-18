package ru.asedias.vkbugtracker.api.webmethods.models;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;

/**
 * Created by rorom on 21.10.2018.
 */

public class ProductList {

    @Selector(".bt_product_row") public List<Product> products = new ArrayList<>();

    public static class Product {
        @Selector(".bt_product_row_title") public String title;
        @Selector(".bt_product_row_subtitle") public List<String> subtitles;
        @Selector(value = ".bt_prod_one_photo__img", attr = "src") public String photo;
        @Selector(value = ".bt_prod_link", attr = "href", regex = "\\/bugtracker\\?act=product&id=([0-9]*)") public int id;

    }
}
