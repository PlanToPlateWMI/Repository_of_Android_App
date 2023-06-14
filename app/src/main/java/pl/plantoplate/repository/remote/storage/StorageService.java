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

package pl.plantoplate.repository.remote.storage;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.models.Product;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StorageService {

    int maxAge = 3600 * 24 * 30;

    @GET("api/pantry/")
    Call<ArrayList<Product>> getStorage(@Header("Authorization") String token);

    @POST("api/pantry/")
    Call<ArrayList<Product>> addProductToStorage(@Header("Authorization") String token, @Body Product product);

    @DELETE("api/pantry/{id}")
    Call<ArrayList<Product>> deleteProductStorage(@Header("Authorization") String token, @Path("id") int productId);

    @POST("api/pantry/transfer")
    Call<ArrayList<Product>> transferBoughtProductsToStorage(@Header("Authorization") String token, @Body ArrayList<Integer> productIds);

    @PATCH("api/pantry/{id}")
    Call<ArrayList<Product>> changeProductAmountInStorage(@Header("Authorization") String token, @Path("id") int productId, @Body Product product);
}
