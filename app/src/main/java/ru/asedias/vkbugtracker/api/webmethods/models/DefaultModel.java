package ru.asedias.vkbugtracker.api.webmethods.models;

import pl.droidsonroids.jspoon.annotation.Selector;

/**
 * Created by Рома on 17.05.2019.
 */

public class DefaultModel {
    @Selector(value = "body", attr = "outerHtml") public String html;
}
