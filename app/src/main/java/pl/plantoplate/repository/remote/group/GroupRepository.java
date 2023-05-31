package pl.plantoplate.repository.remote.group;

import androidx.annotation.NonNull;

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

    public GroupRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        groupService = retrofitClient.getClient().create(GroupService.class);
    }

    public void createGroup(CreateGroupData createGroupData, ResponseCallback<JwtResponse> callback) {
        Call<JwtResponse> call = groupService.createGroup(createGroupData);
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
}
