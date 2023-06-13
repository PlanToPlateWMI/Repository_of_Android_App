package pl.plantoplate.repository.remote.user;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.RetrofitClient;
import pl.plantoplate.repository.remote.models.JwtResponse;
import pl.plantoplate.repository.remote.models.Message;
import pl.plantoplate.repository.remote.models.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private UserService userService;

    private ArrayList<Call<?>> calls;

    public UserRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        userService = retrofitClient.getClient().create(UserService.class);
        calls = new ArrayList<>();
    }

    public void changeUsername(String token, String username, ResponseCallback<UserInfo> callback) {
        Call<UserInfo> call = userService.changeUsername(token, new UserInfo(username));
        calls.add(call);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
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
                            callback.onError("Użytkownik nie istnieje.");
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
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void changeEmail(String token, UserInfo userInfo, ResponseCallback<JwtResponse> callback) {
        Call<JwtResponse> call = userService.changeEmail(token, userInfo);
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
                    System.out.println(code);
                    switch (code) {
                        case 400:
                            callback.onError("Użytkownik nie istnieje.");
                            break;
                        case 409:
                            callback.onError("Podany email jest już zajęty.");
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
                System.out.println(t.getMessage());
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void changePassword(String token, String password, ResponseCallback<UserInfo> callback) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(password);
        Call<UserInfo> call = userService.changePassword(token, userInfo);
        calls.add(call);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
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
                            callback.onError("Użytkownik nie istnieje.");
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
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void getUserInfo(String token, ResponseCallback<UserInfo> callback) {
        Call<UserInfo> call = userService.getUserInfo(token);
        calls.add(call);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserInfo> call, @NonNull Response<UserInfo> response) {
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
                            callback.onError("Użytkownik nie istnieje lub nie ma grupy.");
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
            public void onFailure(@NonNull Call<UserInfo> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void validatePasswordMatch(String token, String password, ResponseCallback<Message> callback) {

        Call<Message> call = userService.validatePasswordMatch(token, password);
        calls.add(call);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 409:
                            callback.onError("Podane hasło jest nieprawidłowe.");
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
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });

    }

    public void getUsersInfo(String token, ResponseCallback<ArrayList<UserInfo>> callback) {
        Call<ArrayList<UserInfo>> call = userService.getUsersInfo(token);
        calls.add(call);
        call.enqueue(new Callback<ArrayList<UserInfo>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<UserInfo>> call, @NonNull Response<ArrayList<UserInfo>> response) {
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
                            callback.onError("Użytkownik nie istnieje lub nie ma grupy.");
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
            public void onFailure(@NonNull Call<ArrayList<UserInfo>> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void changePermissions(String token, UserInfo userInfo, ResponseCallback<ArrayList<UserInfo>> callback) {
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(userInfo);
        Call<ArrayList<UserInfo>> call = userService.changePermissions(token, userInfos);
        calls.add(call);
        call.enqueue(new Callback<ArrayList<UserInfo>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<UserInfo>> call, @NonNull Response<ArrayList<UserInfo>> response) {
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
                            callback.onError("Użytkownik nie istnieje.");
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
            public void onFailure(@NonNull Call<ArrayList<UserInfo>> call, @NonNull Throwable t) {
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
