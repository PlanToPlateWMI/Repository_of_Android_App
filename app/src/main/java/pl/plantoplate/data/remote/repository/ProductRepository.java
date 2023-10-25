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
import pl.plantoplate.data.remote.service.ProductService;

public class ProductRepository {
    private final ProductService productService;

    public ProductRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        productService = retrofitClient.getClient().create(ProductService.class);
    }

    public Single<ArrayList<Product>> getAllProducts(String token){
        return productService.getProducts(token, "all")
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Użytkownik o podanym adresie email nie istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> getOwnProducts(String token){
        return productService.getProducts(token, "group")
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Użytkownik o podanym adresie email nie istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> addProduct(String token, Product product){
        return productService.addProduct(token, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Produkt o podanej nazwie już istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> modifyProduct(String token, int productId, Product product){
        return productService.changeProduct(token, productId, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Produkt z takimi parametrami już istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> deleteProduct(String token, int productId){
        return productService.deleteProduct(token, productId)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Próba usunięcia produktu z bazy wszystkich produktów.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}