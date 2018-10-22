package ru.asedias.vkbugtracker.api.apimethods.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("request_params")
    @Expose
    private List<RequestParam> requestParams = null;

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public List<RequestParam> getRequestParams() {
        return requestParams;
    }

    public class RequestParam {

        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("value")
        @Expose
        private String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

    }
}