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
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import pl.plantoplate.repository.remote.user.UserRepository;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.main.settings.SettingsInsideFragment;
import pl.plantoplate.ui.main.settings.accountManagement.ChangeTheData;
import pl.plantoplate.ui.main.settings.changePermissions.ChangePermissionsFragment;
import pl.plantoplate.ui.main.settings.developerContact.MailDevelops;
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
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

    @Rule
    public ActivityScenarioRule<ActivityMain> fragmentRule =
            new ActivityScenarioRule<>(ActivityMain.class);

    //serwer
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        // Initialize Intents before each test
        Intents.init();

        // Navigate to the SettingsFragment
        navigateToSettingsFragment();

        // server
        server = new MockWebServer();
        server.start(8080);
    }

    @After
    public void cleanup() throws IOException{
        // Release Intents after each test
        Intents.release();

        // Shutdown server
        server.shutdown();
    }

    public void navigateToSettingsFragment() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, SettingsInsideFragment.class, null)
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

        String name = "test_name";
        String email = "test_email";
        String password = "test_password";
        String role = "test_role";
        String token = "test_token";

        prefs.edit().putString("name", name).apply();
        prefs.edit().putString("email", email).apply();
        prefs.edit().putString("password", password).apply();
        prefs.edit().putString("role", role).apply();
        prefs.edit().putString("token", token).apply();
    }

    @Test
    public void testSettingsFragmentInsideViewDisplayed() {

        onView(withId(R.id.sun)).check(matches(isDisplayed()));
        onView(withId(R.id.switchButtonChangeColorTheme)).check(matches(isDisplayed()));
        onView(withId(R.id.moon)).check(matches(isDisplayed()));

        onView(withId(R.id.imie)).check(matches(isDisplayed()));
        onView(withId(R.id.button_wygenerowanie_kodu)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zmiana_danych)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zarzadyanie_uyztkownikamu)).check(matches(isDisplayed()));
        onView(withId(R.id.button_about_us)).check(matches(isDisplayed()));
        onView(withId(R.id.button_wyloguj)).check(matches(isDisplayed()));

    }

    public void navigateGroupCodeTypeActivity() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, GroupCodeTypeActivity.class, null)
                    .commit();
        });
    }

    @Test
    public void generateGroupCodeButtonNavigatesToGroupCodeTypeActivity() {

        String name = "";
        String email = "";
        String role = "";

        MockResponse response = new MockResponse()
                .setResponseCode(200)
                .setBody("{" +
                        "\"username\": \"name\"" + "," +
                        "\"email\": \"email\"" + "," +
                        "\"role\": \"ROLE_ADMIN\""
                        + "}");
        server.enqueue(response);

        // Perform a click on the generate group code button
        onView(withId(R.id.button_zarzadyanie_uyztkownikamu)).perform(click());

        // Check if the GroupCodeTypeActivity is launched
        navigateGroupCodeTypeActivity();
    }

    public void navigateChangePermissionsFragment() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, ChangePermissionsFragment.class, null)
                    .commit();
        });
    }

    @Test
    public void generateChangeUsersSettings() {

        String name = "";
        String email = "";
        String role = "";

        MockResponse response = new MockResponse()
                .setResponseCode(200)
                .setBody("{" +
                        "\"username\": \"name\"" + "," +
                        "\"email\": \"email\"" + "," +
                        "\"role\": \"ROLE_ADMIN\""
                        + "}");
        server.enqueue(response);

        // Perform a click on the generate group code button
        onView(withId(R.id.button_wygenerowanie_kodu)).perform(click());

        // Check if the GroupCodeTypeActivity is launched
        navigateChangePermissionsFragment();
    }

    public void navigateChangeTheData() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, ChangeTheData.class, null)
                    .commit();
        });
    }

    @Test
    public void changeTheData() {

        // Perform a click on the generate group code button
        onView(withId(R.id.button_zmiana_danych)).perform(click());

        // Check if the GroupCodeTypeActivity is launched
        navigateChangeTheData();
        // intended(hasComponent(ChangeTheData.class.getName()));
    }

    public void navigateToWriteToUs() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, MailDevelops.class, null)
                    .commit();
        });
    }

    @Test
    public void writeToUs() {

        // Perform a click on the generate group code button
        onView(withId(R.id.button_about_us)).perform(click());

        // Check if the GroupCodeTypeActivity is launched
        navigateToWriteToUs();
        // intended(hasComponent(ChangeTheData.class.getName()));
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

        String name = "name";
        String email = "email";
        String password = "password";
        String role = "role";
        String token = "token";

        // Verify that the shared preferences are cleared
        Assert.assertFalse(prefs.contains(name));
        Assert.assertFalse(prefs.contains(email));
        Assert.assertFalse(prefs.contains(password));
        Assert.assertFalse(prefs.contains(role));
        Assert.assertFalse(prefs.contains(token));
    }
}
