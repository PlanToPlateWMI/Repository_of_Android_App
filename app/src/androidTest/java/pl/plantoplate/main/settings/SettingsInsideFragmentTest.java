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

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockwebserver3.MockWebServer;
import pl.plantoplate.data.remote.models.user.Role;
import pl.plantoplate.service.push_notification.PushNotificationService;
import pl.plantoplate.tools.MockHelper;
import pl.plantoplate.tools.ServiceHelper;
import pl.plantoplate.tools.TestDataJsonGenerator;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;

import static pl.plantoplate.tools.SharedPreferencesHelper.getSharedPreferencesFromActivityScenario;
import static pl.plantoplate.tools.SharedPreferencesHelper.setUpSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class SettingsInsideFragmentTest {

    private ActivityScenario<ActivityMain> activityScenario;
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        // Initialize Intents before each test
        Intents.init();

        // server
        server = new MockWebServer();
        server.start(8080);

        MockHelper.enqueueResponse(server,  200, TestDataJsonGenerator.generateUserInfo(Role.ROLE_ADMIN));
        MockHelper.enqueueResponse(server,  200, TestDataJsonGenerator.generateProducts());
        MockHelper.enqueueResponse(server,  200, TestDataJsonGenerator.generateUserInfos());
        MockHelper.enqueueResponse(server,  200, TestDataJsonGenerator.generateUserInfo(Role.ROLE_ADMIN));

        // start activity
        activityScenario = ActivityScenario.launch(ActivityMain.class);

        // disable firebase service
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ServiceHelper.disableService(appContext, PushNotificationService.class);

        setUpSharedPreferences(getSharedPreferencesFromActivityScenario(activityScenario));

        // Navigate to the SettingsFragment
        onView(withId(R.id.settings)).perform(click());
    }

    @After
    public void cleanup() throws IOException {
        // test Helper
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ServiceHelper.enableService(appContext, PushNotificationService.class);

        // Release Intents after each test
        Intents.release();

        // Shutdown server
        server.shutdown();
    }

    @Test
    public void testSwitchAppTheme() {

        SharedPreferences sharedPreferences = getSharedPreferencesFromActivityScenario(activityScenario);

        String theme = sharedPreferences.getString("theme", "");
        assertEquals("light", theme);

        onView(withId(R.id.sun)).check(matches(isDisplayed()));
        onView(withId(R.id.moon)).check(matches(isDisplayed()));
        onView(withId(R.id.switchButtonChangeColorTheme)).check(matches(isDisplayed()));

        onView(withId(R.id.switchButtonChangeColorTheme)).perform(click());
        theme = sharedPreferences.getString("theme", "");
        assertEquals("dark", theme);
    }

    @Test
    public void testExitAccountCorrectly() {
        // Get the SharedPreferences instance
        SharedPreferences prefs = getSharedPreferencesFromActivityScenario(activityScenario);

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

        intended(hasComponent(LoginActivity.class.getName()));
    }
}