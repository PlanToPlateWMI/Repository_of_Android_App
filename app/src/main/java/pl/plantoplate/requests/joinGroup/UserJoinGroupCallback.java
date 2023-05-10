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
package pl.plantoplate.requests.joinGroup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.BaseCallback;
import pl.plantoplate.requests.signin.JwtResponse;
import pl.plantoplate.ui.main.ActivityMain;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A callback class for the API request for join a group.
 */
public class UserJoinGroupCallback extends BaseCallback implements Callback<ResponseBody> {

    private SharedPreferences prefs;

    /**
     * Constructor to create a new UserJoinGroupCallback object.
     * @param view The view object to display the Snackbar.
     */
    public UserJoinGroupCallback(View view) {
        super(view);
        this.prefs = view.getContext().getSharedPreferences("prefs",0);
    }

    @Override
    public void handleSuccessResponse(String response) {
        JwtResponse jwt = new Gson().fromJson(response, JwtResponse.class);
        Intent intent = new Intent(view.getContext(), ActivityMain.class);
        // save token to shared preferences
        saveTokenAndRole(jwt);
        // delete user email from shared preferences
        deleteUserEmail();
        view.getContext().startActivity(intent);
    }

    /**
     * Handles the API server error responses.
     * @param code The error code.
     */
    @Override
    public void handleErrorResponse(int code) {
        switch (code) {
            case 400:
                Snackbar.make(view, "Użytkownik o podanym adresie email nie istnieje lub kod zaproszeniowy jest niepoprawny!", Snackbar.LENGTH_LONG)
                        .show();
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
     * Saves the token and role to shared preferences.
     * @param jwt The JwtResponse object containing the token and role.
     */
    public void saveTokenAndRole(JwtResponse jwt) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", jwt.getToken());
        editor.putString("role", jwt.getRole());
        editor.apply();
    }

    /**
     * Deletes the user email from shared preferences.
     */
    public void deleteUserEmail() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("email");
        editor.apply();
    }
}
