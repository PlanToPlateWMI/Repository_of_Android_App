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

package pl.plantoplate.main.settings.accountManagement;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockwebserver3.MockWebServer;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.main.settings.accountManagement.ChangeTheData;
import pl.plantoplate.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class ChangeTheDataTest {

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
        navigateToChangeTheData();

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

    public void navigateToChangeTheData() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, ChangeTheData.class, null)
                    .commit();
        });
    }

    @Test
    public void testSettingsFragmentInsideViewDisplayed() {

        onView(withId(R.id.zmiana_imienia)).check(matches(isDisplayed()));
        onView(withId(R.id.zmiana_email)).check(matches(isDisplayed()));
        onView(withId(R.id.zmiana_hasla)).check(matches(isDisplayed()));
        onView(withId(R.id.usuwanie_konta)).check(matches(isDisplayed()));

    }

    @Test
    public void testChangeName() {

        onView(withId(R.id.zmiana_imienia)).perform(click());

    }

    @Test
    public void testChangeEmail() {

        onView(withId(R.id.zmiana_email)).perform(click());

    }


    @Test
    public void testChangePassword() {

        onView(withId(R.id.zmiana_hasla)).perform(click());

    }

    @Test
    public void testDeleteAccount() {

        onView(withId(R.id.usuwanie_konta)).perform(click());

    }
}
