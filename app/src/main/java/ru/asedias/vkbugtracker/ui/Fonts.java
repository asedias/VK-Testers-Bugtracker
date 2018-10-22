package ru.asedias.vkbugtracker.ui;

import android.graphics.Typeface;
import static ru.asedias.vkbugtracker.BugTrackerApp.context;

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
        Bold = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-Bold.ttf");
        BoldItalic = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-BoldItalic.ttf");
        Italic = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-Italic.ttf");
        Medium = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-Medium.ttf");
        MediumItalic = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-MediumItalic.ttf");
        Regular = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-Regular.ttf");
    }
}
