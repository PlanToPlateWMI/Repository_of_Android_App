package pl.plantoplate.main.recepies;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.recipe.Ingredient;
import pl.plantoplate.data.remote.models.recipe.Level;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import pl.plantoplate.data.remote.models.user.Role;
import pl.plantoplate.data.remote.models.user.UserInfo;
import pl.plantoplate.ui.main.ActivityMain;

@RunWith(AndroidJUnit4.class)
public class RecipesInfoFragmentTest {

    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        Intents.init();
        server = new MockWebServer();
        server.start(8080);
        enqueueStartResponses();
        enqueueRecepiesResponses();
        enqueueRecipeInfoResponse();

        ActivityScenario.launch(ActivityMain.class);

        onView(withId(R.id.receipt_long)).perform(click());
        onView(withText("recipe11")).perform(click());
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
        Intents.release();
    }

    public List<Recipe> generateRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(11, "recipe11", "10", Level.EASY, null, "Dania główne", false));
        recipes.add(new Recipe(12, "recipe12", "10", Level.EASY, null, "Dania główne", false));
        recipes.add(new Recipe(13, "recipe13", "10", Level.EASY, null, "Dania główne", false));
        recipes.add(new Recipe(2, "recipe2", "20", Level.MEDIUM, null, "Zupy", true));
        recipes.add(new Recipe(3, "recipe3", "30", Level.HARD, null, "Desery", false));
        recipes.add(new Recipe(5, "recipe4", "50", Level.MEDIUM, null, "Przekąski", false));
        recipes.add(new Recipe(4, "recipe5", "40", Level.EASY, null, "Napoje", true));
        return recipes;
    }

    public UserInfo generateUserInfo() {
        return new UserInfo("user", "user@gmail.com", Role.ROLE_ADMIN.toString());
    }

    public List<Product> generateProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "product1", "Mięso", 1, "kg"));

        return products;
    }

    public RecipeInfo generateRecipeInfo() {
        List<String> steps = new ArrayList<>();
        steps.add("1. Krok 1");
        steps.add("2. Krok 2");
        steps.add("3. Krok 3");
        steps.add("4. Krok 4");

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, 0.2f, "ingredient1", "kg"));
        ingredients.add(new Ingredient(2, 0.5f, "ingredient2", "kg"));
        ingredients.add(new Ingredient(3, 0.7f, "ingredient3", "kg"));
        ingredients.add(new Ingredient(4, 1f, "ingredient4", "kg"));


        return new RecipeInfo(11, "recipe11", null, null,
                "10", Level.EASY, 1, steps, ingredients, false, 11);
    }

    public void enqueueStartResponses() {
        ObjectMapper objectMapper = new ObjectMapper();
        String productsJson = null;
        String userInfoJson = null;

        try {
            productsJson = objectMapper.writeValueAsString(generateProducts());
            userInfoJson = objectMapper.writeValueAsString(generateUserInfo());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(productsJson);
        System.out.println(userInfoJson);

        MockResponse productsResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(productsJson);

        MockResponse userInfoResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(userInfoJson);
        server.enqueue(userInfoResponse);
        server.enqueue(productsResponse);
    }

    public void enqueueRecepiesResponses() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(generateRecipes());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        MockResponse response = new MockResponse()
                .setResponseCode(200)
                .setBody(json);
        server.enqueue(response);
    }

    public void enqueueRecipeInfoResponse() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(generateRecipeInfo());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        MockResponse response = new MockResponse()
                .setResponseCode(200)
                .setBody(json);
        server.enqueue(response);
    }

    @Test
    public void testRecipeInfoDisplayed() {

        onView(withId(R.id.textNazwaPrzepisu)).check(matches(withText("recipe11")));
        onView(withId(R.id.timeText)).check(matches(withText("10 min.")));
        onView(withId(R.id.levelText)).check(matches(withText("Łatwy")));
        onView(withId(R.id.question)).perform(click()).check(matches(withText("Odznacz polę, jeśli nie chcesz produktu")));

        for (Ingredient ingredient : generateRecipeInfo().getIngredients()) {
            onView(withText(ingredient.getIngredientName())).check(matches(isDisplayed()));
        }

        onView(withId(R.id.przepis_button)).perform(click());

        for (String step : generateRecipeInfo().getSteps()) {
            onView(withText(step)).check(matches(isDisplayed()));
        }
    }
}
