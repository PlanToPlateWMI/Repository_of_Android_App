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
import pl.plantoplate.data.remote.service.ProductService;

/**
 * Class that handles products.
 */
public class ProductRepository {
    private final ProductService productService;

    public ProductRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        productService = retrofitClient.getClient().create(ProductService.class);
    }

    /**
     * Retrieves a list of all products associated with the provided token.
     *
     * @param token The token used to authenticate the request.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing all available products.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see Product
     */

    public Single<List<Product>> getAllProducts(String token){
        String userDoesNotExist = "Użytkownik o podanym adresie email nie istnieje!";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return productService.getProducts(token, "all")
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Retrieves a list of own products associated with the provided token.
     *
     * @param token The token used to authenticate the request.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the products
     *         associated with the user.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see Product
     */

    public Single<List<Product>> getOwnProducts(String token){
        String userDoesNotExist = "Użytkownik o podanym adresie email nie istnieje!";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotExist);

        return productService.getProducts(token, "group")
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Adds a new product using the provided token and product data.
     *
     * @param token   The token used to authenticate the request.
     * @param product The data for the new product to be added.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the updated list of products
     *         after the addition operation.
     * @throws NullPointerException if {@code token} or {@code product} is {@code null}.
     *
     * @see Product
     */

    public Single<List<Product>> addProduct(String token, Product product){
        String productAlreadyExists = "Produkt o podanej nazwie już istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, productAlreadyExists);

        return productService.addProduct(token, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Modifies a product by its ID using the provided token and new product data.
     *
     * @param token      The token used to authenticate the request.
     * @param productId  The ID of the product to be modified.
     * @param product    The new data for modifying the product.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the updated list of products
     *         after the modification operation.
     * @throws NullPointerException if {@code token} or {@code product} is {@code null}.
     *
     * @see Product
     */

    public Single<List<Product>> modifyProduct(String token, int productId, Product product){
        String productAlreadyExists = "Produkt z takimi parametrami już istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, productAlreadyExists);

        return productService.changeProduct(token, productId, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Deletes a product by its ID associated with the provided token.
     *
     * @param token      The token used to authenticate the request.
     * @param productId  The ID of the product to be deleted.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the updated list of products
     *         after the deletion operation.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see Product
     */

    public Single<List<Product>> deleteProduct(String token, int productId){
        String tryToDeleteAllProducts = "Próba usunięcia produktu z bazy wszystkich produktów.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, tryToDeleteAllProducts);

        return productService.deleteProduct(token, productId)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}