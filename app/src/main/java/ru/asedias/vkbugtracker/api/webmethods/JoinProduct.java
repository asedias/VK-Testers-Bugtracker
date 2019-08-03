package ru.asedias.vkbugtracker.api.webmethods;

import ru.asedias.vkbugtracker.api.API;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.models.DefaultModel;

/**
 * Created by Рома on 17.05.2019.
 */

public class JoinProduct extends WebRequest<DefaultModel> {

    public final static int TYPE_JOIN = 1;
    public final static int TYPE_LEAVE = 2;
    public final static int TYPE_DELETE_LICENCE = 3;
    int type;
    int pid;
    String hash;


    public JoinProduct(int type, int pid, String hash) {
        super(null, false);
        this.type = type;
        this.pid = pid;
        this.hash = hash;
    }

    @Override
    public void generateParams() {
        if(type == TYPE_JOIN) {
            this.params.put("act", "a_join_product");
        } else if(type == TYPE_LEAVE) {
            this.params.put("act", "a_leave_product");
        } else if(type == TYPE_DELETE_LICENCE) {
            this.params.put("act", "a_delete_licence_request");
        }
        this.params.put("al", "1");
        this.params.put("id", String.valueOf(pid));
        this.params.put("hash", hash);
        this.call = API.WebApi.JoinProduct(this.params);
    }
}
