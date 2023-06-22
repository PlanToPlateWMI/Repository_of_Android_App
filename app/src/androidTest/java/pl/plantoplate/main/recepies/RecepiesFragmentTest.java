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

import mockwebserver3.MockWebServer;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.main.calendar.CalendarFragment;
import pl.plantoplate.ui.main.recepies.RecipeFragment;
import pl.plantoplate.ui.main.settings.SettingsFragment;
import pl.plantoplate.ui.main.settings.SettingsInsideFragment;
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

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class RecepiesFragmentTest {

    @Rule
    public ActivityScenarioRule<ActivityMain> fragmentRule =
            new ActivityScenarioRule<>(ActivityMain.class);

    //serwer
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        // Initialize Intents
        Intents.init();

        // Navigate to the RecepiesFragment
        navigateToRecepiesFragment();

        // server
        server = new MockWebServer();
        server.start(8080);
    }

    @After
    public void tearDown() throws IOException {
        // Release Intents
        Intents.release();

        // Shutdown server
        server.shutdown();
    }

    public void navigateToRecepiesFragment() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, RecipeFragment.class, null)
                    .commit();
        });
    }

//    public void navigateToRecepiesFragment() {
//
//        fragmentRule.getScenario().onActivity(activity -> {
//            activity.getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.frame_layout, RecipeFragment.class, null)
//                    .commit();
//        });
//    }
//
//    @Test
//    public void testRecepiesDisplayed() {
//        onView(withId(R.id.recipie)).check(matches(isDisplayed()));
//    }
}

