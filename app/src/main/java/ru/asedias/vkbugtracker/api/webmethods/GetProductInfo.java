package ru.asedias.vkbugtracker.api.webmethods;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.SimpleCallback;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductInfo;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;

/**
 * Created by rorom on 15.12.2018.
 */

public class GetProductInfo extends WebRequest<ProductInfo> {

    int id;

    public GetProductInfo(LoaderFragment fragment, int id, SimpleCallback<ProductInfo> simpleCallback) {
        super(fragment, simpleCallback, false);
        this.id = id;
    }

    public GetProductInfo(int id, Callback<ProductInfo> callback) {
        super(callback, false);
        this.id = id;
    }

    @Override
    public void generateParams() {
        this.params.put("act", "product");
        this.params.put("id", String.valueOf(id));
        this.call = API.WebApi.GetProductInfo(this.params);
    }
}
