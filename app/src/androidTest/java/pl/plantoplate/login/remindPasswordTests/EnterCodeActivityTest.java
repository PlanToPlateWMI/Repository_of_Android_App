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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.net.Uri;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
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
import pl.plantoplate.tools.TestHelper;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.ui.login.remindPassword.ChangePasswordActivity;
import pl.plantoplate.ui.login.remindPassword.EnterCodeActivity;
import pl.plantoplate.ui.login.remindPassword.EnterEmailActivity;
import pl.plantoplate.ui.registration.RegisterActivity;

@RunWith(AndroidJUnit4.class)
public class EnterCodeActivityTest {

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

        // test Helper
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TestHelper.disableService(appContext, PushNotificationService.class);
    }

    @After
    public void tearDown() throws IOException {
        // Release Intents
        Intents.release();

        // Shutdown server
        server.shutdown();

        // test Helper
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TestHelper.enableService(appContext, PushNotificationService.class);
    }

    //remind password 2
    //19.12.2023 - ok
    @Test
    public void testChangePasswordViewDisplayed() {

        onView(withId(R.id.wprowadz_kod)).check(matches(isDisplayed()));
        onView(withId(R.id.wy_lij_pono)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdzenie_link)).check(matches(isDisplayed()));

    }

    //19.12.2023 - ok
    @Test
    public void testEnterCodeButton() {

        String code = "1111";

        onView(withId(R.id.wprowadz_kod)).perform(typeText(code), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie_link)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wprowadzony kod jest niepoprawny")));

    }

    //19.12.2023 - ok
    @Test
    public void testAgainButton() {

        onView(withId(R.id.wy_lij_pono)).perform(click());

    }

//    //19.12.2023 - ok
//    @Test
//    public void testNoSuchUser() throws InterruptedException {
//
//        String code = "1111";
//        String emailApiPath = "api/users/email";
//
//        MockResponse responseCode = new MockResponse()
//                .setResponseCode(400)
//                .setBody("{\"message\": \"Account with this email doesn't exist\"}");
//        server.enqueue(responseCode);
//
//        onView(withId(R.id.wprowadz_kod)).perform(typeText(code), closeSoftKeyboard());
//        onView(withId(R.id.button_zatwierdzenie_link)).perform(click());
//
//        RecordedRequest recordedRequest = server.takeRequest();
//
//        String url = Uri.parse("")
//                .buildUpon()
//                .appendEncodedPath(emailApiPath)
//                .build()
//                .toString();
//        assertEquals(url, recordedRequest.getPath());
//
//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText("Wprowadzony kod jest niepoprawny")));
//
//    }
}

