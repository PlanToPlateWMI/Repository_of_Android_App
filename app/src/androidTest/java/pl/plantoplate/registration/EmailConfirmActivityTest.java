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

package pl.plantoplate.registration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

import android.net.Uri;

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
import mockwebserver3.RecordedRequest;
import pl.plantoplate.R;
import pl.plantoplate.ui.login.remindPassword.EnterCodeActivity;
import pl.plantoplate.ui.registration.RegisterActivity;

@RunWith(AndroidJUnit4.class)
public class EmailConfirmActivityTest {

    @Rule
    public ActivityScenarioRule<EnterCodeActivity> activityScenarioRule
            = new ActivityScenarioRule<>(EnterCodeActivity.class);

    //serwer
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        // Initialize Intents
        Intents.init();

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

    //remind password 2
    @Test
    public void testChangePasswordViewDisplayed() {

        onView(withId(R.id.wprowadz_kod)).check(matches(isDisplayed()));
        onView(withId(R.id.wy_lij_pono)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdzenie_link)).check(matches(isDisplayed()));

    }

    @Test
    public void testSignInButton() throws InterruptedException {

        String code = "1111";
        String baseUrl = "/api/mail/code";
        String email = "testmailmailmail@test.com";

        MockResponse response = new MockResponse()
                .setResponseCode(200)
                .setBody("{" +
                        "\"message\": \"API send back code that it sends to user's email\"" + "}");
        server.enqueue(response);

        onView(withId(R.id.wprowadz_kod)).perform(typeText(code), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie_link)).perform(click());

        RecordedRequest recordedRequest = server.takeRequest();
        String url = Uri.parse(baseUrl)
                .buildUpon()
                .appendQueryParameter("email", email)
                .build()
                .toString();

        assertEquals(url, recordedRequest.getPath());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wprowadzony kod jest niepoprawny")));

    }

    @Test
    public void testCreateAccountButton() {

        onView(withId(R.id.wy_lij_pono)).perform(click());

    }
}

