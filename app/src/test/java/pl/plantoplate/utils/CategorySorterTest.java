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
package pl.plantoplate.utils;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

import pl.plantoplate.data.remote.models.meal.Meal;
import pl.plantoplate.data.remote.models.meal.MealType;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.product.ProductCategory;
import pl.plantoplate.data.remote.models.recipe.Level;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.ui.main.calendar.recycler_views.models.MealTypes;
import pl.plantoplate.ui.main.recipes.recycler_views.RecipeCategory;

public class CategorySorterTest {

    @Test
    public void testSortCategoriesByProduct() {
        // Test with an empty product list
        List<Product> emptyProducts = List.of();
        List<ProductCategory> emptyResult = CategorySorter.sortCategoriesByProduct(emptyProducts);
        assertTrue(emptyResult.isEmpty());

        // Test with a list of food products
        List<Product> foodProducts = Arrays.asList(
                new Product(1, "Apple", "Food", 5, "pieces"),
                new Product(2, "Banana", "Food", 3, "pieces"),
                new Product(3, "Carrot", "Food", 2, "pieces"),
                new Product(4, "Dill", "Food",1, "pieces")
        );

        List<ProductCategory> sortedFoodCategories = CategorySorter.sortCategoriesByProduct(foodProducts);

        // Verify if categories are sorted alphabetically
        assertEquals("Food", sortedFoodCategories.get(0).getName());

        // Verify the number of categories (in this case, only one category - "Food")
        assertEquals(1, sortedFoodCategories.size());

        // Verify if products within the "Food" category are sorted alphabetically
        assertEquals("Apple", sortedFoodCategories.get(0).getProducts().get(0).getName());
        assertEquals("Banana", sortedFoodCategories.get(0).getProducts().get(1).getName());
        assertEquals("Carrot", sortedFoodCategories.get(0).getProducts().get(2).getName());
        assertEquals("Dill", sortedFoodCategories.get(0).getProducts().get(3).getName());
    }


    @Test
    public void testSortProductsByName() {
        List<Product> products = Arrays.asList(
                new Product(1, "Banana", "Food", 3, "pieces"),
                new Product(2, "Apple", "Food", 5, "pieces"),
                new Product(3, "Dill", "Food", 1, "pieces"),
                new Product(4, "Carrot", "Food", 2, "pieces")
        );

        // Sort products by name
        List<Product> sortedProducts = CategorySorter.sortProductsByName(products);

        // Verify if the products are sorted by name
        assertEquals("Apple", sortedProducts.get(0).getName());
        assertEquals("Banana", sortedProducts.get(1).getName());
        assertEquals("Carrot", sortedProducts.get(2).getName());
        assertEquals("Dill", sortedProducts.get(3).getName());
    }

    @Test
    public void testFilterCategoriesBySearch() {
        // Sample list of ProductCategory instances
        List<ProductCategory> sampleCategories = Arrays.asList(
                new ProductCategory(1, "Fruits", Arrays.asList(
                        new Product(1, "Apple", "Fruits", 5, "pieces"),
                        new Product(2, "Banana", "Fruits", 3, "pieces"),
                        new Product(3, "Orange", "Fruits", 2, "pieces")
                )),
                new ProductCategory(2, "Vegetables", Arrays.asList(
                        new Product(4, "Carrot", "Vegetables", 2, "pieces"),
                        new Product(5, "Tomato", "Vegetables", 4, "pieces"),
                        new Product(6, "Cucumber", "Vegetables", 3, "pieces")
                )),
                new ProductCategory(3, "Dairy", Arrays.asList(
                        new Product(7, "Milk", "Dairy", 1, "bottle"),
                        new Product(8, "Cheese", "Dairy", 1, "pack"),
                        new Product(9, "Yogurt", "Dairy", 3, "cups")
                ))
        );

        // Test filtering with a query "apple"
        List<Product> filteredProducts = CategorySorter.filterCategoriesBySearch(sampleCategories, "apple");

        assertEquals(1, filteredProducts.size());
        assertEquals("Apple", filteredProducts.get(0).getName());

        // Test filtering with a query "cu"
        filteredProducts = CategorySorter.filterCategoriesBySearch(sampleCategories, "cu");

        assertEquals(1, filteredProducts.size());
        assertEquals("Cucumber", filteredProducts.get(0).getName());

        // Test filtering with a query that doesn't match any product name
        filteredProducts = CategorySorter.filterCategoriesBySearch(sampleCategories, "nonexistent");
        assertTrue(filteredProducts.isEmpty());
    }

