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

import io.reactivex.rxjava3.core.Single;
import pl.plantoplate.data.remote.models.CodeResponse;
import pl.plantoplate.data.remote.models.CreateGroupData;
import pl.plantoplate.data.remote.models.JwtResponse;
import pl.plantoplate.data.remote.models.UserJoinGroupData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GroupService {

    @POST("api/auth/group")
    Single<JwtResponse> createGroup(@Body CreateGroupData createGroupRequest);

    @POST("api/invite-codes")
    Single<JwtResponse> joinGroupByCode(@Body UserJoinGroupData userJoinGroupRequest);

    @GET("api/invite-codes")
    Single<CodeResponse> generateGroupCode(@Header("Authorization") String token, @Query("role") String role);
}
