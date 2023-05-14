package pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.plantoplate.requests.products.Product;

public class CategorySorter {

    public static List<Category> sortCategoriesByProduct(List<Product> products) {
        // Create a map of category names to lists of products
        Map<String, List<Product>> categoryMap = new HashMap<>();
        for (Product product : products) {
            List<Product> productList = categoryMap.computeIfAbsent(product.getCategory(), k -> new ArrayList<>());
            productList.add(product);
        }

        // Create a list of Category objects
        List<Category> categories = new ArrayList<>();
        for (Map.Entry<String, List<Product>> entry : categoryMap.entrySet()) {
            Category category = new Category();
            category.setId(categories.size() + 1);
            category.setName(entry.getKey());
            List<Product> sortedProducts = entry.getValue();
            sortedProducts.sort(Comparator.comparing(Product::getName));
            category.setProducts(sortedProducts);
            categories.add(category);
        }

        // Sort the list of categories by category name
        categories.sort(Comparator.comparing(Category::getName));

        return categories;
    }

    public static List<Product> sortProductsByName(List<Product> products) {
        products.sort(Comparator.comparing(Product::getName));
        return products;
    }
}