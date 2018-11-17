package ru.asedias.vkbugtracker.api.webmethods;

import java.util.List;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;

/**
 * Created by rorom on 21.10.2018.
 */

public class GetProducts extends WebRequest<ProductList> {

    public GetProducts(boolean all, Callback<ProductList> callback) {
        super(callback, false);
        this.params.put("act", "products");
        if(all) this.params.put("section", "all");
        this.call = API.WebApi.GetProducts(params);
    }
}
