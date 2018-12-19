package ru.asedias.vkbugtracker.api.webmethods.models;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;

/**
 * Created by rorom on 21.10.2018.
 */

public class ProductList implements ListModel<ProductList.Product> {

    @Selector(".bt_product_row") public List<Product> products = new ArrayList<>();

    @Override
    public int getSize() {
        return products.size();
    }

    @Override
    public Product get(int position) {
        return products.get(position);
    }

    public static class Product {
        @Selector(value = ".bt_product_row_title", defValue = "") public String title;
        @Selector(value = ".bt_product_row_subtitle", defValue = "") public List<String> subtitles;
        @Selector(value = "img", attr = "src", defValue = "https://vkontakte.ru/images/camera_200.png") public String photo;
        @Selector(value = ".bt_prod_link", attr = "href", regex = "\\/bugtracker\\?act=product&id=([0-9]*)", defValue = "0") public int id;
        public boolean my;

        public String toString() {
            return title + ", " + my;
        }
    }
}
