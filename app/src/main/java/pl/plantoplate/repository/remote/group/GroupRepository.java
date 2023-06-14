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

package pl.plantoplate.repository.remote.group;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.models.CodeResponse;
import pl.plantoplate.repository.remote.models.CreateGroupData;
import pl.plantoplate.repository.remote.models.JwtResponse;
import pl.plantoplate.repository.remote.models.UserJoinGroupData;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupRepository {
    private GroupService groupService;

    //list of all active calls
    private ArrayList<Call<?>> calls;

    public GroupRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        groupService = retrofitClient.getClient().create(GroupService.class);
        calls = new ArrayList<>();
    }

    public void createGroup(CreateGroupData createGroupData, ResponseCallback<JwtResponse> callback) {
        Call<JwtResponse> call = groupService.createGroup(createGroupData);
        calls.add(call);
        call.enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(@NonNull Call<JwtResponse> call, @NonNull Response<JwtResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Użytkownik o podanym adresie email nie istnieje.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JwtResponse> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void joinGroupByCode(UserJoinGroupData userJoinGroupRequest, ResponseCallback<JwtResponse> callback) {
        Call<JwtResponse> call = groupService.joinGroupByCode(userJoinGroupRequest);
        calls.add(call);
        call.enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(@NonNull Call<JwtResponse> call, @NonNull Response<JwtResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Niepoprawny kod grupy.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JwtResponse> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void generateGroupCode(String token, String role, ResponseCallback<String> callback) {
        Call<CodeResponse> call = groupService.generateGroupCode(token, role);
        calls.add(call);
        call.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(@NonNull Call<CodeResponse> call, @NonNull Response<CodeResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body().getCode());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Niepoprawna rola dla użytkownika lub użytkownik nie należy do żadnej grupy.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CodeResponse> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void cancelCalls() {
        for (Call<?> call : calls) {
            call.cancel();
        }
    }
}
