package pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.plantoplate.requests.products.Product;

public class CategorySorter {

    public static ArrayList<Category> sortCategoriesByProduct(ArrayList<Product> products) {
        // Create a map of category names to lists of products
        Map<String, ArrayList<Product>> categoryMap = new HashMap<>();
        for (Product product : products) {
            List<Product> productList = categoryMap.computeIfAbsent(product.getCategory(), k -> new ArrayList<>());
            productList.add(product);
        }

        // Create a list of Category objects
        ArrayList<Category> categories = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Product>> entry : categoryMap.entrySet()) {
            Category category = new Category();
            category.setId(categories.size() + 1);
            category.setName(entry.getKey());
            ArrayList<Product> sortedProducts = entry.getValue();
            sortedProducts.sort(Comparator.comparing(Product::getName));
            category.setProducts(sortedProducts);
            categories.add(category);
        }

        // Sort the list of categories by category name
        categories.sort(Comparator.comparing(Category::getName));

        return categories;
    }

    public static ArrayList<Product> sortProductsByName(ArrayList<Product> products) {
        products.sort(Comparator.comparing(Product::getName));
        return products;
    }

    public static ArrayList<Product> filterCategoriesBySearch(ArrayList<Category> products, String query) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (Category category : products) {
            for (Product product : category.getProducts()) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
        }
        return filteredProducts;
    }

    public static ArrayList<Product> filterProductsBySearch(ArrayList<Product> products, String query) {
        ArrayList<Product> filteredProducts= new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
}