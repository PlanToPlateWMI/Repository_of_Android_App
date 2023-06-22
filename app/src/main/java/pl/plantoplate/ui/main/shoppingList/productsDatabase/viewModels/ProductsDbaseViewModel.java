package pl.plantoplate.ui.main.shoppingList.productsDatabase.viewModels;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.models.UserInfo;
import pl.plantoplate.repository.remote.product.ProductRepository;
import pl.plantoplate.repository.remote.shoppingList.ShoppingListRepository;
import pl.plantoplate.repository.remote.storage.StorageRepository;
import pl.plantoplate.repository.remote.user.UserRepository;

public class ProductsDbaseViewModel extends AndroidViewModel {

    private SharedPreferences prefs;
    private ProductRepository productRepository;


    private MutableLiveData<String> allProductsOnSuccessOperation;
    private MutableLiveData<String> ownProductsOnSuccessOperation;
    private MutableLiveData<String> allProductsOnErrorOperation;
    private MutableLiveData<String> ownProductsOnErrorOperation;
    private MutableLiveData<ArrayList<Product>> allProducts;
    private MutableLiveData<ArrayList<Product>> ownProducts;
    private MutableLiveData<UserInfo> userInfo;

    public ProductsDbaseViewModel(@NonNull Application application) {
        super(application);

        prefs = application.getSharedPreferences("prefs", 0);

        productRepository = new ProductRepository();
    }

    public MutableLiveData<String> getAllProductsOnSuccessOperation() {
        if (allProductsOnSuccessOperation == null) {
            allProductsOnSuccessOperation = new MutableLiveData<>();
        }
        return allProductsOnSuccessOperation;
    }

    public MutableLiveData<String> getOwnProductsOnSuccessOperation() {
        if (ownProductsOnSuccessOperation == null) {
            ownProductsOnSuccessOperation = new MutableLiveData<>();
        }
        return ownProductsOnSuccessOperation;
    }

    public MutableLiveData<String> getAllProductsOnErrorOperation() {
        if (allProductsOnErrorOperation == null) {
            allProductsOnErrorOperation = new MutableLiveData<>();
        }
        return allProductsOnErrorOperation;
    }

    public MutableLiveData<String> getOwnProductsOnErrorOperation() {
        if (ownProductsOnErrorOperation == null) {
            ownProductsOnErrorOperation = new MutableLiveData<>();
        }
        return ownProductsOnErrorOperation;
    }

    public MutableLiveData<ArrayList<Product>> getAllProducts() {
        if (allProducts == null) {
            allProducts = new MutableLiveData<>();
            fetchAllProducts();
        }
        return allProducts;
    }

    public MutableLiveData<ArrayList<Product>> getOwnProducts() {
        if (ownProducts == null) {
            ownProducts = new MutableLiveData<>();
            fetchOwnProducts();
        }
        return ownProducts;
    }

    public MutableLiveData<UserInfo> getUserInfo() {
        if (userInfo == null) {
            userInfo = new MutableLiveData<>();
            fetchUserInfo();
        }
        return userInfo;
    }

    public void fetchAllProducts(){
        String token = "Bearer " + prefs.getString("token", "");
        productRepository.getAllProducts(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> productsDataBase) {
                allProducts.setValue(productsDataBase);
            }

            @Override
            public void onError(String errorMessage) {
                //allProductsOnErrorOperation.setValue(errorMessage);
                Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                //allProductsOnErrorOperation.setValue(failureMessage);
                Toast.makeText(getApplication(), failureMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchOwnProducts(){
        String token = "Bearer " + prefs.getString("token", "");
        productRepository.getOwnProducts(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                ownProducts.setValue(products);
            }

            @Override
            public void onError(String errorMessage) {
                //ownProductsOnErrorOperation.setValue(errorMessage);
                Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                //ownProductsOnErrorOperation.setValue(failureMessage);
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

    public void addProductToShoppingList(Product product, String listType) {
        String token = "Bearer " + prefs.getString("token", "");
        ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
        shoppingListRepository.addProductToShopList(token, product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                String message = "Produkt " + product.getName() + " w ilości " +
                        product.getAmount() + " " + product.getUnit() + " został dodany do listy zakupów";

//                if (listType.equals("own")) {
//                    ownProductsOnSuccessOperation.setValue(message);
//                } else {
//                    allProductsOnSuccessOperation.setValue(message);
//                }

                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
//                if (listType.equals("own")) {
//                    ownProductsOnErrorOperation.setValue(errorMessage);
//                } else {
//                    allProductsOnErrorOperation.setValue(errorMessage);
//                }

                Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String failureMessage) {
//                if (listType.equals("own")) {
//                    ownProductsOnErrorOperation.setValue(failureMessage);
//                } else {
//                    allProductsOnErrorOperation.setValue(failureMessage);
//                }

                    Toast.makeText(getApplication(), failureMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addProductToStorage(Product product, String listType){
        String token = "Bearer " + prefs.getString("token", "");
        StorageRepository storageRepository = new StorageRepository();
        storageRepository.addProductToStorage(token, product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                String message = "Produkt " + product.getName() + " w ilości "
                        + product.getAmount() + " " + product.getUnit() + " został dodany do spiżarni";
//                if (listType.equals("own")) {
//                    ownProductsOnSuccessOperation.setValue(message);
//                } else {
//                    allProductsOnSuccessOperation.setValue(message);
//                }
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
//                if (listType.equals("own")) {
//                    ownProductsOnErrorOperation.setValue(errorMessage);
//                } else {
//                    allProductsOnErrorOperation.setValue(errorMessage);
//                }
                Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String failureMessage) {
//                if (listType.equals("own")) {
//                    ownProductsOnErrorOperation.setValue(failureMessage);
//                } else {
//                    allProductsOnErrorOperation.setValue(failureMessage);
//                }
                Toast.makeText(getApplication(), failureMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteOwnProductFromList(Product product, String listType){
        String token = "Bearer " + prefs.getString("token", "");
        productRepository.deleteProduct(token, product.getId(), new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> shopList) {
                String message = "Produkt " + product.getName() + " został usunięty z listy";
//                if (listType.equals("own")) {
//                    ownProductsOnSuccessOperation.setValue(message);
//                } else {
//                    allProductsOnSuccessOperation.setValue(message);
//                }
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(String errorMessage) {
//                if (listType.equals("own")) {
//                    ownProductsOnErrorOperation.setValue(errorMessage);
//                } else {
//                    allProductsOnErrorOperation.setValue(errorMessage);
//                }
                Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String failureMessage) {
//                if (listType.equals("own")) {
//                    ownProductsOnErrorOperation.setValue(failureMessage);
//                } else {
//                    allProductsOnErrorOperation.setValue(failureMessage);
//                }
                Toast.makeText(getApplication(), failureMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        productRepository.cancelCalls();
    }
}
