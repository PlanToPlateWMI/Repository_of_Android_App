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

package pl.plantoplate.login;

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

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Timer;

import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.Message;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.ui.registration.RegisterActivity;
import timber.log.Timber;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

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

    //19.12.2023 - ok
    @Test
    public void testLoginViewDisplayed() {

        onView(withId(R.id.enter_mail)).check(matches(isDisplayed()));
        onView(withId(R.id.enter_pass)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zaloguj_sie)).check(matches(isDisplayed()));
        onView(withId(R.id.nie_masz_konta)).check(matches(isDisplayed()));
        onView(withId(R.id.przyp_haslo)).check(matches(isDisplayed()));

    }

    //19.12.2023 - ok
    @Test
    public void testNoUserExists() throws InterruptedException {

        String email = "test@test.com";
        String password = "password";
        String baseUrl = "/api/auth/signin";
        String message = "Account with this email doesn't exist";

//        MockResponse response = new MockResponse()
//                .setResponseCode(400)
//                .setBody(new Message(message).toString());
//        server.enqueue(response);

        onView(withId(R.id.enter_mail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_pass)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.button_zaloguj_sie)).perform(click());

        Thread.sleep(1000);

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Użytkownik o podanym adresie email nie istnieje!")));

//        RecordedRequest recordedRequest = server.takeRequest();
//        assertEquals(baseUrl, recordedRequest.getPath());

    }


    //19.12.2023 - ok
    @Test
    public void testPasswordIsNotCorrect() throws InterruptedException {

        String email = "plantoplatemobileapp@gmail.com";
        String password = "invalid";
        String baseUrl = "/api/auth/signin";

//        MockResponse response = new MockResponse()
//                .setResponseCode(403);
//        server.enqueue(response);

        onView(withId(R.id.enter_mail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_pass)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.button_zaloguj_sie)).perform(click());

//        RecordedRequest recordedRequest = server.takeRequest();
//        assertEquals(baseUrl, recordedRequest.getPath());

        Thread.sleep(1000);

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Nieprawidłowe hasło!")));

    }

    //19.12.2023 - ok
    @Test
    public void testUserExists() throws InterruptedException {

        String email = "plantoplatemobileapp@gmail.com";
        String password = "plantoplate";
        String baseUrl = "/api/auth/signin";

//        MockResponse response = new MockResponse()
//                .setResponseCode(200)
//                .setBody("User successfully login and API sends back JWT Token and rolet");
//        server.enqueue(response);

        onView(withId(R.id.enter_mail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_pass)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.button_zaloguj_sie)).perform(click());

//        RecordedRequest recordedRequest = server.takeRequest();
//        assertEquals(baseUrl, recordedRequest.getPath());

    }


    //19.12.2023 - ok
    @Test
    public void testCreateAccountButton() {

        onView(withId(R.id.nie_masz_konta)).perform(click());
        intended(hasComponent(RegisterActivity.class.getName()));

    }

    //19.12.2023 - ok
    @Test
    public void testNoEmail() {

        String email = "";
        String password = "password";

        onView(withId(R.id.enter_mail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_pass)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.button_zaloguj_sie)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wprowadź adres email")));

    }

    //19.12.2023 - ok
    @Test
    public void testNoPassword() {

        String email = "test@test.com";
        String password = "";

        onView(withId(R.id.enter_mail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_pass)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.button_zaloguj_sie)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wprowadź hasło")));

    }
}

