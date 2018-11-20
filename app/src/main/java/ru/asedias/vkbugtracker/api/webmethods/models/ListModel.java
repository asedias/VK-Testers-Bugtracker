package ru.asedias.vkbugtracker.api.webmethods.models;

/**
 * Created by rorom on 20.11.2018.
 */

public interface ListModel<I> {
    public int getSize();
    public I get(int position);
}
