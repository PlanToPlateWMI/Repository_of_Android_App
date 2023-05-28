package pl.plantoplate.repository.remote.auth;

import androidx.annotation.NonNull;

import pl.plantoplate.repository.models.CodeResponse;
import pl.plantoplate.repository.models.Message;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.RetrofitClient;
import pl.plantoplate.repository.models.UserRegisterData;
import pl.plantoplate.repository.models.JwtResponse;
import pl.plantoplate.repository.models.SignInData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private AuthService authService;

    public AuthRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        authService = retrofitClient.getClient().create(AuthService.class);
    }

    public void sendUserRegisterData(UserRegisterData info, ResponseCallback<String> callback) {
        Call<CodeResponse> call = authService.sendUserRegisterData(info);
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

    public void getEmailConfirmCode(String email, ResponseCallback<String> callback) {
        Call<CodeResponse> call = authService.getEmailConfirmCode(email);
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
}

