package ru.asedias.vkbugtracker.api.apimethods.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.asedias.vkbugtracker.api.apimethods.models.Error;
import ru.asedias.vkbugtracker.api.webmethods.models.ListModel;

/**
 * Created by rorom on 20.10.2018.
 */

public class VKAPIResponse<I> implements ListModel<I> {
    @SerializedName("error")
    @Expose
    private Error error;
    public Error getError() {
        return error;
    }

    @SerializedName("response")
    @Expose
    private List<I> response = null;
    public List<I> getResponse() {
        return response;
    }

    @Override
    public int getSize() {
        return getResponse() != null ? getResponse().size() : 0;
    }

    @Override
    public I get(int position) {
        return getResponse() != null ? getResponse().get(position) : null;
    }
}
