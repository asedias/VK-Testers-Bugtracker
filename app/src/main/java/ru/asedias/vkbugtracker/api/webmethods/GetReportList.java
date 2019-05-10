package ru.asedias.vkbugtracker.api.webmethods;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.SimpleCallback;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;

/**
 * Created by rorom on 17.10.2018.
 */

public class GetReportList extends WebRequest<ReportList> {

    private int mid = 0;
    private int udate;
    private boolean bookmarks;
    private int product = 0;
    private String status = "100";
    private int versions = 0;

    public GetReportList(int udate, boolean bookmarks, Callback<ReportList> callback) {
        super(callback, false);
        this.udate = udate;
        this.bookmarks = bookmarks;
    }

    public GetReportList(LoaderFragment fragment, int udate, boolean bookmarks, SimpleCallback<ReportList> simpleCallback) {
        super(fragment, simpleCallback, false);
        this.udate = udate;
        this.bookmarks = bookmarks;
    }

    @Override
    protected void generateParams() {
        if(bookmarks) this.params.put("act", "bookmarks");
        this.params.put("load", "0");
        this.params.put("max_udate", String.valueOf(udate));
        this.params.put("mid", String.valueOf(mid));
        this.params.put("status", status);
        this.params.put("product", String.valueOf(product));
        this.params.put("version", String.valueOf(versions));
        this.params.put("al", "0");
        this.params.put("al_id", String.valueOf(UserData.getUID()));
        this.call = API.WebApi.GetReports(params);
    }

    public GetReportList setUid(int mid) {
        this.mid = mid;
        return this;
    }

    public GetReportList setProduct(int product) {
        this.product = product;
        return this;
    }

    public GetReportList setStatus(int status) {
        this.status = String.valueOf(status);
        return this;
    }

    public GetReportList setStatus(String status) {
        this.status = status;
        return this;
    }

    public GetReportList setVersion(int version) {
        this.versions = version;
        return this;
    }
}
