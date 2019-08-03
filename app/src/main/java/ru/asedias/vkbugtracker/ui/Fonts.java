package ru.asedias.vkbugtracker.ui;

import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.res.ResourcesCompat;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;

import static ru.asedias.vkbugtracker.BTApp.context;

/**
 * Created by rorom on 13.05.2018.
 */

public class Fonts {

    public static Typeface Bold;
    public static Typeface BoldItalic;
    public static Typeface Italic;
    public static Typeface Medium;
    public static Typeface MediumItalic;
    public static Typeface Regular;

    public Fonts() {
        Bold = ResourcesCompat.getFont(BTApp.context, R.font.googlesans_bold);
        BoldItalic = ResourcesCompat.getFont(BTApp.context, R.font.googlesans_bolditalic);
        Italic = ResourcesCompat.getFont(BTApp.context, R.font.googlesans_italic);
        Medium = ResourcesCompat.getFont(context, R.font.googlesans_medium);
        MediumItalic = ResourcesCompat.getFont(BTApp.context, R.font.googlesans_mediumitalic);
        Regular = ResourcesCompat.getFont(BTApp.context, R.font.googlesans_regular);
    }
}
