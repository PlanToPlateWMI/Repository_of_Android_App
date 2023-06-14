package pl.plantoplate.ui.main.storage;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.models.Category;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.storage.StorageRepository;
import pl.plantoplate.tools.CategorySorter;

public class StorageViewModel extends AndroidViewModel {

    private SharedPreferences prefs;
    private StorageRepository storageRepository;

    private MutableLiveData<String> success;
    private MutableLiveData<String> error;
    private MutableLiveData<String> storageTitle;
    private MutableLiveData<ArrayList<Category>> storageProducts;

    public StorageViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences("prefs", 0);

        storageRepository = new StorageRepository();
    }

    public MutableLiveData<String> getStorageTitle() {
        if (storageTitle == null) {
            storageTitle = new MutableLiveData<>();
        }
        return storageTitle;
    }

    public MutableLiveData<String> getSuccess() {
        if (success == null) {
            success = new MutableLiveData<>();
        }
        return success;
    }

    public MutableLiveData<String> getError() {
        if (error == null) {
            error = new MutableLiveData<>();
        }
        return error;
    }

    public MutableLiveData<ArrayList<Category>> getStorageProducts() {
        if (storageProducts == null) {
            storageProducts = new MutableLiveData<>();
            fetchStorageProducts();
        }
        return storageProducts;
    }

    public void fetchStorageProducts(){
        String token = "Bearer " + prefs.getString("token", "");
        storageRepository.getStorage(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                storageProducts.setValue(CategorySorter.sortCategoriesByProduct(products));

                if (products.isEmpty()) {
                    storageTitle.setValue("Spiżarnia");
                } else {
                    storageTitle.setValue("");
                }
            }

            @Override
            public void onError(String errorMessage) {
                error.setValue(errorMessage);
            }

            @Override
            public void onFailure(String failureMessage) {
                error.setValue(failureMessage);
            }
        });
    }

    public void deleteProductFromStorage(Product product) {
        String token = "Bearer " + prefs.getString("token", "");
        storageRepository.deleteProductStorage(token, product.getId(), new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                storageProducts.setValue(CategorySorter.sortCategoriesByProduct(products));
                success.setValue("produkt '" + product.getName() + "' został usunięty");

                if (products.isEmpty()) {
                    storageTitle.setValue("Spiżarnia");
                } else {
                    storageTitle.setValue("");
                }
            }

            @Override
            public void onError(String errorMessage) {
                error.setValue(errorMessage);
            }

            @Override
            public void onFailure(String failureMessage) {
                error.setValue(failureMessage);
            }
        });
    }

    public void changeProductAmount(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        storageRepository.changeProductAmountInStorage(token, product.getId(), product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                storageProducts.setValue(CategorySorter.sortCategoriesByProduct(products));
                success.setValue("produkt '" + product.getName() + "' został zmieniony");

            }
            @Override
            public void onError(String errorMessage) {
                error.setValue(errorMessage);
            }

            @Override
            public void onFailure(String failureMessage) {
                error.setValue(failureMessage);
            }
        });
    }

    public void moveProductsToStorage(ArrayList<Integer> productsIds){
        String token = "Bearer " + prefs.getString("token", "");
        storageRepository.transferBoughtProductsToStorage(token, productsIds, new ResponseCallback<ArrayList<Product>>(){
            @Override
            public void onSuccess(ArrayList<Product> products) {
                storageProducts.setValue(CategorySorter.sortCategoriesByProduct(products));
                success.setValue("Produkty zostały przeniesione do spiżarni");

                if (products.isEmpty()) {
                    storageTitle.setValue("Spiżarnia");
                } else {
                    storageTitle.setValue("");
                }
            }

            @Override
            public void onError(String errorMessage) {
                error.setValue(errorMessage);
            }

            @Override
            public void onFailure(String failureMessage) {
                error.setValue(failureMessage);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        storageRepository.cancelCalls();
    }
}