    @Test
    public void testFilterProductsBySearch() {
        List<Product> sampleProducts = Arrays.asList(
                new Product(1, "Apple", "Food", 5, "pieces"),
                new Product(2, "Banana", "Food", 3, "pieces"),
                new Product(3, "Orange", "Food", 2, "pieces"),
                new Product(4, "Carrot", "Food", 2, "pieces")
        );

        // Test filtering with a query "apple"
        List<Product> filteredProducts = CategorySorter.filterProductsBySearch(sampleProducts, "apple");

        assertEquals(1, filteredProducts.size());
        assertEquals("Apple", filteredProducts.get(0).getName());

        // Test filtering with a query "car"
        filteredProducts = CategorySorter.filterProductsBySearch(sampleProducts, "car");

        assertEquals(1, filteredProducts.size());
        assertEquals("Carrot", filteredProducts.get(0).getName());

        // Test filtering with a query that doesn't match any product name
        filteredProducts = CategorySorter.filterProductsBySearch(sampleProducts, "nonexistent");
        assertTrue(filteredProducts.isEmpty());
    }

    @Test
    public void testSortCategoriesByRecipe() {
        List<Recipe> sampleRecipes = Arrays.asList(
                new Recipe(11, "Pasta Carbonara", "30", Level.EASY, null, "Pasta", false),
                new Recipe(12, "Chicken Parmesan", "40", Level.EASY, null, "Main Course", false),
                new Recipe(2, "Tomato Basil Soup", "25", Level.MEDIUM, null, "Soups", true),
                new Recipe(5, "Stuffed Mushrooms", "50", Level.MEDIUM, null, "Appetizers", false)
        );

        List<RecipeCategory> sortedCategories = CategorySorter.sortCategoriesByRecipe(sampleRecipes);

        // Verify if categories are sorted alphabetically
        assertEquals("Appetizers", sortedCategories.get(0).getName());
        assertEquals("Main Course", sortedCategories.get(1).getName());
        assertEquals("Pasta", sortedCategories.get(2).getName());
        assertEquals("Soups", sortedCategories.get(3).getName());

        // Verify if recipes within categories are sorted alphabetically
        assertEquals("Chicken Parmesan", sortedCategories.get(1).getRecipes().get(0).getTitle());
        assertEquals("Stuffed Mushrooms", sortedCategories.get(0).getRecipes().get(0).getTitle());
    }

    @Test
    public void testSortRecipesByName() {
        List<Recipe> unsortedRecipes = Arrays.asList(
                new Recipe(11, "Pasta Carbonara", "30", Level.EASY, null, "Pasta", false),
                new Recipe(12, "Chicken Parmesan", "40", Level.EASY, null, "Main Course", false),
                new Recipe(2, "Tomato Basil Soup", "25", Level.MEDIUM, null, "Soups", true),
                new Recipe(5, "Stuffed Mushrooms", "50", Level.MEDIUM, null, "Appetizers", false)
        );

        List<Recipe> sortedRecipes = CategorySorter.sortRecipesByName(unsortedRecipes);

        // Verify if recipes are sorted alphabetically by title
        assertEquals("Chicken Parmesan", sortedRecipes.get(0).getTitle());
        assertEquals("Pasta Carbonara", sortedRecipes.get(1).getTitle());
        assertEquals("Stuffed Mushrooms", sortedRecipes.get(2).getTitle());
        assertEquals("Tomato Basil Soup", sortedRecipes.get(3).getTitle());
    }

    @Test
    public void testFilterRecipesCategoriesBySearch() {
        List<RecipeCategory> sampleCategories = Arrays.asList(
                new RecipeCategory("Italian", Arrays.asList(
                        new Recipe(1, "Pasta Carbonara", "30", Level.EASY, null, "Pasta", false),
                        new Recipe(2, "Chicken Parmesan", "40", Level.EASY, null, "Main Course", false)
                )),
                new RecipeCategory("Soups", Arrays.asList(
                        new Recipe(3, "Tomato Basil Soup", "25", Level.MEDIUM, null, "Soups", true),
                        new Recipe(4, "Chicken Noodle Soup", "35", Level.MEDIUM, null, "Soups", false)
                )),
                new RecipeCategory("Appetizers", Arrays.asList(
                        new Recipe(5, "Stuffed Mushrooms", "50", Level.MEDIUM, null, "Appetizers", false),
                        new Recipe(6, "Spinach Artichoke Dip", "45", Level.EASY, null, "Appetizers", false)
                ))
        );

        // Test filtering with a query "pasta"
        List<Recipe> filteredRecipes = CategorySorter.filterRecipesCategoriesBySearch(sampleCategories, "pasta");

        assertEquals(1, filteredRecipes.size());
        assertEquals("Pasta Carbonara", filteredRecipes.get(0).getTitle());

        // Test filtering with a query "soup"
        filteredRecipes = CategorySorter.filterRecipesCategoriesBySearch(sampleCategories, "soup");

        assertEquals(2, filteredRecipes.size());
        assertEquals("Tomato Basil Soup", filteredRecipes.get(0).getTitle());
        assertEquals("Chicken Noodle Soup", filteredRecipes.get(1).getTitle());

        // Test filtering with a query that doesn't match any recipe title
        filteredRecipes = CategorySorter.filterRecipesCategoriesBySearch(sampleCategories, "nonexistent");
        assertTrue(filteredRecipes.isEmpty());
    }

