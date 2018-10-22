package ru.asedias.vkbugtracker.api.webmethods;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import static ru.asedias.vkbugtracker.api.API.uid;

/**
 * Created by rorom on 17.10.2018.
 */

public class GetReportList extends WebRequest<ReportList> {

    public String mid;
    public String udate;


    public GetReportList(String mid, String udate, boolean bookmarks, Callback<ReportList> callback) {
        super(callback, false);
        this.mid = mid;
        this.udate = udate;
        if(bookmarks) this.params.put("act", "bookmarks");
        this.params.put("load", "0");
        this.params.put("max_udate", udate);
        this.params.put("mid", mid);
        if(Integer.valueOf(mid) > 0) {
            this.params.put("status", "100");
        }
        this.params.put("al", "0");
        this.params.put("al_id", uid);
        this.call = API.WebApi.GetReports(params);
    }
}
