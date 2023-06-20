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

package pl.plantoplate.login.remindPasswordTests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import pl.plantoplate.R;
import pl.plantoplate.ui.login.remindPassword.EnterEmailActivity;

@RunWith(AndroidJUnit4.class)
public class EnterMailActivityTest {

    @Rule
    public ActivityScenarioRule<EnterEmailActivity> activityScenarioRule
            = new ActivityScenarioRule<>(EnterEmailActivity.class);

    //serwer
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        // Initialize Intents
        Intents.init();

        // server
        server = new MockWebServer();
        server.start();
    }

    @After
    public void tearDown() throws IOException {
        // Release Intents
        Intents.release();

        // Shutdown server
        server.shutdown();
    }


    //remind password 1
    @Test
    public void testChangePasswordViewDisplayed() {
        onView(ViewMatchers.withId(R.id.enter_the_name)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdz)).check(matches(isDisplayed()));
    }

    @Test
    public void testChangePasswordButton() {
        onView(withId(R.id.enter_the_name)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());
    }

    @Test
    public void testNoUserWithSuchMail() throws InterruptedException {
        MockResponse response = new MockResponse()
                .setResponseCode(400)
                .setBody("Account with this email doesn't exist");
        server.enqueue(response);

        onView(withId(R.id.enter_the_name)).perform(typeText("testmailmail@test.com"), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

        try {
            Thread.sleep(2000); // Adjust the duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Użytkownik o podanym adresie email nie istnieje")));
    }
}

