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
package pl.plantoplate.requests.createGroup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.signin.JwtResponse;
import pl.plantoplate.ui.main.ActivityMain;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A callback class for the API request for create a new group.
 */
public class CreateGroupCallback implements Callback<ResponseBody> {
    // View object to display the Snackbar
    private final View view;

    private SharedPreferences prefs;

    /**
     * Constructor to create a new CreateGroupCallback object.
     * @param view The view object to display the Snackbar.
     */
    public CreateGroupCallback(View view) {
        this.view = view;
        this.prefs = view.getContext().getSharedPreferences("prefs", 0);
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

            // If the response body is not null, parse the response body to Jwt Response and start Main Activity.
            try {
                JwtResponse jwt = new Gson().fromJson(response.body().string(), JwtResponse.class);
                Intent intent = new Intent(view.getContext(), ActivityMain.class);
                // save token in shared preferences
                saveToken(jwt.getToken());
                view.getContext().startActivity(intent);

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
        Snackbar.make(view, "Błąd, sprawdź swoje połączenie internetowe!", Snackbar.LENGTH_LONG).show();
    }

    /**
     * Handles the API server error responses.
     * @param code The error code.
     */
    private void handleErrorResponse(int code) {
        switch (code) {
            case 400:
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

    //TODO save token
    public void saveToken(String token) {
    }
}
