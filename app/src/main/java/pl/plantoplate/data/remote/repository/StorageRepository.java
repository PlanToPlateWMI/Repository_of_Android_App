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
package pl.plantoplate.data.remote.repository;

import java.util.HashMap;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.service.StorageService;

/**
 * A repository class responsible for retrieving storage-related data from the remote API.
 */
public class StorageRepository {
    private final StorageService storageService;

    public StorageRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        storageService = retrofitClient.getClient().create(StorageService.class);
    }

    /**
     * Retrieves a list of products from the user's storage associated with the provided token.
     *
     * @param token The token used to authenticate the request.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the products
     *         stored by the user.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see Product
     */
    public Single<List<Product>> getStorage(String token){
        String userDoesNotExist = "Użytkownik o podanym adresie email nie istnieje!";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return storageService.getStorage(token)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Adds a product to the user's storage associated with the provided token.
     *
     * @param token   The token used to authenticate the request.
     * @param product The product to be added to the user's storage.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the updated storage
     *         after adding the product.
     * @throws NullPointerException if {@code token} or {@code product} is {@code null}.
     *
     * @see Product
     */
    public Single<List<Product>> addProductToStorage(String token, Product product){
        String userDoesNotExist = "Użytkownik o podanym adresie email nie istnieje!";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return storageService.addProductToStorage(token, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Deletes a product from the user's storage by its ID associated with the provided token.
     *
     * @param token      The token used to authenticate the request.
     * @param productId  The ID of the product to be deleted from the user's storage.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the updated storage
     *         after deleting the product.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see Product
     */
    public Single<List<Product>> deleteProductFromStorage(String token, int productId){
        String productDoesNotExist = "Produkt o podanym id nie istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, productDoesNotExist);

        return storageService.deleteProductStorage(token, productId)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Transfers bought products to the user's storage based on the provided token and product IDs.
     *
     * @param token       The token used to authenticate the request.
     * @param productsIds The list of IDs of products to be transferred to the user's storage.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the updated storage
     *         after transferring the products.
     * @throws NullPointerException if {@code token} or {@code productsIds} is {@code null}.
     *
     * @see Product
     */
    public Single<List<Product>> transferBoughtProductsToStorage(String token, List<Integer> productsIds){
        String productsDoNotExist = "Produkty o podanych id nie istnieją.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, productsDoNotExist);

        return storageService.transferBoughtProductsToStorage(token, productsIds)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Changes the amount of a product in the user's storage associated with the provided token.
     *
     * @param token      The token used to authenticate the request.
     * @param productId  The ID of the product for which the amount will be changed in the storage.
     * @param product    The updated product data, including the new amount.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the updated storage
     *         after changing the amount of the specified product.
     * @throws NullPointerException if {@code token} or {@code product} is {@code null}.
     *
     * @see Product
     */
    public Single<List<Product>> changeProductAmountInStorage(String token, int productId, Product product){
        String incorrectAmount = "Niepoprawna ilość produktu.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, incorrectAmount);

        return storageService.changeProductAmountInStorage(token, productId, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}