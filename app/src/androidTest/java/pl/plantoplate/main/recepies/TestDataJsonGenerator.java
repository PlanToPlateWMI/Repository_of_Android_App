package pl.plantoplate.main.recepies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.recipe.Level;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.data.remote.models.user.Role;
import pl.plantoplate.data.remote.models.user.UserInfo;

public class TestDataJsonGenerator {

    private TestDataJsonGenerator() {}

    public static String generateRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(11, "recipe11", "10", Level.EASY, null, "Dania główne", false));
        recipes.add(new Recipe(12, "recipe12", "10", Level.EASY, null, "Dania główne", false));
        recipes.add(new Recipe(2, "recipe2", "20", Level.MEDIUM, null, "Zupy", true));
        recipes.add(new Recipe(3, "recipe3", "30", Level.HARD, null, "Desery", false));
        recipes.add(new Recipe(5, "recipe4", "50", Level.MEDIUM, null, "Przekąski", false));
        recipes.add(new Recipe(4, "recipe5", "40", Level.EASY, null, "Napoje", true));
        return toJson(recipes);
    }

    public static String generateUserInfo() {
        UserInfo userInfo = new UserInfo("user", "user@gmail.com", Role.ROLE_ADMIN.toString());
        return toJson(userInfo);
    }

    public static String generateProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "product1", "Mięso", 1, "kg"));
        return toJson(products);
    }

    public static String toJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
