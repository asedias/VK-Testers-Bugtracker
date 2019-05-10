package ru.asedias.vkbugtracker.api.apimethods.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo extends VKAPIResponse<UserInfo.User> {

    public static class User {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("photo_200")
        @Expose
        private String photo200;
        @SerializedName("tester")
        @Expose
        private int tester;

        public Integer getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getPhoto200() {
            return photo200;
        }

        public int getTesterNum() {
            return tester;
        }
    }

}
