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
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.product.ProductCategory;
import pl.plantoplate.data.remote.models.shoppingList.ListType;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.user.UserInfo;
import pl.plantoplate.data.remote.repository.ShoppingListRepository;
import pl.plantoplate.data.remote.repository.UserRepository;
import pl.plantoplate.utils.CategorySorter;

public class ToBuyProductsListViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable;
    private final SharedPreferences prefs;
    private final ShoppingListRepository shoppingListRepository;

    private final MutableLiveData<String> responseMessage;
    private final MutableLiveData<ArrayList<ProductCategory>> toBuyProducts;
    private final MutableLiveData<UserInfo> userInfo;

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

    public MutableLiveData<ArrayList<ProductCategory>> getToBuyProducts() {
        return toBuyProducts;
    }

    public MutableLiveData<UserInfo> getUserInfo() {
        return userInfo;
    }

    public void fetchToBuyProducts() {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.getToBuyShoppingList(token)
                .subscribe(response -> toBuyProducts.setValue(CategorySorter.sortCategoriesByProduct(response)),
                        throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void fetchUserInfo() {
        UserRepository userRepository = new UserRepository();
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = userRepository.getUserInfo(token)
                .subscribe(userInfo -> {
                    this.userInfo.setValue(userInfo);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("email", userInfo.getEmail());
                    editor.putString("username", userInfo.getUsername());
                    editor.putString("role", userInfo.getRole());
                    editor.apply();
                }, throwable ->
                        responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void moveProductToBought(Product product) {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.changeProductStateInShopList(token, product.getId())
                .subscribe(shoppingList -> {
                    EventBus.getDefault().post(new ProductsListChangedEvent(shoppingList.getBought(), ListType.BOUGHT));
                    toBuyProducts.setValue(CategorySorter.sortCategoriesByProduct(shoppingList.getToBuy()));
                }, throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void deleteProductFromList(Product product) {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.deleteProductFromShoppingList(token, product.getId())
                .subscribe(products -> {
                    toBuyProducts.setValue(CategorySorter.sortCategoriesByProduct(products));
                    responseMessage.setValue("Produkt został usunięty z listy");

                }, throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void changeProductAmount(Product product) {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.changeProductAmountInShopList(token, product.getId(), product)
                .subscribe(products -> {
                    toBuyProducts.setValue(CategorySorter.sortCategoriesByProduct(products));
                    responseMessage.setValue("Ilość produktu została zmieniona");
                }, throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}