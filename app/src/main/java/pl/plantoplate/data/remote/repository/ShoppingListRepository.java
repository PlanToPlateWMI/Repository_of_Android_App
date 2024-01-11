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

/**
 * A repository class responsible for retrieving shopping list-related data from the remote API.
 */
public class ShoppingListRepository {
    private final ShoppingListService shoppingListService;

    public ShoppingListRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        shoppingListService = retrofitClient.getClient().create(ShoppingListService.class);
    }

    /**
     * Retrieves a list of products for the "to-buy" shopping list associated with the provided token.
     *
     * @param token The token used to authenticate the request.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing products
     *         in the "to-buy" shopping list.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see Product
     */

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

    /**
     * Retrieves a list of products for the "bought" shopping list associated with the provided token.
     *
     * @param token The token used to authenticate the request.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing products
     *         in the "bought" shopping list.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see Product
     */
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

    /**
     * Retrieves a list of IDs for products in the "bought" shopping list associated with the provided token.
     *
     * @param token The token used to authenticate the request.
     * @return A {@link Single} emitting a {@link List} of {@link Integer} objects representing product IDs
     *         in the "bought" shopping list.
     * @throws NullPointerException if {@code token} is {@code null}.
     */
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

    /**
     * Adds a product to the shopping list associated with the provided token.
     *
     * @param token   The token used to authenticate the request.
     * @param product The product to be added to the shopping list.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the updated shopping list
     *         after adding the product.
     * @throws NullPointerException if {@code token} or {@code product} is {@code null}.
     *
     * @see Product
     */

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

    /**
     * Deletes a product from the shopping list associated with the provided token.
     *
     * @param token      The token used to authenticate the request.
     * @param productId  The ID of the product to be deleted from the shopping list.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the updated shopping list
     *         after deleting the product.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see Product
     */
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

    /**
     * Changes the state of a product in the shopping list associated with the provided token.
     *
     * @param token      The token used to authenticate the request.
     * @param productId  The ID of the product for which the state will be changed in the shopping list.
     * @return A {@link Single} emitting a {@link ShoppingList} object representing the updated shopping list
     *         after changing the state of the specified product.
     * @throws NullPointerException if {@code token} is {@code null}.
     *
     * @see ShoppingList
     */

    public Single<ShoppingList> changeProductStateInShopList(String token, int productId) {
        return shoppingListService.changeProductStateInShopList(token, productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Changes the amount of a product in the shopping list associated with the provided token.
     *
     * @param token      The token used to authenticate the request.
     * @param productId  The ID of the product for which the amount will be changed in the shopping list.
     * @param product    The updated product data, including the new amount.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the updated shopping list
     *         after changing the amount of the specified product.
     * @throws NullPointerException if {@code token} or {@code product} is {@code null}.
     *
     * @see Product
     */
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

    /**
     * Synchronizes meal products in the shopping list based on the provided token, meal shop plan, and synchronization flag.
     *
     * @param token          The token used to authenticate the request.
     * @param mealShopPlan   The data containing information required to synchronize meal products.
     * @param synchronize    A flag indicating whether to synchronize or not.
     * @return A {@link Single} emitting a {@link List} of {@link Product} objects representing the updated shopping list
     *         after synchronizing meal products.
     * @throws NullPointerException if {@code token} or {@code mealShopPlan} is {@code null}.
     *
     * @see MealShopPlan
     * @see Product
     */
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