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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.auth.JwtResponse;
import pl.plantoplate.data.remote.models.Message;
import pl.plantoplate.data.remote.models.user.UserInfo;
import pl.plantoplate.data.remote.service.UserService;

public class UserRepository {
    private final UserService userService;

    public UserRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        userService = retrofitClient.getClient().create(UserService.class);
    }

    public Single<UserInfo> changeUsername(String token, String username) {
        return userService.changeUsername(token, new UserInfo(username))
                .onErrorResumeNext(throwable -> new ErrorHandler<UserInfo>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Użytkownik nie istnieje!");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<UserInfo> getUserInfo(String token) {
        return userService.getUserInfo(token)
                .onErrorResumeNext(throwable -> new ErrorHandler<UserInfo>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Użytkownik nie istnieje lub nie jest w grupie.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<JwtResponse> changeEmail(String token, UserInfo userInfo) {
        return userService.changeEmail(token, userInfo)
                .onErrorResumeNext(throwable -> new ErrorHandler<JwtResponse>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Użytkownik nie istnieje!");
                            put(409, "Użytkownik o podanym adresie email już istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<UserInfo> changePassword(String token, String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(password);

        return userService.changePassword(token, userInfo)
                .onErrorResumeNext(throwable -> new ErrorHandler<UserInfo>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Użytkownik nie istnieje!");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Message> validatePasswordMatch(String token, String password) {
        return userService.validatePasswordMatch(token, password)
                .onErrorResumeNext(throwable -> new ErrorHandler<Message>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Użytkownik nie istnieje!");
                            put(409, "Hasło jest nieprawidłowe.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<UserInfo>> getUsersInfo(String token) {
        return userService.getUsersInfo(token)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<UserInfo>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Użytkownik nie istnieje lub nie jest w grupie.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<UserInfo>> changePermissions(String token, UserInfo userInfo) {
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(userInfo);

        return userService.changePermissions(token, userInfos)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<UserInfo>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Użytkownik nie jest z tej grupy lub rola jest nieprawidłowa.");
                            put(409, "Co najmniej jeden z użytkowników nie jest z tej grupy.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
