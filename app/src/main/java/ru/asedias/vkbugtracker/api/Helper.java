package ru.asedias.vkbugtracker.api;

import android.util.Log;

import java.util.Locale;

import ru.asedias.vkbugtracker.api.apimethods.models.Error;
import ru.asedias.vkbugtracker.api.apimethods.models.VKAPIResponse;

/**
 * Created by rorom on 20.10.2018.
 */

public class Helper {

    public static void LogAPIError(VKAPIResponse data) {
        Error error = data.getError();
        StringBuilder errorInfo;
        if(error != null) {
            errorInfo = new StringBuilder(String.format(Locale.getDefault(), "Ошибка %d: %s", error.getErrorCode(), error.getErrorMsg()));
            for (int i = 0; i < error.getRequestParams().size(); i++) {
                Error.RequestParam param = error.getRequestParams().get(i);
                errorInfo.append(String.format("\n%s:%s", param.getKey(), param.getValue()));
            }
        } else {
            errorInfo = new StringBuilder("Неизвестная ошибка");
        }
        Log.e("error", errorInfo.toString());
    }
}
