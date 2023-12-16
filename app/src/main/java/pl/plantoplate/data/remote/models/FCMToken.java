package pl.plantoplate.data.remote.models;

import com.google.gson.annotations.SerializedName;

public class FCMToken {

    @SerializedName("fcmToken")
    private String fcmToken;

    public FCMToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    @Override
    public String toString() {
        return "FCMToken{" +
                "fcmToken='" + fcmToken + '\'' +
                '}';
    }
}
