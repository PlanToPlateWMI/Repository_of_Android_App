package pl.plantoplate.data.remote.repository;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.data.remote.models.ShoppingList;
import pl.plantoplate.data.remote.service.ShoppingListService;
import retrofit2.HttpException;

public class ShoppingListRepository {
    private final ShoppingListService shoppingListService;

    public ShoppingListRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        shoppingListService = retrofitClient.getClient().create(ShoppingListService.class);
    }

    public Single<ArrayList<Product>> getToBuyShoppingList(String token) {
        return shoppingListService.getShoppingList(token, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> getBoughtShoppingList(String token) {
        return shoppingListService.getShoppingList(token, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Integer>> getBoughtProductsIds(String token) {
        return getBoughtShoppingList(token)
                .flatMap(response -> {
                    ArrayList<Integer> productIds = new ArrayList<>();
                    for (Product product : response) {
                        productIds.add(product.getId());
                    }
                    return Single.just(productIds);
                });
    }

    public Single<ArrayList<Product>> addProductToShoppingList(String token, Product product) {
        return shoppingListService.addProductToShopList(token, product)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> deleteProductFromShoppingList(String token, int productId) {
        return shoppingListService.deleteProductFromShopList(token, productId)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<Integer, String>() {{
                            put(404, "Nie znaleziono produktu.");
                            put(500, "Wystąpił nieznany błąd.");
                    }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ShoppingList> changeProductStateInShopList(String token, int productId) {
        return shoppingListService.changeProductStateInShopList(token, productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Product>> changeProductAmountInShopList(String token, int productId, Product product) {
        return shoppingListService.changeProductAmountInShopList(token, productId, product)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Product>>().
                        handleHttpError(throwable, new HashMap<Integer, String>() {{
                            put(400, "Niepoprawna ilość produktu.");
                            put(404, "Nie znaleziono produktu.");
                            put(500, "Wystąpił nieznany błąd.");
                    }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
