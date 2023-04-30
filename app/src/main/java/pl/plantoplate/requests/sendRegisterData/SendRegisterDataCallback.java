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
package pl.plantoplate.requests.sendRegisterData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;

import pl.plantoplate.requests.getConfirmCode.ConfirmCodeResponse;
import pl.plantoplate.ui.registration.EmailConfirmActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * A callback class for the API request to retrieve a confirmation code for user registration (for the first time).
 */
public class SendRegisterDataCallback implements Callback<ResponseBody> {

    // View object to display the Snackbar
    private final View view;
    // SharedPreferences object to store the user's email
    private final SharedPreferences prefs;
    // The user's register data (name, email, password)
    private final UserRegisterData userData;

    /**
     * Constructor to create a new SendRegisterDataCallback object.
     * @param view The view object to display the Snackbar.
     * @param userData The User register data (name, email, password).
     */
    public SendRegisterDataCallback(View view, UserRegisterData userData) {
        this.view = view;
        this.prefs = view.getContext().getSharedPreferences("prefs", 0);
        this.userData = userData;
    }

    /**
     * Handles the API response.
     * @param call The API call object.
     * @param response The API response object.
     */
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

        if (response.isSuccessful()) {

            // If the response body is null, display a Snackbar and return
            if (response.body() == null) {
                Snackbar.make(view, "Coś poszło nie tak!", Snackbar.LENGTH_LONG).show();
                return;
            }

            // If the response body is not null, parse the response body to CodeResponse and start the EmailConfirmActivity
            try {
                ConfirmCodeResponse code = new Gson().fromJson(response.body().string(), ConfirmCodeResponse.class);

                saveUserDataToPrefs();
                startEmailConfirmActivity(code.getCode());

            } catch (IOException e) {
                Snackbar.make(view, "Coś poszło nie tak!", Snackbar.LENGTH_LONG).show();
            }
        } else {
            handleErrorResponse(response.code());
        }
    }

    /**
     * Handles the API call failure.
     * @param call The API call object.
     * @param t The throwable object.
     */
    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        Snackbar.make(view, "Error while sending user data!", Snackbar.LENGTH_LONG).show();
    }

    /**
     * Handles the API server error responses.
     * @param code The error code.
     */
    private void handleErrorResponse(int code) {
        switch (code) {
            case 409:
                Snackbar.make(view, "Użytkownik o podanym adresie email już istnieje!", Snackbar.LENGTH_LONG).show();
                break;
            case 500:
                Snackbar.make(view, "Błąd serwera!", Snackbar.LENGTH_LONG).show();
                break;
            default:
                Snackbar.make(view, "Nieznana odpowiedź serwera!", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * Saves the user's email to the SharedPreferences.
     */
    private void saveUserDataToPrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", userData.getName());
        editor.putString("email", userData.getEmail());
        editor.putString("password", userData.getPassword());
        editor.apply();
    }

    /**
     * Put confirmation code to intent of new activity and starts the EmailConfirmActivity.
     * @param code The confirmation code.
     */
    private void startEmailConfirmActivity(String code) {
        Intent intent = new Intent(view.getContext(), EmailConfirmActivity.class);
        intent.putExtra("code", code);
        view.getContext().startActivity(intent);
    }
}
