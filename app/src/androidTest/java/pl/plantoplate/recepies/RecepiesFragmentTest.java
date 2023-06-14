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

package pl.plantoplate.recepies;


import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.main.calendar.CalendarFragment;
import pl.plantoplate.ui.main.recepies.RecipeFragment;
import pl.plantoplate.ui.main.settings.SettingsFragment;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.GroupCodeTypeActivity;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.SharedPreferences;

@RunWith(AndroidJUnit4.class)
public class RecepiesFragmentTest {

    @Rule
    public ActivityScenarioRule<ActivityMain> fragmentRule =
            new ActivityScenarioRule<>(ActivityMain.class);

    @Before
    public void setUp() {
        // Initialize Intents before each test
        Intents.init();

        // Navigate to the SettingsFragment
        navigateToRecepiesFragment();
    }

    @After
    public void cleanup() {
        // Release Intents after each test
        Intents.release();
    }

    public void navigateToRecepiesFragment() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, RecipeFragment.class, null)
                    .commit();
        });
    }

    @Test
    public void testRecepiesDisplayed() {
        onView(ViewMatchers.withId(R.id.recipie)).check(matches(isDisplayed()));
        //onView(withId(R.id.textView)).check(matches(isDisplayed()));

    }
}

