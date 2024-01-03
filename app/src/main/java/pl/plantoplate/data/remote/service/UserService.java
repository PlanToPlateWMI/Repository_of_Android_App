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
package pl.plantoplate.data.remote.service;

import java.util.List;
import io.reactivex.rxjava3.core.Single;
import pl.plantoplate.data.remote.models.auth.JwtResponse;
import pl.plantoplate.data.remote.models.Message;
import pl.plantoplate.data.remote.models.user.UserInfo;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Query;

public interface UserService {

    @PATCH("api/users/username")
    Single<UserInfo> changeUsername(@Header("Authorization") String token, @Body UserInfo username);

    @PATCH("api/users/email")
    Single<JwtResponse> changeEmail(@Header("Authorization") String token, @Body UserInfo userInfo);

    @PATCH("api/users/password")
    Single<UserInfo> changePassword(@Header("Authorization") String token, @Body UserInfo password);

    @GET("api/users")
    Single<UserInfo> getUserInfo(@Header("Authorization") String token);

    @GET("api/users/password/match")
    Single<Message> validatePasswordMatch(@Header("Authorization") String token, @Query("password") String password);

    @GET("api/users/infos")
    Single<List<UserInfo>> getUsersInfo(@Header("Authorization") String token);

    @PATCH("api/users/roles")
    Single<List<UserInfo>> changePermissions(@Header("Authorization") String token, @Body List<UserInfo> usersInfo);
}
