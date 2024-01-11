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
import pl.plantoplate.ui.login.remind_password.EnterEmailActivity;

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


    //remind password 1
    //19.12.2023 - ok
    @Test
    public void testChangePasswordViewDisplayed() {

        onView(withId(R.id.enter_the_name)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdz)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdz)).check(matches(isDisplayed()));

    }


    //19.12.2023 - ok
    @Test
    public void testChangePasswordButton() {

        onView(withId(R.id.enter_the_name)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

    }


    //26.12.2023 - ok
    @Test
    public void testNoUserWithSuchMail() throws InterruptedException {

        String emailApiPath = "api/users/emails";
        String email = "testtestmailmailmail@test.com";

        MockResponse responseCode = new MockResponse()
                .setResponseCode(200)
                .setBody("{\"message\": \"Doesn't exist active user with email\"}");
        server.enqueue(responseCode);

        onView(withId(R.id.enter_the_name)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

        onView(withText("Użytkownik o podanym adresie email nie istnieje!"))
                .check(matches(isDisplayed()));

        RecordedRequest recordedRequest = server.takeRequest();

        String url = Uri.parse("")
                .buildUpon()
                .appendEncodedPath(emailApiPath)
                .appendQueryParameter("email", email)
                .build()
                .toString();
        assertEquals(url, recordedRequest.getPath());

//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText("Użytkownik o podanym adresie email nie istnieje!")));

    }

    @Test
    public void testUserIsWithSuchMail() throws InterruptedException {

        String emailApiPath = "api/users/emails";
        String email = "plantoplate@test.com";

        MockResponse responseCode = new MockResponse()
                .setResponseCode(409)
                .setBody("{\"message\": \"Exists active user with emaill\"}");
        server.enqueue(responseCode);

        onView(withId(R.id.enter_the_name)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

        RecordedRequest recordedRequest = server.takeRequest();

        String url = Uri.parse("")
                .buildUpon()
                .appendEncodedPath(emailApiPath)
                .appendQueryParameter("email", email)
                .build()
                .toString();
        assertEquals(url, recordedRequest.getPath());

    }
}

