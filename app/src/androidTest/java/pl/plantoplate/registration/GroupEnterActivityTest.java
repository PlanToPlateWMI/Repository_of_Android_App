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
import pl.plantoplate.tools.TestHelper;
import pl.plantoplate.ui.registration.GroupEnterActivity;

@RunWith(AndroidJUnit4.class)
public class GroupEnterActivityTest {

    @Rule
    public ActivityScenarioRule<GroupEnterActivity> activityScenarioRule
            = new ActivityScenarioRule<>(GroupEnterActivity.class);

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

    //19.12.2023 - ok
    @Test
    public void testRegisterViewDisplayed() {

        onView(withId(R.id.wprowadz_kod)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdz)).check(matches(isDisplayed()));

    }

    //26.12.2023 - ok
    @Test
    public void testGroupCode() throws InterruptedException {

        String code = "111111";
        String email = "marinamarinatestmarinatesttest@gmail.com";
        String emailApiPath = "api/invite-codes";
        String password = "password";

        MockResponse responseCode = new MockResponse()
                .setResponseCode(200)
                .setBody("{\"message\": \"API sends back JWT Token and role\"}");
        server.enqueue(responseCode);

        onView(withId(R.id.wprowadz_kod)).perform(typeText(code), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

        RecordedRequest recordedRequest = server.takeRequest();

        String url = Uri.parse("")
                .buildUpon()
                .appendEncodedPath(emailApiPath)
                .build()
                .toString();
        assertEquals(url, recordedRequest.getPath());

    }


    //26.12.2023 - ok
    @Test
    public void testGroupCodeFail() throws InterruptedException {

        String code = "111111";
        String email = "marinamarinatestmarinatesttest@gmail.com";
        String emailApiPath = "api/invite-codes";
        String password = "password";


        MockResponse responseCode = new MockResponse()
                .setResponseCode(400)
                .setBody("{\"message\": \"Account with this email doesn't exist or type of email is invalid\"}");
        server.enqueue(responseCode);


        onView(withId(R.id.wprowadz_kod)).perform(typeText(code), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

        RecordedRequest recordedRequest = server.takeRequest();

        String url = Uri.parse("")
                .buildUpon()
                .appendEncodedPath(emailApiPath)
                .build()
                .toString();
        assertEquals(url, recordedRequest.getPath());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Niepoprawny kod grupy.")));

    }
}


