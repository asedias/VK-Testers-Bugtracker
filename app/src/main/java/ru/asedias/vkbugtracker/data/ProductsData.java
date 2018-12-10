package ru.asedias.vkbugtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.Actions;
import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.api.webmethods.GetProducts;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;

/**
 * Created by rorom on 27.11.2018.
 */

public class ProductsData {

    private static ProductsHelper helper = new ProductsHelper();
    private static final int DB_VERSION = 1;
    private static final String BD_TABLENAME = "products";
    private static final String LOG_TAG = "ProductsDB";
    private static ProductList data = new ProductList();

    public static void updateProducts() {
        new GetProducts(true, new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                data = response.body();
                clearCacheData();
                insertData(data.products);
                BugTrackerApp.context.sendBroadcast(new Intent(Actions.ACTION_PDB_UPDATED));
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                invalidateCache();
                Log.e(LOG_TAG, "UPDATE FAILED: " + t.getLocalizedMessage());
                t.printStackTrace();
            }
        }).execute();
    }

    public static void clearCacheData() {
        helper.getWritableDatabase().delete("products", null, null);
    }

    public static ProductList getCacheData() {
        return data;
    }

    public static ProductList.Product getProductByName(String name) {
        for (ProductList.Product product : data.products) {
            if (product.title.hashCode() == name.hashCode()) {
                return product;
            }
        }
        return new ProductList.Product();
    }

    public static ProductList.Product getProduct(int id) {
        for (ProductList.Product product : data.products) {
            if (product.id == id) {
                return product;
            }
        }
        return new ProductList.Product();
    }

    public static void insertData(List<ProductList.Product> products) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i = 0; i < products.size(); i++) {
            ProductList.Product product = products.get(i);
            values.put("pid", product.id);
            values.put("title", product.title);
            values.put("photo", product.photo);
            StringBuilder sb = new StringBuilder();
            for (String s : product.subtitles)
            {
                sb.append(s);
                sb.append(";");
            }
            values.put("subtitles", sb.toString());
            db.insert(BD_TABLENAME, null, values);
        }
    }

    public static void invalidateCache() {
        Cursor c = helper.getReadableDatabase().query(BD_TABLENAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int pid = c.getColumnIndex("pid");
            int title = c.getColumnIndex("title");
            int photo = c.getColumnIndex("photo");
            int subtitles = c.getColumnIndex("subtitles");
            do {
                ProductList.Product product = new ProductList.Product();
                product.id = c.getInt(pid);
                product.title = c.getString(title);
                product.photo = c.getString(photo);
                product.subtitles = Arrays.asList(c.getString(subtitles).split(";"));
                data.products.add(product);
            } while (c.moveToNext());
        }
        c.close();
    }

    public static class ProductsHelper extends SQLiteOpenHelper {
        public ProductsHelper() {
            super(BugTrackerApp.context, BD_TABLENAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table products ("
                    + "id integer primary key autoincrement,"
                    + "pid integer,"
                    + "title text,"
                    + "photo text,"
                    + "subtitles text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
