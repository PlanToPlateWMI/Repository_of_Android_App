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
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.net.Uri;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

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
import pl.plantoplate.service.push_notification.PushNotificationService;
import pl.plantoplate.tools.ServiceHelper;
import pl.plantoplate.ui.login.remind_password.ChangePasswordActivity;

@RunWith(AndroidJUnit4.class)
public class ChangePasswordActivityTest {

    @Rule
    public ActivityScenarioRule<ChangePasswordActivity> activityScenarioRule
            = new ActivityScenarioRule<>(ChangePasswordActivity.class);

    //serwer
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        // Initialize Intents
        Intents.init();

        // server
        server = new MockWebServer();
        server.start(8080);

        // test Helper
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ServiceHelper.disableService(appContext, PushNotificationService.class);
    }

    @After
    public void tearDown() throws IOException {
        // Release Intents
        Intents.release();

        // Shutdown server
        server.shutdown();

        // test Helper
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ServiceHelper.enableService(appContext, PushNotificationService.class);
    }

    //remind password 3
    //19.12.2023 - ok
    @Test
    public void testChangePasswordViewDisplayed() {

        onView(withId(R.id.nowe_haslo)).check(matches(isDisplayed()));
        onView(withId(R.id.nowe_haslo2)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdzenie)).check(matches(isDisplayed()));

    }

    //19.12.2023 - ok
    @Test
    public void testInvalidCredentials() {

        String password = "p";

        onView(withId(R.id.nowe_haslo)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.nowe_haslo2)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Hasło musi mieć co najmniej 7 znaków")));

    }

    //19.12.2023 - ok
    @Test
    public void testSignInButton() {
        String password = "password";

        onView(withId(R.id.nowe_haslo)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.nowe_haslo2)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie)).perform(click());

    }

    //19.12.2023 - ok
    @Test
    public void testInvalidNotTheSameCredentials() {

        String password = "password";
        String password2 = "password1";

        onView(withId(R.id.nowe_haslo)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.nowe_haslo2)).perform(typeText(password2), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie)).perform(click());

        onView(withId(R.id.nowe_haslo2))
                .check(matches(hasErrorText("Hasła nie są takie same")));

    }

    @Test
    public void testSuccecfulPasswordChange() throws InterruptedException {

        String password = "password";
        String emailApiPath = "api/auth/password/reset";


        MockResponse responseCode = new MockResponse()
                .setResponseCode(200)
                .setBody("{\"message\": \"API update password\"}");
        server.enqueue(responseCode);

        onView(withId(R.id.nowe_haslo)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.nowe_haslo2)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie)).perform(click());

        RecordedRequest recordedRequest = server.takeRequest();

        String url = Uri.parse("")
                .buildUpon()
                .appendEncodedPath(emailApiPath)
                .build()
                .toString();
        assertEquals(url, recordedRequest.getPath());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Pomyślnie zmieniono hasło!")));

    }

    //26.12.2023 - ok
    @Test
    public void testNotSuccecfulPasswordChange() throws InterruptedException {

        String password = "password";
        String emailApiPath = "api/auth/password/reset";

        MockResponse responseCode = new MockResponse()
                .setResponseCode(400)
                .setBody("{\"message\": \"Account with this email doesn't exist\"}");
        server.enqueue(responseCode);

        onView(withId(R.id.nowe_haslo)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.nowe_haslo2)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie)).perform(click());

        RecordedRequest recordedRequest = server.takeRequest();

        String url = Uri.parse("")
                .buildUpon()
                .appendEncodedPath(emailApiPath)
                .build()
                .toString();
        assertEquals(url, recordedRequest.getPath());

    }
}

