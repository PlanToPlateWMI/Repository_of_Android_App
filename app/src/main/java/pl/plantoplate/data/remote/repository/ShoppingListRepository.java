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
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.shopping_list.MealShopPlan;
import pl.plantoplate.data.remote.models.shopping_list.ShoppingList;
import pl.plantoplate.data.remote.service.ShoppingListService;

public class ShoppingListRepository {
    private final ShoppingListService shoppingListService;

    public ShoppingListRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        shoppingListService = retrofitClient.getClient().create(ShoppingListService.class);
    }

    public Single<List<Product>> getToBuyShoppingList(String token) {
        String accountDoesNotExist = "Konto z takim emailem nie istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, accountDoesNotExist);

        return shoppingListService.getShoppingList(token, false)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Product>> getBoughtShoppingList(String token) {
        String accountDoesNotExist = "Konto z takim emailem nie istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, accountDoesNotExist);

        return shoppingListService.getShoppingList(token, true)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Integer>> getBoughtProductsIds(String token) {
        return getBoughtShoppingList(token)
                .flatMap(response -> {
                    ArrayList<Integer> productIds = new ArrayList<>();
                    for (Product product : response) {
                        productIds.add(product.getId());
                    }
                    return Single.just(productIds);
                });
    }

    public Single<List<Product>> addProductToShoppingList(String token, Product product) {
        String userDoesNotHaveAccessToGroup = "Użytkownik chcę dodać produkt nie z jego grupy lub liczba produktów jest <=0.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, userDoesNotHaveAccessToGroup);

        return shoppingListService.addProductToShopList(token, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Product>> deleteProductFromShoppingList(String token, int productId) {
        String productDoesNotExist = "Produkt nie istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(404, productDoesNotExist);

        return shoppingListService.deleteProductFromShopList(token, productId)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ShoppingList> changeProductStateInShopList(String token, int productId) {
        return shoppingListService.changeProductStateInShopList(token, productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Product>> changeProductAmountInShopList(String token, int productId, Product product) {
        String incorrectAmount = "Niepoprawna ilość produktu.";
        String productDoesNotExist = "Nie znaleziono produktu.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, incorrectAmount);
        errorMap.put(404, productDoesNotExist);

        return shoppingListService.changeProductAmountInShopList(token, productId, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Product>> synchronizeMealProducts(String token,
                                                                   MealShopPlan mealShopPlan,
                                                                   boolean synchronize) {

        String recipeDoesNotExist = "Przepis nie istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, recipeDoesNotExist);

        return shoppingListService.synchronizeMealProducts(token, mealShopPlan, synchronize)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Product>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}