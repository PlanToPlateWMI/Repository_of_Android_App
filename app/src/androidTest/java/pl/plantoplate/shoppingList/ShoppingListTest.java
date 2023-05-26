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

package pl.plantoplate.shoppingList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.ActivityMain;


@RunWith(AndroidJUnit4.class)
public class ShoppingListTest {

    @Rule
    public ActivityScenarioRule<ActivityMain> activityScenarioRule
            = new ActivityScenarioRule<>(ActivityMain.class);


    @Before
    public void setUp() {
        // Initialize Intents
        Intents.init();
    }

    @After
    public void tearDown() {
        // Release Intents
        Intents.release();
    }

    @Test
    public void testShoppingListDisplayed() {
        onView(ViewMatchers.withId(R.id.shopping)).check(matches(isDisplayed()));
        onView(withId(R.id.bottomNavigationView2)).check(matches(isDisplayed()));
        onView(withId(R.id.shopping_list_default)).check(matches(isDisplayed()));
    }


    @Test
    public void testTrzebaKupicNavbar() {
        onView(withId(R.id.trzeba_kupic)).perform(click());
        onView(withId(R.id.products_own_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testKupioneNavbar() {
        onView(withId(R.id.kupione)).perform(click());
        onView(withId(R.id.category_recycler_view)).check(matches(isDisplayed()));
    }

}
