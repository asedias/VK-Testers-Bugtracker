package ru.asedias.vkbugtracker.api.webmethods;

import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.SimpleCallback;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.MembersList;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;

/**
 * Created by Roma on 10.05.2019.
 */

public class GetMembers extends WebRequest<MembersList> {

    public GetMembers(LoaderFragment fragment, SimpleCallback<MembersList> simpleCallback) {
        super(fragment, simpleCallback, false);
    }

    @Override
    public void generateParams() {
        super.generateParams();
        this.params.put("act", "reporters");
        this.call = API.WebApi.GetMembers(params);
    }
}
