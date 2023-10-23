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

package pl.plantoplate.ui.main.storage;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.Category;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.data.remote.models.UserInfo;
import pl.plantoplate.data.remote.repository.StorageRepository;
import pl.plantoplate.data.remote.repository.UserRepository;
import pl.plantoplate.tools.CategorySorter;

public class StorageViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable;
    private final SharedPreferences prefs;
    private final StorageRepository storageRepository;
    private final MutableLiveData<String> responseMessage;
    private final MutableLiveData<ArrayList<Category>> storageProducts;
    private final MutableLiveData<UserInfo> userInfo;

    /**
     * Constructs a new StorageViewModel object.
     *
     * @param application The application context.
     */
    public StorageViewModel(@NonNull Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
        prefs = application.getSharedPreferences("prefs", 0);
        storageRepository = new StorageRepository();

        responseMessage = new MutableLiveData<>();
        storageProducts = new MutableLiveData<>();
        userInfo = new MutableLiveData<>();
    }

    /**
     * Returns the MutableLiveData object that holds the success message.
     *
     * @return The MutableLiveData object for the success message.
     */
    public MutableLiveData<String> getResponseMessage() {
        return responseMessage;
    }

    /**
     * Returns the MutableLiveData object that holds the list of storage products.
     * If the storageProducts object is null, it creates a new instance and fetches the storage products.
     *
     * @return The MutableLiveData object for the list of storage products.
     */
    public MutableLiveData<ArrayList<Category>> getStorageProducts() {
        return storageProducts;
    }

    /**
     * Returns the MutableLiveData object that holds the user info.
     * If the userInfo object is null, it creates a new instance and fetches the user info.
     *
     * @return The MutableLiveData object for the user info.
     */
    public MutableLiveData<UserInfo> getUserInfo() {
        return userInfo;
    }

    /**
     * Fetches the storage products from the storage repository.
     * It uses the token from shared preferences to authenticate the request.
     * Handles the success, error, and failure cases by updating the corresponding MutableLiveData objects.
     */
    public void fetchStorageProducts(){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = storageRepository.getStorage(token)
                .subscribe(response -> storageProducts.setValue(CategorySorter.sortCategoriesByProduct(response)),
                           throwable -> responseMessage.setValue(throwable.getMessage())
                );

        compositeDisposable.add(disposable);

    }

    public void fetchUserInfo() {
        UserRepository userRepository = new UserRepository();
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = userRepository.getUserInfo(token)
                .subscribe(response -> {
                        userInfo.setValue(response);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("email", response.getEmail());
                        editor.putString("username", response.getUsername());
                        editor.putString("role", response.getRole());
                        editor.apply();
                    },
                    throwable -> responseMessage.setValue(throwable.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    /**
     * Deletes a product from the storage.
     *
     * @param product The product to be deleted
     */
    public void deleteProductFromStorage(Product product) {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = storageRepository.deleteProductFromStorage(token, product.getId())
                .subscribe(response -> {
                            storageProducts.setValue(CategorySorter.sortCategoriesByProduct(response));
                            responseMessage.setValue("produkt '" + product.getName() + "' został usunięty");
                        },
                        throwable -> responseMessage.setValue(throwable.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    /**
     * Changes the amount of a product in the storage.
     *
     * @param product The product to be updated.
     */
    public void changeProductAmount(Product product){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = storageRepository.changeProductAmountInStorage(token, product.getId(), product)
                .subscribe(response -> {
                            storageProducts.setValue(CategorySorter.sortCategoriesByProduct(response));
                            responseMessage.setValue("produkt '" + product.getName() + "' został zmieniony");
                        },
                        throwable -> responseMessage.setValue(throwable.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    /**
     * Moves the specified products to the storage.
     *
     * @param productsIds The list of product IDs to be moved to the storage.
     */
    public void moveProductsToStorage(ArrayList<Integer> productsIds){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = storageRepository.transferBoughtProductsToStorage(token, productsIds)
                .subscribe(response -> {
                            storageProducts.setValue(CategorySorter.sortCategoriesByProduct(response));
                            responseMessage.setValue("Produkty zostały przeniesione do spiżarni");
                        },
                        throwable -> responseMessage.setValue(throwable.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    /**
     * Called when the ViewModel is no longer used and will be destroyed.
     * Cancels any ongoing API calls or operations related to the storage repository.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
