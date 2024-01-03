package pl.plantoplate.data.remote.models;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

public class FCMToken {

    @SerializedName("fcmToken")
    private String token;

    public FCMToken(String fcmToken) {
        this.token = fcmToken;
    }

    public String getFcmToken() {
        return token;
    }

    @NonNull
    @Override
    public String toString() {
        return "FCMToken{" +
                "fcmToken='" + token + '\'' +
                '}';
    }
}
