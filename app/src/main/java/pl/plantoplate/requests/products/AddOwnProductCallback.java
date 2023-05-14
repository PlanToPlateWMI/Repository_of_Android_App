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
package pl.plantoplate.requests.products;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.BaseCallback;
import retrofit2.Callback;

public class AddOwnProductCallback extends BaseCallback implements Callback<ResponseBody> {

    /**
     * Constructor to create a new CreateGroupCallback object.
     *
     * @param view The view object to display the Snackbar.
     */
    public AddOwnProductCallback(View view) {
        super(view);
    }


    @Override
    public void handleSuccessResponse(String response) {
        Snackbar.make(view, "Dodano produkt!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void handleErrorResponse(int code) {
        switch (code) {
            case 400:
                Snackbar.make(view, "Niepoprawne dane lub produkt już istnieje!", Snackbar.LENGTH_LONG).show();
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
