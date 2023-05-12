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
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.BaseCallback;
import retrofit2.Callback;

public class GetProductsDBaseCallback extends BaseCallback implements Callback<ResponseBody> {

    private ProductsListCallback callback;

    /**
     * Constructor to create a new CreateGroupCallback object.
     *
     * @param view The view object to display the Snackbar.
     * @param callback The callback object to call the onProductsListsReceived method.
     */
    public GetProductsDBaseCallback(View view, ProductsListCallback callback) {
        super(view);
        this.callback = callback;
    }


    @Override
    public void handleSuccessResponse(String response) {
        Gson gson = new Gson();
        ProductsDataBase productsDataBase = gson.fromJson(response, ProductsDataBase.class);

        callback.onProductsListsReceived(productsDataBase.getGeneralProducts(), productsDataBase.getGroupProducts());
    }

    @Override
    public void handleErrorResponse(int code) {
        switch (code) {
            case 401:
                Snackbar.make(view, "Nieautoryzowany dostęp!", Snackbar.LENGTH_LONG).show();
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
