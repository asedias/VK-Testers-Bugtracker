package ru.asedias.vkbugtracker.api.webmethods;

import retrofit2.Callback;
import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.SimpleCallback;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;

/**
 * Created by rorom on 09.11.2018.
 */

public class GetReportInfo extends WebRequest<Report> {

    int id;

    public GetReportInfo(LoaderFragment fragment,int id, SimpleCallback<Report> simpleCallback) {
        super(fragment, simpleCallback, false);
        this.id = id;
    }

    public GetReportInfo(int id, Callback<Report> callback) { //act=show&id=3562
        super(callback, false);
        this.id = id;
    }

    @Override
    protected void generateParams() {
        this.params.put("act", "show");
        this.params.put("id", String.valueOf(id));
        this.call = API.WebApi.GetReportInfo(this.params);
    }
}
