package ru.asedias.vkbugtracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ru.asedias.vkbugtracker.MainActivity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class RestartExceptionHandler implements UncaughtExceptionHandler {

    private Context mContext;

    public RestartExceptionHandler(Context var1) {
        this.mContext = var1;
    }

    public void uncaughtException(Thread var1, Throwable var2) {
        try {
            Intent var5 = new Intent(this.mContext, MainActivity.class);
            var5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP+Intent.FLAG_ACTIVITY_CLEAR_TASK+Intent.FLAG_RECEIVER_FOREGROUND);//335577088
            StringWriter var3 = new StringWriter();
            var2.printStackTrace(new PrintWriter(var3));
            if(var3.toString().contains("RestartException")) {
                var5.putExtra("crash", false);
            } else {
                var5.putExtra("crash", true);
            }

            var5.putExtra("crash_info", var3.toString());
            this.mContext.startActivity(var5);
            Log.i("Crash", "Restart App");
            var2.printStackTrace();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        System.exit(2);
    }
}
