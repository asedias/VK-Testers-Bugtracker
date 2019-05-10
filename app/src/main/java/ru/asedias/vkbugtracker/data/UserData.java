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

    public static int uid;
    public static String accessToken;
    public static String name;
    public static String photo;
    public static boolean debugEnabled = false;
    private static SharedPreferences user_prefs = BTApp.context.getSharedPreferences("user", 0);
    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BTApp.context);
    public static boolean isTester = true;
    public static String cookie;

    public UserData() {
        accessToken = user_prefs.getString("access_token", "");
        uid = Integer.parseInt(user_prefs.getString("user_id", "0"));
        name = user_prefs.getString("user_name", "DELETED DELETED");
        photo = user_prefs.getString("user_photo", "https://vk.com/images/camera_200.png");
        debugEnabled = preferences.getBoolean("debug", false);
        isTester = user_prefs.getInt("tester", 0) == 1;
        cookie = user_prefs.getString("cookies", "");
    }

    public static void updateUserData(UserInfo.User user) {
        name = String.format("%s %s", user.getFirstName(), user.getLastName());
        photo = user.getPhoto200();
        isTester = 1 == user.getTesterNum();
        SharedPreferences.Editor editor = BTApp.context.getSharedPreferences("user", 0).edit();
        editor.putString("user_photo", user.getPhoto200());
        editor.putString("user_name", name);
        editor.putInt("tester", isTester ? 1 : 0);
        editor.apply();
        BTApp.context.sendBroadcast(new Intent(Actions.ACTION_USER_UPDATED));
    }

    public static void clear() {
        BTApp.context.getSharedPreferences("user", 0).edit().clear().apply();
        accessToken = "";
        uid = 0;
        name = "DELETED DELETED";
        photo = "https://vk.com/images/camera_200.png";
        debugEnabled = false;
        isTester = true;
        cookie = "";
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static int getUID() {
        return uid;
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

    public static boolean isTester() {
        return isTester;
    }

    public static String getCookie() {
        return cookie;
    }
}