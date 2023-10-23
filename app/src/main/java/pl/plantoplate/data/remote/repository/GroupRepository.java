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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.service.GroupService;
import pl.plantoplate.data.remote.models.CodeResponse;
import pl.plantoplate.data.remote.models.CreateGroupData;
import pl.plantoplate.data.remote.models.JwtResponse;
import pl.plantoplate.data.remote.models.UserJoinGroupData;
import pl.plantoplate.data.remote.RetrofitClient;

public class GroupRepository {
    private GroupService groupService;

    public GroupRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        groupService = retrofitClient.getClient().create(GroupService.class);
    }

    public Single<JwtResponse> createGroup(CreateGroupData createGroupData){
        return groupService.createGroup(createGroupData)
                .onErrorResumeNext(throwable -> new ErrorHandler<JwtResponse>().
                        handleHttpError(throwable, new HashMap<Integer, String>() {{
                            put(400, "Użytkownik o podanym adresie email nie istnieje.");
                            put(500, "Wystąpił nieznany błąd.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<JwtResponse> joinGroupByCode(UserJoinGroupData userJoinGroupRequest) {
        return groupService.joinGroupByCode(userJoinGroupRequest)
                .onErrorResumeNext(throwable -> new ErrorHandler<JwtResponse>().
                        handleHttpError(throwable, new HashMap<Integer, String>() {{
                            put(400, "Niepoprawny kod grupy.");
                            put(500, "Wystąpił nieznany błąd.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<CodeResponse> generateGroupCode(String token, String role){
        return groupService.generateGroupCode(token, role)
                .onErrorResumeNext(throwable -> new ErrorHandler<CodeResponse>().
                        handleHttpError(throwable, new HashMap<Integer, String>() {{
                            put(400, "Niepoprawna rola dla użytkownika lub użytkownik nie należy do żadnej grupy.");
                            put(500, "Wystąpił nieznany błąd.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
