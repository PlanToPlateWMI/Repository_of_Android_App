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
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.data.remote.models.UserInfo;
import pl.plantoplate.data.remote.repository.ProductRepository;
import pl.plantoplate.data.remote.repository.ShoppingListRepository;
import pl.plantoplate.data.remote.repository.StorageRepository;
import pl.plantoplate.data.remote.repository.UserRepository;

public class ProductsDbaseViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable;
    private final SharedPreferences prefs;
    private final ProductRepository productRepository;
    private final MutableLiveData<ArrayList<Product>> allProducts;
    private final MutableLiveData<ArrayList<Product>> ownProducts;
    private final MutableLiveData<UserInfo> userInfo;

    public ProductsDbaseViewModel(@NonNull Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
        prefs = application.getSharedPreferences("prefs", 0);
        productRepository = new ProductRepository();

        allProducts = new MutableLiveData<>(new ArrayList<>());
        ownProducts = new MutableLiveData<>(new ArrayList<>());
        userInfo = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Product>> getAllProducts() {
        return allProducts;
    }

    public MutableLiveData<ArrayList<Product>> getOwnProducts() {
        return ownProducts;
    }

    public MutableLiveData<UserInfo> getUserInfo() {
        return userInfo;
    }

    public void fetchAllProducts(){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = productRepository.getAllProducts(token)
                .subscribe(allProducts::setValue, throwable -> {
                    Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });

        compositeDisposable.add(disposable);
    }

    public void fetchOwnProducts(){
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = productRepository.getOwnProducts(token)
                .subscribe(ownProducts::setValue, throwable -> {
                    Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });

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
                }, throwable -> {
                    Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });

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
                }, throwable -> {
                    Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
