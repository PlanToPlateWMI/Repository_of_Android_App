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
import pl.plantoplate.data.remote.models.auth.CodeResponse;
import pl.plantoplate.data.remote.models.auth.UserCredentials;
import pl.plantoplate.data.remote.models.auth.JwtResponse;
import pl.plantoplate.data.remote.models.user.UserJoinGroupData;
import pl.plantoplate.data.remote.RetrofitClient;

public class GroupRepository {
    private final GroupService groupService;

    public GroupRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        groupService = retrofitClient.getClient().create(GroupService.class);
    }

    /**
     * Creates a new group using the provided user credentials.
     *
     * @param userCredentials The credentials of the user creating the group.
     * @return A {@link Single} emitting a {@link JwtResponse} object representing the authentication response
     *         after creating the group.
     * @throws NullPointerException if {@code userCredentials} is {@code null}.
     *
     * @see JwtResponse
     */

    public Single<JwtResponse> createGroup(UserCredentials userCredentials){
        String userDoesNotExist = "Użytkownik o podanym adresie email nie istnieje!";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return groupService.createGroup(userCredentials)
                .onErrorResumeNext(throwable -> new ErrorHandler<JwtResponse>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Joins a group using the provided user join group request data.
     *
     * @param userJoinGroupRequest The data containing information required to join a group.
     * @return A {@link Single} emitting a {@link JwtResponse} object representing the authentication response
     *         after joining the group.
     * @throws NullPointerException if {@code userJoinGroupRequest} is {@code null}.
     *
     * @see JwtResponse
     */

    public Single<JwtResponse> joinGroupByCode(UserJoinGroupData userJoinGroupRequest) {
        String incorrectCode = "Niepoprawny kod grupy.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, incorrectCode);

        return groupService.joinGroupByCode(userJoinGroupRequest)
                .onErrorResumeNext(throwable -> new ErrorHandler<JwtResponse>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Generates a group code for a user identified by the provided token and role.
     *
     * @param token The token used to identify the user.
     * @param role  The role of the user requesting the group code.
     * @return A {@link Single} emitting a {@link CodeResponse} object representing the generated group code.
     * @throws NullPointerException if {@code token} or {@code role} is {@code null}.
     *
     * @see CodeResponse
     */
    public Single<CodeResponse> generateGroupCode(String token, String role){
        String incorrectRole = "Niepoprawna rola dla użytkownika lub użytkownik nie należy do żadnej grupy.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, incorrectRole);

        return groupService.generateGroupCode(token, role)
                .onErrorResumeNext(throwable -> new ErrorHandler<CodeResponse>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}