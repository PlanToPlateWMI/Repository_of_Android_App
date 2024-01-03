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
    private FCMTokenService fcmtokenService;

    public FCMTokenRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        fcmtokenService = retrofitClient.getClient().create(FCMTokenService.class);
    }

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

    public void setFcmtokenService(FCMTokenService mockTokenService) {
        this.fcmtokenService = mockTokenService;
    }
}