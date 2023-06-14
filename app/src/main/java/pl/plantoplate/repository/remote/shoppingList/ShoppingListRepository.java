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

package pl.plantoplate.repository.remote.shoppingList;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.models.ShoppingList;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingListRepository {
    private ShoppingListService shoppingListService;

    private ArrayList<Call<?>> calls;

    public ShoppingListRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        shoppingListService = retrofitClient.getClient().create(ShoppingListService.class);
        calls = new ArrayList<>();
    }

    public void getToBuyShoppingList(String token, ResponseCallback<ArrayList<Product>> callback) {
        Call<ArrayList<Product>> call = shoppingListService.getShoppingList(token, false);
        calls.add(call);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Użytkownik o podanym adresie email nie istnieje.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void getBoughtShoppingList(String token, ResponseCallback<ArrayList<Product>> callback) {
        Call<ArrayList<Product>> call = shoppingListService.getShoppingList(token, true);
        calls.add(call);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Użytkownik o podanym adresie email nie istnieje.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void getBoughtProductsIds(String token, ResponseCallback<ArrayList<Integer>> callback) {
        ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
        // get bought products ids
        shoppingListRepository.getBoughtShoppingList(token, new ResponseCallback<ArrayList<Product>>() {

            @Override
            public void onSuccess(ArrayList<Product> response) {
                ArrayList<Integer> productsIds = new ArrayList<>();
                for (Product product : response) {
                    productsIds.add(product.getId());
                }
                callback.onSuccess(productsIds);
            }
            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
            @Override
            public void onFailure(String failureMessage) {
                callback.onFailure(failureMessage);
            }
        });
    }

    public void addProductToShopList(String token, Product product, ResponseCallback<ArrayList<Product>> callback) {
        Call<ArrayList<Product>> call = shoppingListService.addProductToShopList(token, product);
        calls.add(call);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Jednostka miary nie może być mniejsza lub równa 0.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void deleteProductFromShopList(String token, int productId, ResponseCallback<ArrayList<Product>> callback) {
        Call<ArrayList<Product>> call = shoppingListService.deleteProductFromShopList(token, productId);
        calls.add(call);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Próba usunięcia produktu, który nie jest własnym produktem użytkownika.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void changeProductStateInShopList(String token, int productId, ResponseCallback<ShoppingList> callback) {
        Call<ShoppingList> call = shoppingListService.changeProductStateInShopList(token, productId);
        calls.add(call);
        call.enqueue(new Callback<ShoppingList>() {
            @Override
            public void onResponse(@NonNull Call<ShoppingList> call, @NonNull Response<ShoppingList> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Próba zmiany stanu produktu nie z grupy użytkownika.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShoppingList> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void changeProductAmountInShopList(String token, int productId, Product product, ResponseCallback<ArrayList<Product>> callback) {
        Call<ArrayList<Product>> call = shoppingListService.changeProductAmountInShopList(token, productId, product);
        calls.add(call);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onError("Coś poszło nie tak!");
                        return;
                    }
                    callback.onSuccess(response.body());

                } else {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            callback.onError("Próba zmiany ilości produktu nie z grupy użytkownika.");
                            break;
                        case 500:
                            callback.onError("Błąd serwera!");
                            break;
                        default:
                            callback.onError("Wystąpił nieznany błąd.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                callback.onFailure("Brak połączenia z serwerem. Sprawdź połączenie z internetem.");
            }
        });
    }

    public void cancelCalls() {
        for (Call<?> call : calls) {
            call.cancel();
        }
    }
}
