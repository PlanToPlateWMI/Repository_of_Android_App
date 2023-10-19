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

package pl.plantoplate.ui.main.shoppingList.viewModels;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import pl.plantoplate.R;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.models.ShoppingList;
import pl.plantoplate.repository.remote.models.UserInfo;
import pl.plantoplate.repository.remote.shoppingList.ShoppingListRepository;
import pl.plantoplate.repository.remote.storage.StorageRepository;
import pl.plantoplate.repository.remote.user.UserRepository;

public class ShoppingListViewModel extends AndroidViewModel {

    private final SharedPreferences prefs;
    private final ShoppingListRepository shoppingListRepository;

    private MutableLiveData<String> toBuyProductsOnSuccessOperation;
    private MutableLiveData<String> toBuyProductsOnErrorOperation;
    private MutableLiveData<String> boughtProductsOnSuccessOperation;
    private MutableLiveData<String> boughtProductsOnErrorOperation;
    private MutableLiveData<ArrayList<Product>> toBuyProducts;
    private MutableLiveData<ArrayList<Product>> boughtProducts;
    private MutableLiveData<UserInfo> userInfo;

    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences("prefs", 0);

        shoppingListRepository = new ShoppingListRepository();
    }

    public MutableLiveData<String> getToBuyProductsOnSuccessOperation() {
        if (toBuyProductsOnSuccessOperation == null) {
            toBuyProductsOnSuccessOperation = new MutableLiveData<>();
        }
        return toBuyProductsOnSuccessOperation;
    }

    public MutableLiveData<String> getToBuyProductsOnErrorOperation() {
        if (toBuyProductsOnErrorOperation == null) {
            toBuyProductsOnErrorOperation = new MutableLiveData<>();
        }
        return toBuyProductsOnErrorOperation;
    }

    public MutableLiveData<String> getBoughtProductsOnSuccessOperation() {
        if (boughtProductsOnSuccessOperation == null) {
            boughtProductsOnSuccessOperation = new MutableLiveData<>();
        }
        return boughtProductsOnSuccessOperation;
    }

    public MutableLiveData<String> getBoughtProductsOnErrorOperation() {
        if (boughtProductsOnErrorOperation == null) {
            boughtProductsOnErrorOperation = new MutableLiveData<>();
        }
        return boughtProductsOnErrorOperation;
    }

    public MutableLiveData<ArrayList<Product>> getToBuyProducts() {
        if (toBuyProducts == null) {
            toBuyProducts = new MutableLiveData<>();
            toBuyProducts.setValue(new ArrayList<>());
            fetchToBuyProducts();
        }
        return toBuyProducts;
    }

    public MutableLiveData<ArrayList<Product>> getBoughtProducts() {
        if (boughtProducts == null) {
            boughtProducts = new MutableLiveData<>();
            boughtProducts.setValue(new ArrayList<>());
            fetchBoughtProducts();
        }
        return boughtProducts;
    }

    public MutableLiveData<UserInfo> getUserInfo() {
        if (userInfo == null) {
            userInfo = new MutableLiveData<>();
            fetchUserInfo();
        }
        return userInfo;
    }

    public void fetchToBuyProducts() {
        String token = "Bearer " + prefs.getString("token", "");

        shoppingListRepository.getToBuyShoppingList(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                toBuyProducts.setValue(products);
            }

            @Override
            public void onError(String message) {
                toBuyProductsOnErrorOperation.setValue(message);
            }

            @Override
            public void onFailure(String failureMessage) {
                toBuyProductsOnErrorOperation.setValue(failureMessage);
            }
        });
    }

    public void fetchBoughtProducts() {
        String token = "Bearer " + prefs.getString("token", "");

        shoppingListRepository.getBoughtShoppingList(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                if (boughtProducts == null) {
                    boughtProducts = new MutableLiveData<>();
                }
                boughtProducts.setValue(response);
            }

            @Override
            public void onError(String message) {
                boughtProductsOnErrorOperation.setValue(message);
            }

            @Override
            public void onFailure(String failureMessage) {
                boughtProductsOnErrorOperation.setValue(failureMessage);
            }
        });
    }

    public void fetchUserInfo() {
        UserRepository userRepository = new UserRepository();
        String token = "Bearer " + prefs.getString("token", "");

        userRepository.getUserInfo(token, new ResponseCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                userInfo.setValue(response);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("email", response.getEmail());
                editor.putString("username", response.getUsername());
                editor.putString("role", response.getRole());
                editor.apply();
            }

            @Override
            public void onError(String errorMessage) {
                //error.setValue(errorMessage);
            }

            @Override
            public void onFailure(String failureMessage) {
                //error.setValue(failureMessage);
            }
        });
    }

    public void moveProductToBuy(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        shoppingListRepository.changeProductStateInShopList(token, product.getId(), new ResponseCallback<ShoppingList>() {
            @Override
            public void onSuccess(ShoppingList shoppingList) {
                if (boughtProducts == null) {
                    boughtProducts = new MutableLiveData<>();
                    boughtProducts.setValue(new ArrayList<>());
                }
                if (toBuyProducts == null) {
                    toBuyProducts = new MutableLiveData<>();
                    toBuyProducts.setValue(new ArrayList<>());
                }
                boughtProducts.setValue(shoppingList.getBought());
                toBuyProducts.setValue(shoppingList.getToBuy());

            }

            @Override
            public void onError(String errorMessage) {
                boughtProductsOnErrorOperation.setValue(errorMessage);
            }

            @Override
            public void onFailure(String failureMessage) {
                boughtProductsOnErrorOperation.setValue(failureMessage);
            }
        });
    }

    public void moveProductToBought(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        shoppingListRepository.changeProductStateInShopList(token, product.getId(), new ResponseCallback<ShoppingList>() {
            @Override
            public void onSuccess(ShoppingList shoppingList) {
                if (boughtProducts == null) {
                    boughtProducts = new MutableLiveData<>();
                    boughtProducts.setValue(new ArrayList<>());
                }
                if (toBuyProducts == null) {
                    toBuyProducts = new MutableLiveData<>();
                    toBuyProducts.setValue(new ArrayList<>());
                }
                boughtProducts.setValue(shoppingList.getBought());
                toBuyProducts.setValue(shoppingList.getToBuy());

            }

            @Override
            public void onError(String errorMessage) {
                toBuyProductsOnErrorOperation.setValue(errorMessage);
            }

            @Override
            public void onFailure(String failureMessage) {
                toBuyProductsOnErrorOperation.setValue(failureMessage);
            }
        });
    }

    public void deleteProductFromList(Product product, String listType){
        String token = "Bearer " + prefs.getString("token", "");
        shoppingListRepository.deleteProductFromShopList(token, product.getId(), new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> shopList) {
                if (listType.equals("toBuy")) {
                    toBuyProducts.setValue(shopList);
                    toBuyProductsOnSuccessOperation.setValue("Produkt został usunięty z listy");
                }
                else if (listType.equals("bought")){
                    boughtProducts.setValue(shopList);
                    boughtProductsOnSuccessOperation.setValue("Produkt został usunięty z listy");
                }

            }

            @Override
            public void onError(String errorMessage) {
                if (listType.equals("toBuy")) {
                    toBuyProductsOnErrorOperation.setValue(errorMessage);
                }
                else if (listType.equals("bought")){
                    boughtProductsOnErrorOperation.setValue(errorMessage);
                }
            }

            @Override
            public void onFailure(String failureMessage) {
                if (listType.equals("toBuy")) {
                    toBuyProductsOnErrorOperation.setValue(failureMessage);
                }
                else if (listType.equals("bought")){
                    boughtProductsOnErrorOperation.setValue(failureMessage);
                }
            }
        });
    }

    public void changeProductAmount(Product product, String listType){
        String token = "Bearer " + prefs.getString("token", "");
        shoppingListRepository.changeProductAmountInShopList(token, product.getId(), product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> shopList) {
                if (listType.equals("toBuy")) {
                    toBuyProducts.setValue(shopList);
                }
                else if (listType.equals("bought")){
                    boughtProducts.setValue(shopList);
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (listType.equals("toBuy")) {
                    toBuyProductsOnErrorOperation.setValue(errorMessage);
                }
                else if (listType.equals("bought")){
                    boughtProductsOnErrorOperation.setValue(errorMessage);
                }
            }

            @Override
            public void onFailure(String failureMessage) {
                if (listType.equals("toBuy")) {
                    toBuyProductsOnErrorOperation.setValue(failureMessage);
                }
                else if (listType.equals("bought")){
                    boughtProductsOnErrorOperation.setValue(failureMessage);
                }
            }
        });
    }

    public void moveProductsToStorage(){
        ArrayList<Integer> productsIds = new ArrayList<>();
        ArrayList<Product> products = this.boughtProducts.getValue();
        if (products != null) {
            for (Product product : products) {
                productsIds.add(product.getId());
            }
        }

        String token = "Bearer " + prefs.getString("token", "");
        StorageRepository storageRepository = new StorageRepository();
        storageRepository.transferBoughtProductsToStorage(token, productsIds, new ResponseCallback<ArrayList<Product>>(){
            @Override
            public void onSuccess(ArrayList<Product> response) {
                boughtProductsOnSuccessOperation.setValue("Produkty zostały przeniesione do spiżarni");
                boughtProducts.setValue(response);
            }

            @Override
            public void onError(String errorMessage) {
                boughtProductsOnErrorOperation.setValue(errorMessage);
            }

            @Override
            public void onFailure(String failureMessage) {
                boughtProductsOnErrorOperation.setValue(failureMessage);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        shoppingListRepository.cancelCalls();
    }
}
