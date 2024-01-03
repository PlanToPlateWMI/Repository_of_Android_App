package pl.plantoplate.data.remote.service;

import io.reactivex.rxjava3.core.Single;
import pl.plantoplate.data.remote.models.FCMToken;
import pl.plantoplate.data.remote.models.user.UserInfo;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;

public interface FCMTokenService {
    @PATCH("api/users/fcmtoken")
    Single<UserInfo> updateFCMToken(@Header("Authorization") String token, @Body FCMToken fcmToken);
}
