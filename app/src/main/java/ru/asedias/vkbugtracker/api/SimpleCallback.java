package ru.asedias.vkbugtracker.api;

import ru.asedias.vkbugtracker.api.webmethods.models.ListModel;

/**
 * Created by rorom on 20.11.2018.
 */
@FunctionalInterface
public interface SimpleCallback<I extends Object> {
    I onSuccess(I data);
}
