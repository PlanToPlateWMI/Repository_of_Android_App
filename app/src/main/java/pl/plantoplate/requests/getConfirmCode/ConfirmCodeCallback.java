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
package pl.plantoplate.requests.getConfirmCode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.BaseCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import pl.plantoplate.ui.registration.EmailConfirmActivity;

/**
 * A callback class for the API request to retrieve a confirmation code for user registration (if was problems with getting code).
 */
public class ConfirmCodeCallback extends BaseCallback implements Callback<ResponseBody> {

    // The user's email
    private SharedPreferences prefs;

    /**
     * Constructor to create a new ConfirmCodeCallback object.
     * @param view The view object to display the Snackbar.
     */
    public ConfirmCodeCallback(View view) {
        super(view);
        this.prefs = view.getContext().getSharedPreferences("prefs", 0);
    }

    @Override
    public void handleSuccessResponse(String response) {
        ConfirmCodeResponse code = new Gson().fromJson(response, ConfirmCodeResponse.class);

        // Save the code to the SharedPreferences
        prefs.edit().putString("code", code.getCode()).apply();
    }

    /**
     * Handles the API server error responses.
     * @param code The error code.
     */
    @Override
    public void handleErrorResponse(int code) {
        switch (code) {
            case 409:
                Snackbar.make(view, "Użytkownik o podanym adresie email nie istnieje!", Snackbar.LENGTH_LONG).show();
                break;
            case 500:
                Snackbar.make(view, "Błąd serwera!", Snackbar.LENGTH_LONG).show();
                break;
            default:
                Snackbar.make(view, "Nieznana odpowiedź serwera!", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

}
