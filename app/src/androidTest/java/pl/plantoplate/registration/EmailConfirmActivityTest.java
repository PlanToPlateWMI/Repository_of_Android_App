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

import android.content.Context;
import android.content.SharedPreferences;
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
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import pl.plantoplate.R;
import pl.plantoplate.service.push_notification.PushNotificationService;
import pl.plantoplate.tools.MockHelper;
import pl.plantoplate.tools.ServiceHelper;
import pl.plantoplate.tools.SharedPreferencesHelper;
import pl.plantoplate.tools.TestDataJsonGenerator;
import pl.plantoplate.ui.registration.EmailConfirmActivity;
import pl.plantoplate.ui.registration.GroupSelectActivity;

@RunWith(AndroidJUnit4.class)
public class EmailConfirmActivityTest {

    @Rule
    public ActivityScenarioRule<EmailConfirmActivity> activityScenarioRule
            = new ActivityScenarioRule<>(EmailConfirmActivity.class);

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

    @Test
    public void testChangePasswordViewDisplayed() {

        onView(withId(R.id.wprowadz_kod)).check(matches(isDisplayed()));
        onView(withId(R.id.wy_lij_pono)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdzenie_link)).check(matches(isDisplayed()));

    }

    @Test
    public void testConfirmWithIncorrectCode() {

        SharedPreferences prefs = SharedPreferencesHelper
                .getSharedPreferencesFromActivityScenario(activityScenarioRule.getScenario());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("code", "1234");
        editor.apply();

        String code = "1111";


        onView(withId(R.id.edit_text)).perform(typeText(code), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie_link)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wprowadzony kod jest niepoprawny")));

    }

    @Test
    public void testConfirmWithCorrectCode() {

        SharedPreferences prefs = SharedPreferencesHelper
                .getSharedPreferencesFromActivityScenario(activityScenarioRule.getScenario());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("code", "1111");
        editor.apply();

        String code = "1111";

        onView(withId(R.id.edit_text)).perform(typeText(code), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie_link)).perform(click());

        intended(hasComponent(GroupSelectActivity.class.getName()));
    }

    @Test
    public void testGetNewConfirmCode() throws InterruptedException {

        SharedPreferences prefs = SharedPreferencesHelper
                .getSharedPreferencesFromActivityScenario(activityScenarioRule.getScenario());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", "example@email.com").apply();

        MockHelper.enqueueResponse(server, 200, TestDataJsonGenerator.generateResponseCode(1234));

        onView(withId(R.id.wy_lij_pono)).perform(click());

        // check snack bar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wysłano nowy kod")));

        Uri.Builder builder = Uri.parse("/api/mail/code").buildUpon();
        builder.appendQueryParameter("email", "example@email.com");
        builder.appendQueryParameter("type", "registration");
        String url = builder.build().toString();

        RecordedRequest request = server.takeRequest();
        assertEquals(url, request.getPath());
        assertEquals(prefs.getString("code", ""), "1234");
    }
}