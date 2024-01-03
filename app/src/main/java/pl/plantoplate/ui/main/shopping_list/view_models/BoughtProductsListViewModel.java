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
package pl.plantoplate.ui.main.shopping_list.view_models;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.shopping_list.ListType;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.user.UserInfo;
import pl.plantoplate.data.remote.repository.ShoppingListRepository;
import pl.plantoplate.data.remote.repository.StorageRepository;
import pl.plantoplate.data.remote.repository.UserRepository;
import pl.plantoplate.ui.main.shopping_list.events.ProductsListChangedEvent;

/**
 * ViewModel for bought products list
 */
public class BoughtProductsListViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable;
    private final SharedPreferences prefs;
    private final ShoppingListRepository shoppingListRepository;
    private final MutableLiveData<String> responseMessage;
    private final MutableLiveData<List<Product>> boughtProducts;
    private final MutableLiveData<UserInfo> userInfo;

    /**
     * Constructor
     *
     * @param application
     */
    public BoughtProductsListViewModel(@NonNull Application application) {
        super(application);

        compositeDisposable = new CompositeDisposable();
        prefs = application.getSharedPreferences("prefs", 0);
        shoppingListRepository = new ShoppingListRepository();

        responseMessage = new MutableLiveData<>();
        boughtProducts = new MutableLiveData<>();
        userInfo = new MutableLiveData<>();

    }

    public MutableLiveData<String> getResponseMessage(){
        return responseMessage;
    }

    public MutableLiveData<List<Product>> getBoughtProducts(){
        return boughtProducts;
    }

    public MutableLiveData<UserInfo> getUserInfo(){
        return userInfo;
    }

    /**
     * Fetches bought products from API
     */
    public void fetchBoughtProducts() {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.getBoughtShoppingList(token)
                .subscribe(boughtProducts::setValue, throwable ->
                        responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    /**
     * Fetches user info from API
     */
    public void fetchUserInfo() {
        UserRepository userRepository = new UserRepository();
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = userRepository.getUserInfo(token)
                .subscribe(info -> {
                    this.userInfo.setValue(info);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("email", info.getEmail());
                    editor.putString("username", info.getUsername());
                    editor.putString("role", info.getRole());
                    editor.apply();
                }, throwable ->
                        responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    /**
     * Moves product from bought list to toBuy list
     *
     * @param product
     */
    public void moveProductToBuy(Product product){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.changeProductStateInShopList(token, product.getId())
                .subscribe(shoppingList -> {
                    EventBus.getDefault().post(new ProductsListChangedEvent(shoppingList.getToBuy(), ListType.TO_BUY));
                    boughtProducts.setValue(shoppingList.getBought());
                }, throwable ->
                        responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    /**
     * Deletes product from bought list
     *
     * @param product
     */
    public void deleteProductFromList(Product product){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.deleteProductFromShoppingList(token, product.getId())
                .subscribe(shoppingList -> {
                    boughtProducts.setValue(shoppingList);
                    responseMessage.setValue("Produkt został usunięty z listy");

                }, throwable ->
                        responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    /**
     * Moves all products from bought list to storage
     */
    public void moveProductsToStorage(){
        List<Integer> productsIds = new ArrayList<>();
        List<Product> products = this.boughtProducts.getValue();
        if (products != null) {
            for (Product product : products) {
                productsIds.add(product.getId());
            }
        }

        String token = "Bearer " + prefs.getString("token", "");
        StorageRepository storageRepository = new StorageRepository();

        Disposable disposable = storageRepository.transferBoughtProductsToStorage(token, productsIds)
                .subscribe(productsList -> {
                    responseMessage.setValue("Produkty zostały przeniesione do spiżarni");
                    boughtProducts.setValue(productsList);
                }, throwable ->
                        responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    /**
     * Clears compositeDisposable
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
