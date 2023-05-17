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

package pl.plantoplate.requests.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.BaseCallback;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import pl.plantoplate.ui.main.ActivityMain;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A callback class for the API request to sign in the user.
 */
public class SignInCallback extends BaseCallback implements Callback<ResponseBody> {

    // SharedPreferences object to store the user's email
    private final SharedPreferences prefs;
    private ApplicationStateController controller;

    /**
     * Constructor to create a new SignInCallback object.
     * @param view The view object to display the Snackbar.
     */
    public SignInCallback(View view, ApplicationStateController controller) {
        super(view);
        this.prefs = view.getContext().getSharedPreferences("prefs", 0);
        this.controller = controller;
    }

    /**
     * Handles the API server success responses.
     * @param response The response object.
     */
    @Override
    public void handleSuccessResponse(String response) {
        Gson gson = new Gson();
        JwtResponse jwtResponse = gson.fromJson(response, JwtResponse.class);
        saveTokenAndRole(jwtResponse.getToken(), jwtResponse.getRole());

        // Start the main activity
        Intent intent = new Intent(view.getContext(), ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        view.getContext().startActivity(intent);

        // save app state
        controller.saveAppState(ApplicationState.MAIN_ACTIVITY);
    }

    /**
     * Handles the API server error responses.
     * @param code The error code.
     */
    @Override
    public void handleErrorResponse(int code) {
        switch (code) {
            case 400:
                Snackbar.make(view, "Użytkownik o podanym adresie email nie istnieje!", Snackbar.LENGTH_LONG).show();
                break;
            case 403:
                Snackbar.make(view, "Niepoprawne hasło!", Snackbar.LENGTH_LONG).show();
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
     * Saves the user's token and role to SharedPreferences.
     * @param token The user's token.
     * @param role The user's role.
     */
    public void saveTokenAndRole(String token, String role) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        editor.putString("role", role);
        editor.apply();
    }
}