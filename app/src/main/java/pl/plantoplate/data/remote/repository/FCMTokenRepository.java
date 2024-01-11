package pl.plantoplate.data.remote.repository;

import java.util.HashMap;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.FCMToken;
import pl.plantoplate.data.remote.models.user.UserInfo;
import pl.plantoplate.data.remote.service.FCMTokenService;
import timber.log.Timber;

/**
 * Class that handles FCM token.
 */
public class FCMTokenRepository {
    private FCMTokenService fcmtokenService;

    public FCMTokenRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        fcmtokenService = retrofitClient.getClient().create(FCMTokenService.class);
    }

    /**
     * Updates the Firebase Cloud Messaging (FCM) token for a user identified by the provided token.
     *
     * @param token    The token used to identify the user.
     * @param fcmToken The updated FCM token information.
     * @return A {@link Single} emitting a {@link UserInfo} object representing the user information
     *         after the FCM token update.
     * @throws NullPointerException if {@code token} or {@code fcmToken} is {@code null}.
     *
     * @see UserInfo
     * @see FCMToken
     */
    public Single<UserInfo> updateFcmToken(String token, FCMToken fcmToken) {
        String userDoesNotExist = "Użytkownik nie istnieje!";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return fcmtokenService.updateFCMToken(token, fcmToken)
                .onErrorResumeNext(throwable -> new ErrorHandler<UserInfo>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
}