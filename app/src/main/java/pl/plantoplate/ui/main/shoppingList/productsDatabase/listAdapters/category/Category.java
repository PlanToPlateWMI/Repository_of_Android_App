package pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.category;

import java.util.List;

import pl.plantoplate.requests.products.Product;

public class Category {
    private int id;
    private String name;
    private List<Product> products;

    public Category() {
    }

    public Category(int id, String name, List<Product> products) {
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

    public List<Product> getProducts() {
        return products;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}