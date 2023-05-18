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

package pl.plantoplate.requests;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseCallback implements Callback<ResponseBody> {

    public View view;

    /**
     * Constructor to create a new CreateGroupCallback object.
     *
     * @param view The view object to display the Snackbar.
     */
    public BaseCallback(View view) {
        this.view = view;
    }

    /**
     * Handles the API response.
     * @param call The API call object.
     * @param response The API response object.
     */
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

        //System.out.println(response.code());

        if (response.isSuccessful()) {

            // If the response body is null, display a Snackbar and return
            if (response.body() == null) {
                Snackbar.make(view, "Coś poszło nie tak!", Snackbar.LENGTH_LONG).show();
                return;
            }

            // If no problems with response body, handle the response.
            try {
                handleSuccessResponse(response.body().string());

            } catch (IOException e) {
                //e.printStackTrace();
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
        Snackbar.make(view, "Sprawdź swoje połączenie internetowe!", Snackbar.LENGTH_LONG).show();
    }

    /**
     * Handles the API server success responses.
     * @param response The API response object.
     */
    public abstract void handleSuccessResponse(String response) throws IOException;

    /**
     * Handles the API server error responses.
     * @param code The error code.
     */
    public abstract void handleErrorResponse(int code);
}
