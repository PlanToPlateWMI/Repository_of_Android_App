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

public class FCMTokenRepository {
    private final FCMTokenService fcmtokenService;

    public FCMTokenRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        fcmtokenService = retrofitClient.getClient().create(FCMTokenService.class);
    }

    public Single<UserInfo> updateFcmToken(String token, FCMToken fcmToken) {
        Timber.e("updateFcmToken request: %s", fcmToken);
        return fcmtokenService.updateFCMToken(token, fcmToken)
                .onErrorResumeNext(throwable -> new ErrorHandler<UserInfo>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Użytkonik nie istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
}
