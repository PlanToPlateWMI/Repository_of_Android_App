package pl.plantoplate.requests.shoppingList;

import java.util.ArrayList;

/**
 * Interface, that is used, to setting shop list from GetShopListCallback to ShoppingListFragment
 * after succesfull response in GetShopListCallback.
 */
public interface ShopListCallback {

    void setShoppingList(ArrayList<Product> shopList);
}
