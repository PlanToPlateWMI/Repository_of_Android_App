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

package pl.plantoplate.main.recepies;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import mockwebserver3.MockWebServer;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.R;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static pl.plantoplate.main.recepies.MockHelper.enqueueResponse;
import static pl.plantoplate.main.recepies.TestDataJsonGenerator.generateProducts;
import static pl.plantoplate.main.recepies.TestDataJsonGenerator.generateRecipes;
import static pl.plantoplate.main.recepies.TestDataJsonGenerator.generateUserInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class RecepiesFragmentTest {

    private MockWebServer server;

    private final String category;
    private final List<String> recipeNames;
    private final boolean performSwiping;

    public RecepiesFragmentTest(String category, List<String> recipeNames, boolean performSwiping) {
        this.category = category;
        this.recipeNames = recipeNames;
        this.performSwiping = performSwiping;
    }

    @Before
    public void setUp() throws IOException {
        Intents.init();
        server = new MockWebServer();
        server.start(8080);
        enqueueStartResponses();

        ActivityScenario.launch(ActivityMain.class);
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
        Intents.release();
    }

    private void enqueueStartResponses() {
        enqueueResponse(server, 200, generateUserInfo());
        enqueueResponse(server, 200, generateProducts());
    }

    @Parameterized.Parameters(name = "{index}: Test-{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Main dishes", List.of("recipe11", "recipe12"), false},
                {"Soups", List.of("recipe2"), false},
                {"Desserts", List.of("recipe3"), true},
                {"Snacks", List.of("recipe4"), true},
                {"Drinks", List.of("recipe5"), true}
        });
    }

    private int getViewIdByCategory(String category) {
        switch (category) {
            case "Main dishes":
                return R.id.dania_glowne;
            case "Soups":
                return R.id.zupy;
            case "Desserts":
                return R.id.desery;
            case "Snacks":
                return R.id.przekaski;
            case "Drinks":
                return R.id.napoje;
            default:
                return -1;
        }
    }

    @Test
    public void testRecepiesDisplayed() {
        enqueueResponse(server, 200, generateRecipes());

        onView(withId(R.id.receipt_long)).perform(click());

        if (performSwiping) {
            onView(withId(R.id.hscroll)).perform(swipeLeft());
        }

        onView(withId(getViewIdByCategory(category))).perform(click());
        for (String recipeName : recipeNames) {
            onView(withText(recipeName)).check(matches(isDisplayed()));
        }
    }
}