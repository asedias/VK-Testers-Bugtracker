package ru.asedias.vkbugtracker.api.webmethods;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.SimpleCallback;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;

import static ru.asedias.vkbugtracker.api.API.uid;

/**
 * Created by rorom on 17.10.2018.
 */

public class GetReportList extends WebRequest<ReportList> {

    protected int mid;
    protected int udate;
    protected boolean bookmarks;

    public GetReportList(int mid, int udate, boolean bookmarks, Callback<ReportList> callback) {
        super(callback, false);
        this.mid = mid;
        this.udate = udate;
        this.bookmarks = bookmarks;
    }

    public GetReportList(LoaderFragment fragment, int mid, int udate, boolean bookmarks, SimpleCallback<ReportList> simpleCallback) {
        super(fragment, simpleCallback, false);
        this.mid = mid;
        this.udate = udate;
        this.bookmarks = bookmarks;
    }

    @Override
    protected void generateParams() {
        if(bookmarks) this.params.put("act", "bookmarks");
        this.params.put("load", "0");
        this.params.put("max_udate", String.valueOf(udate));
        this.params.put("mid", String.valueOf(mid));
        if(mid > 0) {
            this.params.put("status", "100");
        }
        this.params.put("al", "0");
        this.params.put("al_id", uid);
        this.call = API.WebApi.GetReports(params);
    }
}
