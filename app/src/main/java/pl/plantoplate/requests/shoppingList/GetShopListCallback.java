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

import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.BaseCallback;
import retrofit2.Callback;

public class GetShopListCallback extends BaseCallback implements Callback<ResponseBody> {

    private ShopListCallback callback;

    /**
     * Constructor to create a new CreateGroupCallback object.
     *
     * @param view The view object to display the Snackbar.
     */
    public GetShopListCallback(View view, ShopListCallback callback) {
        super(view);
        this.callback = callback;
    }

    @Override
    public void handleSuccessResponse(String response) {
        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>(){}.getType();
        ArrayList<Product> shoppingList = gson.fromJson(response, productListType);

        callback.setShoppingList(shoppingList);
    }

    @Override
    public void handleErrorResponse(int code) {
        switch (code) {
            case 400:
                break;
            case 403:
                break;
            case 404:
                break;
            case 500:
                break;
        }
    }
}
