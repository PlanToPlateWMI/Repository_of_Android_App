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

package pl.plantoplate.main.settings;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.main.settings.SettingsFragment;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.GroupCodeTypeActivity;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.SharedPreferences;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

    @Rule
    public ActivityScenarioRule<ActivityMain> fragmentRule =
            new ActivityScenarioRule<>(ActivityMain.class);

    @Before
    public void setUp() {
        // Initialize Intents before each test
        Intents.init();

        // Navigate to the SettingsFragment
        navigateToSettingsFragment();
    }

    @After
    public void cleanup() {
        // Release Intents after each test
        Intents.release();
    }

    public void navigateToSettingsFragment() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, SettingsFragment.class, null)
                    .commit();
        });
    }

    public SharedPreferences getSharedPreferences() {
        final SharedPreferences[] sharedPreferences = {null};

        // Get the SharedPreferences instance
        fragmentRule.getScenario().onActivity(activity -> sharedPreferences[0] = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE));

        return sharedPreferences[0];
    }

    public void setSharedPreferences() {
        // Get the SharedPreferences instance
        SharedPreferences prefs = getSharedPreferences();

        // Set the SharedPreferences values
        prefs.edit().putString("name", "test_name").apply();
        prefs.edit().putString("email", "test_email").apply();
        prefs.edit().putString("password", "test_password").apply();
        prefs.edit().putString("role", "test_role").apply();
        prefs.edit().putString("token", "test_token").apply();
    }

    @Test
    public void generateGroupCodeButtonNavigatesToGroupCodeTypeActivity() {
        // Perform a click on the generate group code button
        onView(withId(R.id.button_wygenerowanie_kodu)).perform(click());

        // Check if the GroupCodeTypeActivity is launched
        intended(hasComponent(GroupCodeTypeActivity.class.getName()));
    }

    @Test
    public void exitAccountButtonNavigatesToLoginActivity() {
        // Perform a click on the exit account button
        onView(withId(R.id.button_wyloguj)).perform(click());

        // Check if the LoginActivity is launched
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void exitAccountButtonClearsSharedPreferences() {

        setSharedPreferences();

        // Get the SharedPreferences instance
        SharedPreferences prefs = getSharedPreferences();

        // Perform a click on the exit account button
        onView(withId(R.id.button_wyloguj)).perform(click());

        // Verify that the shared preferences are cleared
        Assert.assertFalse(prefs.contains("name"));
        Assert.assertFalse(prefs.contains("email"));
        Assert.assertFalse(prefs.contains("password"));
        Assert.assertFalse(prefs.contains("role"));
        Assert.assertFalse(prefs.contains("token"));
    }
}
