package ru.asedias.vkbugtracker.api.webmethods;

import java.util.List;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.SimpleCallback;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;

/**
 * Created by rorom on 21.10.2018.
 */

public class GetProducts extends WebRequest<ProductList> {

    boolean all;

    public GetProducts(LoaderFragment fragment, boolean all, SimpleCallback<ProductList> simpleCallback) {
        super(fragment, simpleCallback, false);
        this.all = all;
    }

    public GetProducts(boolean all, Callback<ProductList> callback) {
        super(callback, false);
        this.all = all;
    }

    @Override
    protected void generateParams() {
        this.params.put("act", "products");
        if(all) this.params.put("section", "all");
        this.call = API.WebApi.GetProducts(params);
    }
}
