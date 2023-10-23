package pl.plantoplate.ui.main.shoppingList.viewModels;

import java.util.ArrayList;

import pl.plantoplate.data.remote.models.ListType;
import pl.plantoplate.data.remote.models.Product;

public class ProductsListChangedEvent {

    private ArrayList<Product> itemList;
    private ListType listType;

    public ProductsListChangedEvent (ArrayList<Product> itemList, ListType listType) {
        this.itemList = itemList;
        this.listType = listType;
    }

    public ArrayList<Product> getItemList() {
        return itemList;
    }

    public ListType getListType() {
        return listType;
    }
}
