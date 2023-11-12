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
package pl.plantoplate.ui.main.productsDatabase.viewModels;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.product.ProductCategory;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.user.UserInfo;
import pl.plantoplate.data.remote.repository.ProductRepository;
import pl.plantoplate.data.remote.repository.ShoppingListRepository;
import pl.plantoplate.data.remote.repository.StorageRepository;
import pl.plantoplate.tools.CategorySorter;

public class AllProductsViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable;
    private final SharedPreferences prefs;
    private final ProductRepository productRepository;
    private final MutableLiveData<ArrayList<ProductCategory>> allProducts;
    private final MutableLiveData<UserInfo> userInfo;

    public AllProductsViewModel(@NonNull Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
        prefs = application.getSharedPreferences("prefs", 0);
        productRepository = new ProductRepository();

        allProducts = new MutableLiveData<>();
        userInfo = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<ProductCategory>> getAllProducts() {
        return allProducts;
    }

    public MutableLiveData<UserInfo> getUserInfo() {
        return userInfo;
    }

    public void fetchAllProducts(){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = productRepository.getAllProducts(token)
                .subscribe(allProducts ->
                                this.allProducts.setValue(CategorySorter.sortCategoriesByProduct(allProducts)),
                        throwable ->
                                Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_SHORT).show());

        compositeDisposable.add(disposable);
    }

    public void addProductToShoppingList(Product product) {
        String token = "Bearer " + prefs.getString("token", "");
        ShoppingListRepository shoppingListRepository = new ShoppingListRepository();

        Disposable disposable = shoppingListRepository.addProductToShoppingList(token, product)
                .subscribe(shoppingList -> {
                    String message = "Produkt " + product.getName() + " w ilości " +
                            product.getAmount() + " " + product.getUnit() + " został dodany do listy zakupów";

                    Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                }, throwable ->
                        Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_SHORT).show());

        compositeDisposable.add(disposable);
    }

    public void addProductToStorage(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        StorageRepository storageRepository = new StorageRepository();

        Disposable disposable = storageRepository.addProductToStorage(token, product)
                .subscribe(storage -> {
                    String message = "Produkt " + product.getName() + " w ilości "
                            + product.getAmount() + " " + product.getUnit() + " został dodany do spiżarni";
                    Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                }, throwable ->
                        Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_SHORT).show());

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}