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
import pl.plantoplate.data.remote.models.ListType;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.data.remote.models.UserInfo;
import pl.plantoplate.data.remote.repository.ShoppingListRepository;
import pl.plantoplate.data.remote.repository.UserRepository;

public class ToBuyProductsListViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable;
    private final SharedPreferences prefs;
    private final ShoppingListRepository shoppingListRepository;

    private final MutableLiveData<String> responseMessage;
    private final MutableLiveData<ArrayList<Product>> toBuyProducts;
    private final MutableLiveData<UserInfo> userInfo;

    public ToBuyProductsListViewModel(@NonNull Application application) {
        super(application);

        prefs = application.getSharedPreferences("prefs", 0);
        shoppingListRepository = new ShoppingListRepository();
        compositeDisposable = new CompositeDisposable();

        responseMessage = new MutableLiveData<>();
        toBuyProducts = new MutableLiveData<>(new ArrayList<>());
        userInfo = new MutableLiveData<>();
    }

    public MutableLiveData<String> getResponseMessage(){
        return responseMessage;
    }

    public MutableLiveData<ArrayList<Product>> getToBuyProducts(){
        return toBuyProducts;
    }

    public MutableLiveData<UserInfo> getUserInfo(){
        return userInfo;
    }

    public void fetchToBuyProducts() {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.getToBuyShoppingList(token)
                .subscribe(toBuyProducts::setValue, throwable -> responseMessage.setValue(throwable.getMessage()));

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
                }, throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void moveProductToBought(Product product){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.changeProductStateInShopList(token, product.getId())
                .subscribe(shoppingList -> {
                    EventBus.getDefault().post(new ProductsListChangedEvent(shoppingList.getBought(), ListType.BOUGHT));
                    toBuyProducts.setValue(shoppingList.getToBuy());
                }, throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void deleteProductFromList(Product product){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.deleteProductFromShoppingList(token, product.getId())
                .subscribe(shoppingList -> {
                    toBuyProducts.setValue(shoppingList);
                    responseMessage.setValue("Produkt został usunięty z listy");

                }, throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void changeProductAmount(Product product){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.changeProductAmountInShopList(token, product.getId(), product)
                .subscribe(shoppingList -> {
                    System.out.println("SUCCESS - changeProductAmountInShopList");
                    toBuyProducts.setValue(shoppingList);
                    responseMessage.setValue("Ilość produktu została zmieniona");
                }, throwable -> {
                    System.out.println("ERROR: " + throwable.getMessage());
                    responseMessage.setValue(throwable.getMessage());
                });

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

}

