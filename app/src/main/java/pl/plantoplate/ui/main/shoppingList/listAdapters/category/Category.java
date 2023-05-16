package pl.plantoplate.ui.main.shoppingList.listAdapters.category;

import java.util.ArrayList;
import java.util.List;

import pl.plantoplate.requests.products.Product;

public class Category {
    private int id;
    private String name;
    private ArrayList<Product> products;

    public Category() {
    }

    public Category(int id, String name, ArrayList<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}