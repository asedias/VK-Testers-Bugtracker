package ru.asedias.vkbugtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
    private static final int DB_VERSION = 2;
    private static final String BD_TABLENAME = "products";
    private static final String LOG_TAG = "ProductsDB";
    private static ProductList data = new ProductList();

    public static void updateProducts(boolean all) {
        new GetProducts(all, new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
            if(!all) clearCacheData();
            ProductList list = response.body();
            for(ProductList.Product product : list.products) {
                product.my = !all;
            }
            data.products.addAll(list.products);
            insertData(data.products, !all);
                if (!all) {
                    updateProducts(true);
                } else {
                    BugTrackerApp.context.sendBroadcast(new Intent(Actions.ACTION_PDB_UPDATED));
                }
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
        data.products.clear();
        helper.getWritableDatabase().delete("products", null, null);
    }

    public static ProductList getProducts(boolean my) {
        ProductList products = new ProductList();
        for(ProductList.Product product: data.products) {
            if(product.my == my) {
                products.products.add(product);
            }
        }
        Log.d(LOG_TAG, "RETURN SIZE " + my + ": " + products.getSize());
        return products;
    }

    public static ProductList getCacheData() {
        return data;
    }

    public static ProductList.Product getProductByName(String name) {
        for (ProductList.Product product : data.products) {
            if (product.title.split(" / ")[0].hashCode() == name.hashCode()) {
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

    public static void insertData(List<ProductList.Product> products, boolean my) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i = 0; i < products.size(); i++) {
            ProductList.Product product = products.get(i);
            values.put("pid", product.id);
            values.put("my", my ? 1 : 0);
            values.put("title", product.title);
            values.put("photo", product.photo);
            StringBuilder sb = new StringBuilder();
            for (String s : product.subtitles)
            {
                sb.append(s);
                sb.append(";");
            }
            values.put("subtitles", sb.toString());
            if(db.insert(BD_TABLENAME, null, values) == -1) {
                helper.onUpgrade(db, db.getVersion(), DB_VERSION);
                i--;
            }
        }
    }

    public static void invalidateCache() {
        Cursor c = helper.getReadableDatabase().query(BD_TABLENAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int pid = c.getColumnIndex("pid");
            int my = c.getColumnIndex("my");
            int title = c.getColumnIndex("title");
            int photo = c.getColumnIndex("photo");
            int subtitles = c.getColumnIndex("subtitles");
            do {
                ProductList.Product product = new ProductList.Product();
                product.id = c.getInt(pid);
                product.my = c.getInt(my) == 1;
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
                    + "my integer,"
                    + "title text,"
                    + "photo text,"
                    + "subtitles text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + BD_TABLENAME);
            Log.d(LOG_TAG, "onUpgrade");
            onCreate(db);
        }
    }
}
