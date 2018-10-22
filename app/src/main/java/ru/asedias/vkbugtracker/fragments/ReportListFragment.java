package ru.asedias.vkbugtracker.fragments;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;
import ru.asedias.vkbugtracker.api.webmethods.GetProducts;
import ru.asedias.vkbugtracker.api.webmethods.GetReportList;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.ui.adapters.ReportsAdapter;

/**
 * Created by rorom on 20.10.2018.
 */

public class ReportListFragment extends RecyclerFragment<ReportsAdapter> {
    public ReportListFragment() {
        this.mAdapter = new ReportsAdapter(null);
    }

    @Override
    public WebRequest getRequest() {
        return new GetReportList("0", "0", false, new Callback<ReportList>() {
            @Override
            public void onResponse(Call<ReportList> call, Response<ReportList> response) {
                try {
                    ReportList body = response.body();
                    if(body.reports.size() > 0) getUsers(body);
                } catch (Exception e) {
                    Log.e("RESPONSE", "Catch Exception", e.fillInStackTrace());
                }
            }
            @Override public void onFailure(Call<ReportList> call, Throwable t) { Log.e("RESPONSE", "ERROR", t); }
        });
    }

    private void getUsers(ReportList body) {
        String ids = "";
        for(int i = 0; i < body.reports.size(); i++) {
            ids+=body.reports.get(i).uid + ", ";
        }
        request = new GetUserInfo(ids, new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                List<UserInfo.User> users = response.body().getResponse();
                for(int i = 0; i < body.reports.size(); i++) {
                    for (int i2 = 0; i2 < users.size(); i2++) {
                        ReportList.ReportItem item = body.reports.get(i);
                        UserInfo.User user = users.get(i2);
                        if(item.uid == user.getId()) {
                            item.user = user;
                            break;
                        }
                    }
                }
                getProducts(body);
            }
            @Override public void onFailure(Call<UserInfo> call, Throwable t) { }
        });
        request.execute();
    }

    private void getProducts(ReportList body) {
        this.request = new GetProducts(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                ProductList data = response.body();
                for(int i = 0; i < body.reports.size(); i++) {
                    for (int i2 = 0; i2 < data.products.size(); i2++) {
                        ReportList.ReportItem item = body.reports.get(i);
                        ProductList.Product product = data.products.get(i2);
                        if(item.product_id == product.id) {
                            item.product = product;
                            break;
                        }
                    }
                }
                getAdapter().setData(body);
                showContent();
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {

            }
        });
        this.request.execute();
    }
}
