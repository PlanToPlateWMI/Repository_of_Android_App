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
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import pl.plantoplate.R;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.models.Category;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.models.UserInfo;
import pl.plantoplate.repository.remote.storage.StorageRepository;
import pl.plantoplate.repository.remote.user.UserRepository;
import pl.plantoplate.tools.CategorySorter;

public class StorageViewModel extends AndroidViewModel {

    private SharedPreferences prefs;
    private StorageRepository storageRepository;
    private Context context;

    private MutableLiveData<String> success;
    private MutableLiveData<String> error;
    private MutableLiveData<String> storageTitle;
    private MutableLiveData<ArrayList<Category>> storageProducts;
    private MutableLiveData<UserInfo> userInfo;

    /**
     * Constructs a new StorageViewModel object.
     *
     * @param application The application context.
     */
    public StorageViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences("prefs", 0);
        context = application.getApplicationContext();

        storageRepository = new StorageRepository();
    }

    /**
     * Returns the MutableLiveData object that holds the storage title.
     *
     * @return The MutableLiveData object for the storage title.
     */
    public MutableLiveData<String> getStorageTitle() {
        if (storageTitle == null) {
            storageTitle = new MutableLiveData<>();
        }
        return storageTitle;
    }

    /**
     * Returns the MutableLiveData object that holds the success message.
     *
     * @return The MutableLiveData object for the success message.
     */
    public MutableLiveData<String> getSuccess() {
        if (success == null) {
            success = new MutableLiveData<>();
        }
        return success;
    }

    /**
     * Returns the MutableLiveData object that holds the error message.
     *
     * @return The MutableLiveData object for the error message.
     */
    public MutableLiveData<String> getError() {
        if (error == null) {
            error = new MutableLiveData<>();
        }
        return error;
    }

    /**
     * Returns the MutableLiveData object that holds the list of storage products.
     * If the storageProducts object is null, it creates a new instance and fetches the storage products.
     *
     * @return The MutableLiveData object for the list of storage products.
     */
    public MutableLiveData<ArrayList<Category>> getStorageProducts() {
        if (storageProducts == null) {
            storageProducts = new MutableLiveData<>();
            fetchStorageProducts();
        }
        return storageProducts;
    }

    /**
     * Returns the MutableLiveData object that holds the user info.
     * If the userInfo object is null, it creates a new instance and fetches the user info.
     *
     * @return The MutableLiveData object for the user info.
     */
    public MutableLiveData<UserInfo> getUserInfo() {
        if (userInfo == null) {
            userInfo = new MutableLiveData<>();
            fetchUserInfo();
        }
        return userInfo;
    }

    /**
     * Fetches the storage products from the storage repository.
     * It uses the token from shared preferences to authenticate the request.
     * Handles the success, error, and failure cases by updating the corresponding MutableLiveData objects.
     */
    public void fetchStorageProducts(){
        String token = "Bearer " + prefs.getString("token", "");
        storageRepository.getStorage(token, new ResponseCallback<ArrayList<Product>>() {

            /**
             * Handles the success case of fetching storage products from the storage repository.
             * Updates the storageProducts MutableLiveData object with the sorted categories by product.
             * Sets the storageTitle value based on the presence of products in the storage.
             *
             * @param products The list of products fetched from the storage repository
             */
            @Override
            public void onSuccess(ArrayList<Product> products) {
                storageProducts.setValue(CategorySorter.sortCategoriesByProduct(products));

                if (products.isEmpty()) {
                    storageTitle.setValue(context.getString(R.string.wprowadzenie_spizarnia));
                } else {
                    storageTitle.setValue("");
                }
            }

            /**
             * Handles the error case of fetching storage products from the storage repository.
             * Updates the error MutableLiveData object with the provided error message.
             *
             * @param errorMessage The error message received from the storage repository
             */
            @Override
            public void onError(String errorMessage) {
                //error.setValue(errorMessage);
                Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            /**
             * Handles the failure case of fetching storage products from the storage repository.
             * Updates the error MutableLiveData object with the provided failure message.
             *
             * @param failureMessage The failure message received from the storage repository
             */
            @Override
            public void onFailure(String failureMessage) {
                //error.setValue(failureMessage);
                Toast.makeText(getApplication(), failureMessage, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                //error.setValue(failureMessage);
                Toast.makeText(getApplication(), failureMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Deletes a product from the storage.
     *
     * @param product The product to be deleted
     */
    public void deleteProductFromStorage(Product product) {
        String token = "Bearer " + prefs.getString("token", "");
        storageRepository.deleteProductStorage(token, product.getId(), new ResponseCallback<ArrayList<Product>>() {

            /**
             * Callback method called when the product is successfully deleted from the storage.
             *
             * @param products The updated list of products in the storage
             */
            @Override
            public void onSuccess(ArrayList<Product> products) {
                storageProducts.setValue(CategorySorter.sortCategoriesByProduct(products));
                //success.setValue("produkt '" + product.getName() + "' został usunięty");
                Toast.makeText(getApplication(), "produkt '" + product.getName() + "' został usunięty", Toast.LENGTH_SHORT).show();

                if (products.isEmpty()) {
                    storageTitle.setValue("Spiżarnia");
                } else {
                    storageTitle.setValue("");
                }
            }

            /**
             * Callback method called when an error occurs while deleting the product from the storage.
             *
             * @param errorMessage The error message describing the cause of the error
             */
            @Override
            public void onError(String errorMessage) {
                //error.setValue(errorMessage);
                Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            /**
             * Callback method called when the deletion of the product from the storage fails.
             *
             * @param failureMessage The failure message describing the reason for the failure
             */
            @Override
            public void onFailure(String failureMessage) {
                //error.setValue(failureMessage);
                Toast.makeText(getApplication(), failureMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Changes the amount of a product in the storage.
     *
     * @param product The product to be updated.
     */
    public void changeProductAmount(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        storageRepository.changeProductAmountInStorage(token, product.getId(), product, new ResponseCallback<ArrayList<Product>>() {

            /**
             * Changes the amount of a product in the storage.
             *
             * @param products The product to be updated.
             */
            @Override
            public void onSuccess(ArrayList<Product> products) {
                storageProducts.setValue(CategorySorter.sortCategoriesByProduct(products));
                //success.setValue("produkt '" + product.getName() + "' został zmieniony");
                Toast.makeText(getApplication(), "produkt '" + product.getName() + "' został zmieniony", Toast.LENGTH_SHORT).show();

            }

            /**
             * Called when an error occurs during the product change operation.
             *
             * @param errorMessage The error message describing the encountered error.
             */
            @Override
            public void onError(String errorMessage) {
                //error.setValue(errorMessage);
                Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            /**
             * Called when the product change operation fails.
             *
             * @param failureMessage The failure message describing the reason for the failure.
             */
            @Override
            public void onFailure(String failureMessage) {
                //error.setValue(failureMessage);
                Toast.makeText(getApplication(), failureMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Moves the specified products to the storage.
     *
     * @param productsIds The list of product IDs to be moved to the storage.
     */
    public void moveProductsToStorage(ArrayList<Integer> productsIds){
        String token = "Bearer " + prefs.getString("token", "");
        storageRepository.transferBoughtProductsToStorage(token, productsIds, new ResponseCallback<ArrayList<Product>>(){

            /**
             * Called when the products have been successfully moved to the storage.
             *
             * @param products The list of products that have been successfully moved to the storage.
             */
            @Override
            public void onSuccess(ArrayList<Product> products) {
                storageProducts.setValue(CategorySorter.sortCategoriesByProduct(products));
                //success.setValue("Produkty zostały przeniesione do spiżarni");
                Toast.makeText(getApplication(), "Produkty zostały przeniesione do spiżarni", Toast.LENGTH_SHORT).show();

                if (products.isEmpty()) {
                    storageTitle.setValue("Spiżarnia");
                } else {
                    storageTitle.setValue("");
                }
            }

            /**
             * Called when an error occurs during the move of products to the storage.
             *
             * @param errorMessage The error message indicating the cause of the error.
             */
            @Override
            public void onError(String errorMessage) {
                //error.setValue(errorMessage);
                Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            /**
             * Called when the move of products to the storage fails.
             *
             * @param failureMessage The failure message indicating the reason for the failure.
             */
            @Override
            public void onFailure(String failureMessage) {
                //error.setValue(failureMessage);
                Toast.makeText(getApplication(), failureMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Called when the ViewModel is no longer used and will be destroyed.
     * Cancels any ongoing API calls or operations related to the storage repository.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        storageRepository.cancelCalls();
    }
}
