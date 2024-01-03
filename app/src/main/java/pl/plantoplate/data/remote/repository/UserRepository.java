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

public class UserRepository {
    private final UserService userService;

    public UserRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        userService = retrofitClient.getClient().create(UserService.class);
    }

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

    public Single<Message> validatePasswordMatch(String token, String password) {
        String userDoesNotExist = "Użytkownik nie istnieje!";
        String passwordIsInvalid = "Hasło jest nieprawidłowe.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);
        errorMap.put(409, passwordIsInvalid);

        return userService.validatePasswordMatch(token, password)
                .onErrorResumeNext(throwable -> new ErrorHandler<Message>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

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