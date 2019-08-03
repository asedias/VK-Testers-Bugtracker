package ru.asedias.vkbugtracker.api.webmethods.models;

import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rorom on 26.05.2018.
 */

public class ProfileActivity {

    public double alpha; //relative
    public int count;    //absolute
    public long date;    //date
    public int dayOfWeek;
    public int month;

    public ProfileActivity(JSONObject object) {
        if(object.has("relative")) {
            this.alpha = object.optDouble("relative", 0);
            this.count = object.optInt("absolute", 0);
            this.date = object.optInt("date", 0);
            Calendar c = Calendar.getInstance();
            Date date = new Date();
            date.setTime(this.date*1000);
            c.setTime(date);
            this.month = c.get(Calendar.MONTH);
            this.dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        }
    }

}
