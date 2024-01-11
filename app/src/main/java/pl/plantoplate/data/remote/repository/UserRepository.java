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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.auth.JwtResponse;
import pl.plantoplate.data.remote.models.Message;
import pl.plantoplate.data.remote.models.user.UserInfo;
import pl.plantoplate.data.remote.service.UserService;

/**
 * Class that handles user data.
 */
public class UserRepository {
    private final UserService userService;

    public UserRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        userService = retrofitClient.getClient().create(UserService.class);
    }

    /**
     * Changes the username associated with the provided token.
     *
     * @param token    The token used to authenticate the request.
     * @param username The new username to be set for the user.
     * @return A {@link Single} emitting a {@link UserInfo} object representing the user information
     *         after changing the username.
     * @throws NullPointerException if {@code token} or {@code username} is {@code null}.
     *
     * @see UserInfo
     */
    public Single<UserInfo> changeUsername(String token, String username) {
        String userDoesNotExist = "Użytkownik nie istnieje!";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return userService.changeUsername(token, new UserInfo(username))
                .onErrorResumeNext(throwable -> new ErrorHandler<UserInfo>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Retrieves information about the user associated with the provided token.
     *
     * @param token The token used to authenticate the request.
     * @return A {@link Single} emitting a {@link UserInfo} object representing information about the user.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see UserInfo
     */
    public Single<UserInfo> getUserInfo(String token) {
        String userDoesNotExist = "Użytkownik nie istnieje lub nie jest w grupie.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return userService.getUserInfo(token)
                .onErrorResumeNext(throwable -> new ErrorHandler<UserInfo>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Changes the email for the user associated with the provided token.
     *
     * @param token     The token used to authenticate the request.
     * @param userInfo  The user information, including the updated email, to be applied.
     * @return A {@link Single} emitting a {@link JwtResponse} object representing the updated user information
     *         and a new authentication token after changing the email.
     * @throws NullPointerException if {@code token} or {@code userInfo} is {@code null}.
     *
     * @see JwtResponse
     */
    public Single<JwtResponse> changeEmail(String token, UserInfo userInfo) {
        String userDoesNotExist = "Użytkownik nie istnieje!";
        String emailAlreadyExists = "Użytkownik o podanym adresie email już istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);
        errorMap.put(409, emailAlreadyExists);

        return userService.changeEmail(token, userInfo)
                .onErrorResumeNext(throwable -> new ErrorHandler<JwtResponse>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Changes the password for the user associated with the provided token.
     *
     * @param token    The token used to authenticate the request.
     * @param password The new password to be set for the user.
     * @return A {@link Single} emitting a {@link UserInfo} object representing the updated user information
     *         after changing the password.
     * @throws NullPointerException if {@code token} or {@code password} is {@code null}.
     *
     * @see UserInfo
     */
    public Single<UserInfo> changePassword(String token, String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(password);

        String userDoesNotExist = "Użytkownik nie istnieje!";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return userService.changePassword(token, userInfo)
                .onErrorResumeNext(throwable -> new ErrorHandler<UserInfo>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Validates whether the provided password matches the user's password associated with the provided token.
     *
     * @param token    The token used to authenticate the request.
     * @param password The password to be validated.
     * @return A {@link Single} emitting a {@link Message} object representing the validation result.
     * @throws NullPointerException if {@code token} or {@code password} is {@code null}.
     *
     * @see Message
     */
    public Single<Message> validatePasswordMatch(String token, String password) {
        String userDoesNotExist = "Użytkownik nie istnieje!";
        String invalidCredentials = "Hasło jest nieprawidłowe.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);
        errorMap.put(409, invalidCredentials);

        return userService.validatePasswordMatch(token, password)
                .onErrorResumeNext(throwable -> new ErrorHandler<Message>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Retrieves information about users in the group associated with the provided token.
     *
     * @param token The token used to authenticate the request.
     * @return A {@link Single} emitting a {@link List} of {@link UserInfo} objects representing information
     *         about users in the group.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see UserInfo
     */
    public Single<List<UserInfo>> getUsersInfo(String token) {
        String userDoesNotExist = "Użytkownik nie istnieje lub nie jest w grupie.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return userService.getUsersInfo(token)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<UserInfo>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Changes permissions for a list of users associated with the provided token.
     *
     * @param token     The token used to authenticate the request.
     * @param userInfo  The user information, including the updated permissions, to be applied.
     * @return A {@link Single} emitting a {@link List} of {@link UserInfo} objects representing the updated user information
     *         after changing permissions.
     * @throws NullPointerException if {@code token} or {@code userInfo} is {@code null}.
     *
     * @see UserInfo
     */
    public Single<List<UserInfo>> changePermissions(String token, UserInfo userInfo) {
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(userInfo);

        String userNotFromGroup = "Użytkownik nie jest z tej grupy lub rola jest nieprawidłowa.";
        String conflict = "Co najmniej jeden z użytkowników nie jest z tej grupy.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userNotFromGroup);
        errorMap.put(409, conflict);

        return userService.changePermissions(token, userInfos)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<UserInfo>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}