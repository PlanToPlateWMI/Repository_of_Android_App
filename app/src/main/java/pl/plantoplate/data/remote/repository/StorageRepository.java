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

import java.util.ArrayList;
import java.util.HashMap;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.service.StorageService;

public class StorageRepository {
    private final StorageService storageService;

    public StorageRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        storageService = retrofitClient.getClient().create(StorageService.class);
    }

    public Single<ArrayList<Product>> getStorage(String token){
        return storageService.getStorage(token)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<Integer, String>() {{
                            put(400, "Użytkownik o podanym adresie email nie istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> addProductToStorage(String token, Product product){
        return storageService.addProductToStorage(token, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<Integer, String>() {{
                            put(400, "Użytkownik o podanym adresie email nie istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> deleteProductFromStorage(String token, int productId){
        return storageService.deleteProductStorage(token, productId)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<Integer, String>() {{
                            put(400, "Produkt o podanym id nie istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> transferBoughtProductsToStorage(String token, ArrayList<Integer> productsIds){
        return storageService.transferBoughtProductsToStorage(token, productsIds)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<Integer, String>() {{
                            put(400, "Produkty nie istnieją.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> changeProductAmountInStorage(String token, int productId, Product product){
        return storageService.changeProductAmountInStorage(token, productId, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<Integer, String>() {{
                            put(400, "Niepoprawna ilość produktu.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
