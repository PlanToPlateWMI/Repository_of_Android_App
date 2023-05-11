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
package pl.plantoplate.requests.joinGroup.sendRegisterData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import pl.plantoplate.requests.BaseCallback;
import pl.plantoplate.requests.getConfirmCode.ConfirmCodeResponse;
import pl.plantoplate.ui.registration.EmailConfirmActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import retrofit2.Callback;

import okhttp3.ResponseBody;

/**
 * A callback class for the API request to retrieve a confirmation code for user registration (for the first time).
 */
public class SendRegisterDataCallback extends BaseCallback implements Callback<ResponseBody> {

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
        super(view);
        this.prefs = view.getContext().getSharedPreferences("prefs", 0);
        this.userData = userData;
    }

    @Override
    public void handleSuccessResponse(String response) {
        ConfirmCodeResponse code = new Gson().fromJson(response, ConfirmCodeResponse.class);

        saveUserDataToPrefs();
        startEmailConfirmActivity(code.getCode());
    }

    /**
     * Handles the API server error responses.
     * @param code The error code.
     */
    @Override
    public void handleErrorResponse(int code) {
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
        editor.putString("name", userData.getUsername());
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
