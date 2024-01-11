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
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.service.AuthService;
import pl.plantoplate.data.remote.models.auth.CodeResponse;
import pl.plantoplate.data.remote.models.Message;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.user.UserRegisterData;
import pl.plantoplate.data.remote.models.auth.JwtResponse;
import pl.plantoplate.data.remote.models.auth.SignInData;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class AuthRepository {

    private final AuthService authService;

    public AuthRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        authService = retrofitClient.getClient().create(AuthService.class);
    }

    /**
     * Sends user register data to the server.
     * @param info User register data.
     * @return Single with code response.
     */
    public Single<CodeResponse> sendUserRegisterData(UserRegisterData info) {
        String userAlreadyExists = "Użytkownik o podanym adresie email już istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(409, userAlreadyExists);

        return authService.sendUserRegisterData(info)
                .onErrorResumeNext(throwable -> new ErrorHandler<CodeResponse>()
                        .handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    private final PublishSubject<Object> cancelSubject = PublishSubject.create();

    /**
     * Sends email to the server to get confirmation code.
     * @param email User email.
     * @param type Type of confirmation code.
     * @return Single with code response.
     */
    public Single<CodeResponse> getEmailConfirmCode(String email, String type) {
        String userDoesNotExist = "Użytkownik o podanym adresie email nie istnieje!";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return authService.getEmailConfirmCode(email, type)
                .onErrorResumeNext(throwable -> new ErrorHandler<CodeResponse>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(cancelSubject.toFlowable(BackpressureStrategy.BUFFER));
    }

    /**
     * Cancels sending email to the server to get confirmation code.
     * @param info SignInData object.
     * @return Single with jwt response.
     */
    public Single<JwtResponse> signIn(SignInData info) {
        String userDoesNotExist = "Użytkownik o podanym adresie email nie istnieje!";
        String incorrectCredentials = "Niepoprawne hasło!";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);
        errorMap.put(403, incorrectCredentials);

        return authService.signinUser(info)
                .onErrorResumeNext(throwable -> new ErrorHandler<JwtResponse>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Resets the password for a user based on the provided {@link SignInData}.
     *
     * @param info The {@link SignInData} containing user information for password reset.
     * @return A {@link Single} emitting a {@link Message} indicating the result of the password reset.
     *         The message can be success or an error message if the user does not exist.
     * @throws NullPointerException if {@code info} is {@code null}.
     *
     * @see SignInData
     * @see Message
     */
    public Single<Message> resetPassword(SignInData info) {
        String userDoesNotExist = "Użytkownik o podanym adresie email nie istnieje!";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return authService.resetPassword(info)
                .onErrorResumeNext(throwable -> new ErrorHandler<Message>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Checks if a user with the given email address already exists.
     *
     * @param email The email address to check for existing user.
     * @return A {@link Single} emitting a {@link Message} indicating the result of the existence check.
     *         The message can be success or an error message if the user already exists.
     * @throws NullPointerException if {@code email} is {@code null}.
     *
     * @see Message
     */
    public Single<Message> userExists(String email) {
        String userAlreadyExists = "Użytkownik o podanym adresie email już istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(409, userAlreadyExists);

        return authService.userExists(email)
                .onErrorResumeNext(throwable -> new ErrorHandler<Message>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}