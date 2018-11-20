package ru.asedias.vkbugtracker;

import android.content.SharedPreferences;

import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;

/**
 * Created by rorom on 20.10.2018.
 */


public class UserData {

    private static int uid;
    private static String accessToken;
    private static String name;
    private static String photo;
    private static SharedPreferences preferences = BugTrackerApp.context.getSharedPreferences("user", 0);

    public UserData() {
        accessToken = preferences.getString("access_token", "");
        uid = Integer.parseInt(preferences.getString("user_id", "0"));
        name = preferences.getString("user_name", "");
        photo = preferences.getString("user_photo", "https://vk.com/images/camera_200.png");
    }

    public static void updateUserData(UserInfo.User user) {
        name = String.format("%s %s", user.getFirstName(), user.getLastName());
        photo = user.getPhoto200();
        SharedPreferences.Editor editor = BugTrackerApp.context.getSharedPreferences("user", 0).edit();
        editor.putString("user_photo", user.getPhoto200());
        editor.putString("user_name", name);
        editor.apply();
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