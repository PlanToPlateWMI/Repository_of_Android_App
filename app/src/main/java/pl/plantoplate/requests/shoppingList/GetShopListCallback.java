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

package pl.plantoplate.requests.shoppingList;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.BaseCallback;
import pl.plantoplate.requests.products.Product;
import retrofit2.Call;
import retrofit2.Callback;

public class GetShopListCallback extends BaseCallback implements Callback<ResponseBody> {

    private ShopListCallback callback;
    private Context context;

    /**
     * Constructor to create a new CreateGroupCallback object.
     *
     * @param view The view object to display the Snackbar.
     */
    public GetShopListCallback(View view, ShopListCallback callback, Context context) {
        super(view);
        this.callback = callback;
        this.context = context;
    }

    /**
     * Handles the API server success responses.
     *
     * @param response The response object.
     */
    @Override
    public void handleSuccessResponse(String response) {
        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>(){}.getType();
        ArrayList<Product> shoppingList = gson.fromJson(response, productListType);

        callback.onShoppingListReceived(shoppingList);
    }

    /**
     * Handles the API server error responses.
     *
     * @param code The error code.
     */
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
