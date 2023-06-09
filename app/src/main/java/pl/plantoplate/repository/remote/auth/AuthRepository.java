package pl.plantoplate.repository.remote.auth;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.models.CodeResponse;
import pl.plantoplate.repository.remote.models.Message;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.RetrofitClient;
import pl.plantoplate.repository.remote.models.UserRegisterData;
import pl.plantoplate.repository.remote.models.JwtResponse;
import pl.plantoplate.repository.remote.models.SignInData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private AuthService authService;

    //list of all active calls
    private ArrayList<Call<?>> calls;

    public AuthRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        authService = retrofitClient.getClient().create(AuthService.class);
        calls = new ArrayList<>();
    }

    public void sendUserRegisterData(UserRegisterData info, ResponseCallback<String> callback) {
        Call<CodeResponse> call = authService.sendUserRegisterData(info);
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
                        case 409:
                            callback.onError("Użytkownik o podanym adresie email już istnieje.");
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
                System.out.println(t.getMessage());
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void getEmailConfirmCode(String email, String type, ResponseCallback<String> callback) {
        Call<CodeResponse> call = authService.getEmailConfirmCode(email, type);
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
                            callback.onError("Użytkownik o podanym adresie email nie istnieje!");
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

    public void signIn(SignInData info, ResponseCallback<JwtResponse> callback) {
        Call<JwtResponse> call = authService.signinUser(info);
        calls.add(call);
        call.enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(@NonNull Call<JwtResponse> call, @NonNull Response<JwtResponse> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Użytkownik o podanym adresie email nie istnieje!");
                            break;
                        case 403:
                            callback.onError("Niepoprawne hasło!");
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

    public void resetPassword(SignInData info, ResponseCallback<Message> callback) {
        Call<Message> call = authService.resetPassword(info);
        calls.add(call);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {

                if (response.isSuccessful()) {
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
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void userExists(String email, ResponseCallback<Message> callback) {
        Call<Message> call = authService.userExists(email);
        calls.add(call);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {

                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 409:
                            callback.onError("Użytkownik o podanym adresie email już istnieje.");
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

    public void cancelCalls() {
        for (Call<?> call : calls) {
            call.cancel();
        }
    }
}

