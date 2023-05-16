package pl.plantoplate.ui.main.shoppingList.listAdapters;

import android.view.View;

import pl.plantoplate.requests.products.Product;

public interface OnProductItemClickListener {
    default void onAddToShoppingListButtonClick(View v, Product product) {

    }

    default void onDeleteProductButtonClick(View v, Product product) {

    }

    default void onEditProductButtonClick(View v, Product product) {

    }

    default void onCheckShoppingListButtonClick(View v, Product product) {

    }
}
