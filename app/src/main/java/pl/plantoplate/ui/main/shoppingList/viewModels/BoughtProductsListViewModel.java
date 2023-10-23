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
import pl.plantoplate.data.remote.ResponseCallback;
import pl.plantoplate.data.remote.models.ListType;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.data.remote.models.UserInfo;
import pl.plantoplate.data.remote.repository.ShoppingListRepository;
import pl.plantoplate.data.remote.repository.StorageRepository;
import pl.plantoplate.data.remote.repository.UserRepository;

public class BoughtProductsListViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable;
    private final SharedPreferences prefs;
    private final ShoppingListRepository shoppingListRepository;
    private MutableLiveData<String> responseMessage;
    private MutableLiveData<ArrayList<Product>> boughtProducts;
    private MutableLiveData<UserInfo> userInfo;

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

    public MutableLiveData<ArrayList<Product>> getBoughtProducts(){
        return boughtProducts;
    }

    public MutableLiveData<UserInfo> getUserInfo(){
        return userInfo;
    }

    public void fetchBoughtProducts() {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.getBoughtShoppingList(token)
                .subscribe(shoppingList -> {
                    boughtProducts.setValue(shoppingList);
                }, throwable -> {
                    responseMessage.setValue(throwable.getMessage());
                });

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
                }, throwable -> {
                    responseMessage.setValue(throwable.getMessage());
                });

        compositeDisposable.add(disposable);
    }

    public void moveProductToBuy(Product product){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.changeProductStateInShopList(token, product.getId())
                .subscribe(shoppingList -> {
                    EventBus.getDefault().post(new ProductsListChangedEvent(shoppingList.getToBuy(), ListType.TO_BUY));
                    boughtProducts.setValue(shoppingList.getBought());
                }, throwable -> {
                    responseMessage.setValue(throwable.getMessage());
                });

        compositeDisposable.add(disposable);
    }

    public void deleteProductFromList(Product product){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.deleteProductFromShoppingList(token, product.getId())
                .subscribe(shoppingList -> {
                    boughtProducts.setValue(shoppingList);
                    responseMessage.setValue("Produkt został usunięty z listy");

                }, throwable -> {
                    responseMessage.setValue(throwable.getMessage());
                });

        compositeDisposable.add(disposable);
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

        Disposable disposable = storageRepository.transferBoughtProductsToStorage(token, productsIds)
                .subscribe(productsList -> {
                    responseMessage.setValue("Produkty zostały przeniesione do spiżarni");
                    boughtProducts.setValue(productsList);
                }, throwable -> {
                    responseMessage.setValue(throwable.getMessage());
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

}
