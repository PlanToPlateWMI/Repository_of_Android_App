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
package pl.plantoplate.requests.groupCodeGeneration;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.BaseCallback;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.GeneratedGroupCodeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A callback class for the API request for generate a group code.
 */
public class GenerateGroupCodeCallback extends BaseCallback implements Callback<ResponseBody> {

    /**
     * Constructor to create a new GenerateGroupCodeCallback object.
     * @param view The view object to display the Snackbar.
     */
    public GenerateGroupCodeCallback(View view) {
        super(view);
    }

    @Override
    public void handleSuccessResponse(String response) {
        GroupCodeResponse groupCode = new Gson().fromJson(response, GroupCodeResponse.class);

        Intent intent = new Intent(view.getContext(), GeneratedGroupCodeActivity.class);
        intent.putExtra("group_code", groupCode.getGroupCode());
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
                Snackbar.make(view, "Niepoprawna wartość dla roli użytkownika!", Snackbar.LENGTH_LONG)
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
}
