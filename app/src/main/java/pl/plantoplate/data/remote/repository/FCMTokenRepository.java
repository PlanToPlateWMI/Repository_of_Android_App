/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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