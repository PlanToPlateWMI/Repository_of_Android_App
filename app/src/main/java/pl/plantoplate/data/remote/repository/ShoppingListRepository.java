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
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.shoppingList.ShoppingList;
import pl.plantoplate.data.remote.service.ShoppingListService;

public class ShoppingListRepository {
    private final ShoppingListService shoppingListService;

    public ShoppingListRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        shoppingListService = retrofitClient.getClient().create(ShoppingListService.class);
    }

    public Single<ArrayList<Product>> getToBuyShoppingList(String token) {
        return shoppingListService.getShoppingList(token, false)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Konto z takim emailem nie istnieje.");
                            put(500, "Wystąpił nieznany błąd.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> getBoughtShoppingList(String token) {
        return shoppingListService.getShoppingList(token, true)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Konto z takim emailem nie istnieje.");
                            put(500, "Wystąpił nieznany błąd.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Integer>> getBoughtProductsIds(String token) {
        return getBoughtShoppingList(token)
                .flatMap(response -> {
                    ArrayList<Integer> productIds = new ArrayList<>();
                    for (Product product : response) {
                        productIds.add(product.getId());
                    }
                    return Single.just(productIds);
                });
    }

    public Single<ArrayList<Product>> addProductToShoppingList(String token, Product product) {
        return shoppingListService.addProductToShopList(token, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Użytkownik chcę dodać produkt nie z jego grupy lub liczba produktów jest <=0");
                            put(500, "Wystąpił nieznany błąd.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> deleteProductFromShoppingList(String token, int productId) {
        return shoppingListService.deleteProductFromShopList(token, productId)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(404, "Nie znaleziono produktu.");
                            put(500, "Wystąpił nieznany błąd.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ShoppingList> changeProductStateInShopList(String token, int productId) {
        return shoppingListService.changeProductStateInShopList(token, productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> changeProductAmountInShopList(String token, int productId, Product product) {
        return shoppingListService.changeProductAmountInShopList(token, productId, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Niepoprawna ilość produktu.");
                            put(404, "Nie znaleziono produktu.");
                            put(500, "Wystąpił nieznany błąd.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}