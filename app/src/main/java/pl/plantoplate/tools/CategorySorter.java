/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.plantoplate.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.data.remote.models.Category;

/**
 * CategorySorter is a class that sorts categories and products.
 */
public class CategorySorter {

    /**
     * Sorts a list of products by category name and product name.
     *
     * @param products The list of products to sort.
     * @return The sorted list of products.
     */
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

    /**
     * Sorts a list of products by product name.
     *
     * @param products The list of products to sort.
     * @return The sorted list of products.
     */
    public static ArrayList<Product> sortProductsByName(ArrayList<Product> products) {
        products.sort(Comparator.comparing(Product::getName));
        return products;
    }

    /**
     * Filters a list of categories by query
     * @param products The list of categories to filter.
     * @param query The query to filter by.
     * @return The filtered list of categories.
     */
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

    /**
     * Filters a list of products by query
     * @param products The list of products to filter.
     * @param query The query to filter by.
     * @return The filtered list of products.
     */
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