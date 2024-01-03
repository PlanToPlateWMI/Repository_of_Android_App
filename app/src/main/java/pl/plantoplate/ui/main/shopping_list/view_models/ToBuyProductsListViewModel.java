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

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.product.ProductCategory;
import pl.plantoplate.data.remote.models.shopping_list.ListType;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.user.UserInfo;
import pl.plantoplate.data.remote.repository.ShoppingListRepository;
import pl.plantoplate.data.remote.repository.UserRepository;
import pl.plantoplate.ui.main.shopping_list.events.ProductBoughtEvent;
import pl.plantoplate.ui.main.shopping_list.events.ProductsListChangedEvent;
import pl.plantoplate.utils.CategorySorter;

/**
 * This class is used to notify the view that the list of products has changed.
 */
public class ToBuyProductsListViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable;
    private final SharedPreferences prefs;
    private final ShoppingListRepository shoppingListRepository;
    private final MutableLiveData<String> responseMessage;
    private final MutableLiveData<List<ProductCategory>> toBuyProducts;
    private final MutableLiveData<UserInfo> userInfo;


    /**
     * @param application the application
     */
    public ToBuyProductsListViewModel(@NonNull Application application) {
        super(application);

        prefs = application.getSharedPreferences("prefs", 0);
        shoppingListRepository = new ShoppingListRepository();
        compositeDisposable = new CompositeDisposable();

        responseMessage = new MutableLiveData<>();
        toBuyProducts = new MutableLiveData<>();
        userInfo = new MutableLiveData<>();
    }

    public MutableLiveData<String> getResponseMessage() {
        return responseMessage;
    }

    public MutableLiveData<List<ProductCategory>> getToBuyProducts() {
        return toBuyProducts;
    }

    public MutableLiveData<UserInfo> getUserInfo() {
        return userInfo;
    }


    /**
     * Fetches the list of products to buy from the server.
     */
    public void fetchToBuyProducts() {
        String token = String.format("Bearer %s", prefs.getString("token", ""));

        Disposable disposable = shoppingListRepository.getToBuyShoppingList(token)
                .subscribe(response -> toBuyProducts.setValue(CategorySorter.sortCategoriesByProduct(response)),
                        throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }


    /**
     * Fetches the user info from the server.
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
     * Moves the product to the bought list.
     *
     * @param product the product to move
     */
    public void moveProductToBought(Product product) {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.changeProductStateInShopList(token, product.getId())
                .subscribe(shoppingList -> {
                    EventBus.getDefault().post(new ProductsListChangedEvent(shoppingList.getBought(), ListType.BOUGHT));
                    EventBus.getDefault().post(new ProductBoughtEvent(shoppingList.getBought().size()));
                    toBuyProducts.setValue(CategorySorter.sortCategoriesByProduct(shoppingList.getToBuy()));
                }, throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }


    /**
     * Deletes the product from the list.
     *
     * @param product the product to delete
     */
    public void deleteProductFromList(Product product) {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.deleteProductFromShoppingList(token, product.getId())
                .subscribe(products -> {
                    toBuyProducts.setValue(CategorySorter.sortCategoriesByProduct(products));
                    responseMessage.setValue("Produkt został usunięty z listy");

                }, throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }


    /**
     * Changes the amount of the product.
     *
     * @param product the product to change
     */
    public void changeProductAmount(Product product) {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.changeProductAmountInShopList(token, product.getId(), product)
                .subscribe(products -> {
                    toBuyProducts.setValue(CategorySorter.sortCategoriesByProduct(products));
                    responseMessage.setValue("Ilość produktu została zmieniona");
                }, throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }


    /**
     * Clears the list of products.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}