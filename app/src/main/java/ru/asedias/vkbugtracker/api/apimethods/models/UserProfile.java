package ru.asedias.vkbugtracker.api.apimethods.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.asedias.vkbugtracker.api.webmethods.models.WebCardUser;

/**
 * Created by Roma on 10.05.2019.
 */

public class UserProfile extends VKAPIResponse<UserProfile.Data> {

    public static class Data {
        @SerializedName("id")@Expose public Integer id;
        @SerializedName("deactivated")@Expose public String deactivated;
        @SerializedName("first_name")@Expose public String firstName;
        @SerializedName("last_name")@Expose public String lastName;
        @SerializedName("photo_50")@Expose public String photo_50;
        @SerializedName("photo_100")@Expose public String photo_100;
        @SerializedName("photo_200")@Expose public String photo_200;
        @SerializedName("photo_200_orig")@Expose public String photo_200_orig;
        @SerializedName("photo_400_orig")@Expose public String photo_400_orig;
        @SerializedName("photo_max_orig")@Expose public String photo_max_orig;
        @SerializedName("photo_max")@Expose public String photo_max;
        @SerializedName("photo_id")@Expose public String photo_id;
        @SerializedName("has_photo")@Expose public Integer has_photo;
        @SerializedName("verified")@Expose public Integer verified;
        @SerializedName("online")@Expose public Integer online;
        @SerializedName("sex")@Expose public Integer sex; //2-male 1-female
        @SerializedName("last_seen")@Expose public LastSeen last_seen;
        @SerializedName("crop_photo")@Expose public CropPhoto crop_photo;
        public WebCardUser webInfo;
    }

    public static class LastSeen {
        @SerializedName("time")@Expose public Integer time;
        @SerializedName("platform")@Expose public Integer platform;
    }

    public static class Photo {
        @SerializedName("sizes")@Expose public List<Size> sizes;
    }

    public static class CropPhoto {
        @SerializedName("photo")@Expose public Photo photo;
        @SerializedName("crop")@Expose public Crop crop;
        @SerializedName("rect")@Expose public Rect rect;
    }

    public static class Size {
        @SerializedName("type")@Expose public String type;
        @SerializedName("url")@Expose public String url;
        @SerializedName("width")@Expose public int width;
        @SerializedName("height")@Expose public int height;
    }

    public static class Rect {
        @SerializedName("x")@Expose public double x;
        @SerializedName("y")@Expose public double y;
        @SerializedName("x2")@Expose public double x2;
        @SerializedName("y2")@Expose public double y2;
    }

    public static class Crop extends Rect { }


}
