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
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.product.ProductCategory;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.ui.main.recipes.recyclerViews.RecipeCategory;

/**
 * CategorySorter is a class that sorts categories and products.
 */
public class CategorySorter {

    // products sorting

    /**
     * Sorts a list of products by category name and product name.
     *
     * @param products The list of products to sort.
     * @return The sorted list of products.
     */
    public static ArrayList<ProductCategory> sortCategoriesByProduct(ArrayList<Product> products) {
        // Create a map of category names to lists of products
        Map<String, ArrayList<Product>> categoryMap = new HashMap<>();
        for (Product product : products) {
            List<Product> productList = categoryMap.computeIfAbsent(product.getCategory(), k -> new ArrayList<>());
            productList.add(product);
        }

        // Create a list of Category objects
        ArrayList<ProductCategory> categories = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Product>> entry : categoryMap.entrySet()) {
            ProductCategory category = new ProductCategory();
            category.setId(categories.size() + 1);
            category.setName(entry.getKey());
            ArrayList<Product> sortedProducts = entry.getValue();
            sortedProducts.sort(Comparator.comparing(Product::getName));
            category.setProducts(sortedProducts);
            categories.add(category);
        }

        // Sort the list of categories by category name
        categories.sort(Comparator.comparing(ProductCategory::getName));

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
    public static ArrayList<Product> filterCategoriesBySearch(ArrayList<ProductCategory> products, String query) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (ProductCategory category : products) {
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

    // recipes sorting

    public static ArrayList<RecipeCategory> sortCategoriesByRecipe(ArrayList<Recipe> recipes) {
        // Create a map of category names to lists of products
        Map<String, ArrayList<Recipe>> categoryMap = new HashMap<>();
        for (Recipe recipe : recipes) {
            List<Recipe> recipeList = categoryMap.computeIfAbsent(recipe.getCategoryName(), k -> new ArrayList<>());
            recipeList.add(recipe);
        }

        // Create a list of Category objects
        ArrayList<RecipeCategory> categories = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Recipe>> entry : categoryMap.entrySet()) {
            RecipeCategory category = new RecipeCategory();
            category.setName(entry.getKey());
            ArrayList<Recipe> sortedRecipes = entry.getValue();
            sortedRecipes.sort(Comparator.comparing(Recipe::getTitle));
            category.setRecipes(sortedRecipes);
            categories.add(category);
        }

        // Sort the list of categories by category name
        categories.sort(Comparator.comparing(RecipeCategory::getName));

        return categories;
    }

    public static ArrayList<Recipe> sortRecipesByName(ArrayList<Recipe> recipes) {
        recipes.sort(Comparator.comparing(Recipe::getTitle));
        return recipes;
    }

    public static ArrayList<Recipe> filterRecipesCategoriesBySearch(ArrayList<RecipeCategory> recipes, String query) {
        ArrayList<Recipe> filteredRecipes = new ArrayList<>();
        for (RecipeCategory category : recipes) {
            for (Recipe recipe : category.getRecipes()) {
                if (recipe.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredRecipes.add(recipe);
                }
            }
        }
        return filteredRecipes;
    }

    public static ArrayList<Recipe> filterRecipesBySearch(ArrayList<Recipe> recipes, String query) {
        ArrayList<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
    }
}