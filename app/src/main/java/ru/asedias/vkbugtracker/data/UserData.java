package ru.asedias.vkbugtracker.data;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ru.asedias.vkbugtracker.Actions;
import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;

/**
 * Created by rorom on 20.10.2018.
 */


public class UserData {

    private static int uid;
    private static String accessToken;
    private static String name;
    private static String photo;
    public static boolean debugEnabled = false;
    private static SharedPreferences user_prefs = BTApp.context.getSharedPreferences("user", 0);
    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BTApp.context);

    public UserData() {
        accessToken = user_prefs.getString("access_token", "");
        uid = Integer.parseInt(user_prefs.getString("user_id", "0"));
        name = user_prefs.getString("user_name", "");
        photo = user_prefs.getString("user_photo", "https://vk.com/images/camera_200.png");
        debugEnabled = preferences.getBoolean("debug", false);
    }

    public static void updateUserData(UserInfo.User user) {
        name = String.format("%s %s", user.getFirstName(), user.getLastName());
        photo = user.getPhoto200();
        SharedPreferences.Editor editor = BTApp.context.getSharedPreferences("user", 0).edit();
        editor.putString("user_photo", user.getPhoto200());
        editor.putString("user_name", name);
        editor.apply();
        BTApp.context.sendBroadcast(new Intent(Actions.ACTION_USER_UPDATED));
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getUID() {
        return String.valueOf(uid);
    }

    public static String getName() {
        return name;
    }

    public static String getPhoto() {
        return photo;
    }

    public static void setName(String name) {
        UserData.name = name;
    }

    public static void setPhoto(String photo) {
        UserData.photo = photo;
    }
}