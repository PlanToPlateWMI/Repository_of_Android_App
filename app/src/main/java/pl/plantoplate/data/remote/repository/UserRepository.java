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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.JwtResponse;
import pl.plantoplate.data.remote.models.Message;
import pl.plantoplate.data.remote.models.UserInfo;
import pl.plantoplate.data.remote.service.UserService;

public class UserRepository {
    private final UserService userService;

    public UserRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        userService = retrofitClient.getClient().create(UserService.class);
    }

    public Single<UserInfo> changeUsername(String token, String username) {
        return userService.changeUsername(token, new UserInfo(username))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<UserInfo> getUserInfo(String token) {
        return userService.getUserInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<JwtResponse> changeEmail(String token, UserInfo userInfo) {
        return userService.changeEmail(token, userInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<UserInfo> changePassword(String token, String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(password);

        return userService.changePassword(token, userInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Message> validatePasswordMatch(String token, String password) {
        return userService.validatePasswordMatch(token, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<UserInfo>> getUsersInfo(String token) {
        return userService.getUsersInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<UserInfo>> changePermissions(String token, UserInfo userInfo) {
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(userInfo);

        return userService.changePermissions(token, userInfos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
