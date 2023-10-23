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

package pl.plantoplate.data.remote.service;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Single;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.data.remote.models.ShoppingList;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShoppingListService {

    int maxAge = 3600 * 24 * 30;

    //@Headers("Cache-Control: max-age=" + maxAge)
    @GET("api/shopping")
    Single<ArrayList<Product>> getShoppingList(@Header("Authorization") String token, @Query("bought") boolean bought);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @POST("api/shopping")
    Single<ArrayList<Product>> addProductToShopList(@Header("Authorization") String token, @Body Product product);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @DELETE("api/shopping/{id}")
    Single<ArrayList<Product>> deleteProductFromShopList(@Header("Authorization") String token, @Path("id") int productId);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @PUT("api/shopping/{id}")
    Single<ShoppingList> changeProductStateInShopList(@Header("Authorization") String token, @Path("id") int productId);

    //@Headers("Cache-Control: max-age=" + maxAge)
    @PATCH("api/shopping/{id}")
    Single<ArrayList<Product>> changeProductAmountInShopList(@Header("Authorization") String token, @Path("id") int productId, @Body Product product);
}
