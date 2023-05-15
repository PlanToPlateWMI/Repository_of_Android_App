package pl.plantoplate.requests.shoppingList;

import java.util.ArrayList;

import pl.plantoplate.requests.products.Product;

public class ShoppingList {

    private ArrayList<Product> toBuy;
    private ArrayList<Product> bought;

    public ShoppingList(ArrayList<Product> toBuy, ArrayList<Product> bought) {
        this.toBuy = toBuy;
        this.bought = bought;
    }

    public ArrayList<Product> getToBuy() {
        return toBuy;
    }

    public void setToBuy(ArrayList<Product> toBuy) {
        this.toBuy = toBuy;
    }

    public ArrayList<Product> getBought() {
        return bought;
    }

    public void setBought(ArrayList<Product> bought) {
        this.bought = bought;
    }
}