    @Test
    public void testFilterRecipesBySearch() {
        List<Recipe> sampleRecipes = Arrays.asList(
                new Recipe(1, "Pasta Carbonara", "30", Level.EASY, null, "Pasta", false),
                new Recipe(2, "Chicken Parmesan", "40", Level.EASY, null, "Main Course", false),
                new Recipe(3, "Tomato Basil Soup", "25", Level.MEDIUM, null, "Soups", true),
                new Recipe(4, "Stuffed Mushrooms", "50", Level.MEDIUM, null, "Appetizers", false)
        );

        // Test filtering with a query "pasta"
        List<Recipe> filteredRecipes = CategorySorter.filterRecipesBySearch(sampleRecipes, "pasta");

        assertEquals(1, filteredRecipes.size());
        assertEquals("Pasta Carbonara", filteredRecipes.get(0).getTitle());

        // Test filtering with a query "mushrooms"
        filteredRecipes = CategorySorter.filterRecipesBySearch(sampleRecipes, "mushrooms");

        assertEquals(1, filteredRecipes.size());
        assertEquals("Stuffed Mushrooms", filteredRecipes.get(0).getTitle());

        // Test filtering with a query that doesn't match any recipe title
        filteredRecipes = CategorySorter.filterRecipesBySearch(sampleRecipes, "nonexistent");
        assertTrue(filteredRecipes.isEmpty());
    }

    @Test
    public void testGroupMealsByType() {
        List<Meal> sampleMeals = Arrays.asList(
                new Meal(1, "Eggs Benedict", 20, MealType.BREAKFAST, "eggs_benedict.jpg", false, true),
                new Meal(2, "Caesar Salad", 15, MealType.LUNCH, "caesar_salad.jpg", true, false),
                new Meal(3, "Grilled Salmon", 30, MealType.DINNER, "grilled_salmon.jpg", false, false),
                new Meal(5, "Oatmeal", 15, MealType.BREAKFAST, "oatmeal.jpg", true, true),
                new Meal(6, "Pasta Primavera", 25, MealType.DINNER, "pasta_primavera.jpg", false, true)
        );

        List<MealTypes> groupedMeals = CategorySorter.groupMealsByType(sampleMeals);

        // Verify if all MealTypes are present in the correct order
        assertEquals(MealType.BREAKFAST, groupedMeals.get(0).getMealType());
        assertEquals(MealType.LUNCH, groupedMeals.get(1).getMealType());
        assertEquals(MealType.DINNER, groupedMeals.get(2).getMealType());

        // Verify if meals are correctly grouped under each MealType
        assertEquals(2, groupedMeals.get(0).getMeals().size()); // Two breakfast meals
        assertEquals(1, groupedMeals.get(1).getMeals().size()); // One lunch meal
        assertEquals(2, groupedMeals.get(2).getMeals().size()); // Two dinner meals
    }

    @Test
    public void testGetSortedMealTypeList() {
        List<Meal> sampleMeals = Arrays.asList(
                new Meal(1, "Eggs Benedict", 20, MealType.BREAKFAST, "eggs_benedict.jpg", false, true),
                new Meal(2, "Caesar Salad", 15, MealType.LUNCH, "caesar_salad.jpg", true, false),
                new Meal(3, "Grilled Salmon", 30, MealType.DINNER, "grilled_salmon.jpg", false, false),
                new Meal(5, "Oatmeal", 15, MealType.BREAKFAST, "oatmeal.jpg", true, true),
                new Meal(6, "Pasta Primavera", 25, MealType.DINNER, "pasta_primavera.jpg", false, true)
        );

        MealType targetMealType = MealType.BREAKFAST;
        List<Meal> sortedMeals = CategorySorter.getSortedMealTypeList(sampleMeals, targetMealType);

        // Verify if the returned list contains meals of the specified meal type
        for (Meal meal : sortedMeals) {
            assertEquals(targetMealType, meal.getMealType());
        }

        // Verify if the returned list is sorted by meal type
        assertEquals(2, sortedMeals.size()); // Two breakfast meals
        assertEquals(MealType.BREAKFAST, sortedMeals.get(0).getMealType());
        assertEquals(MealType.BREAKFAST, sortedMeals.get(1).getMealType());
    }
}