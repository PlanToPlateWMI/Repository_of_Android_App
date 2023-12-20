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
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.ui.registration.EmailConfirmActivity;
import pl.plantoplate.ui.registration.RegisterActivity;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityScenarioRule
            = new ActivityScenarioRule<>(RegisterActivity.class);

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


    //19/12/2023 - ok
    @Test
    public void testRegisterViewDisplayed() {

        onView(withId(R.id.enterName)).check(matches(isDisplayed()));
        onView(withId(R.id.enter_email)).check(matches(isDisplayed()));
        onView(withId(R.id.enter_password)).check(matches(isDisplayed()));
        onView(withId(R.id.checkbox_wyrazam_zgode)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zaloz_konto)).check(matches(isDisplayed()));
        onView(withId(R.id.masz_konto)).check(matches(isDisplayed()));
        onView(withId(R.id.checkbox_mam_lat_13)).check(matches(isDisplayed()));

    }

    //19/12/2023 - ok
    @Test
    public void testUserIsAlreadyExist() throws InterruptedException {

        String baseUrl = "/api/users/emails";
        String email = "plantoplatemobileapp@gmail.com";
        String password = "password";
        String name = "Plantest";

//        MockResponse response = new MockResponse()
//                .setResponseCode(409)
//                .setBody("Email is already taken");
//        server.enqueue(response);

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.checkbox_mam_lat_13)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());

//        RecordedRequest recordedRequest = server.takeRequest();
//
//        String url = Uri.parse(baseUrl)
//                .buildUpon()
//                .appendQueryParameter("email", email)
//                .build()
//                .toString();
//
//        assertEquals(url, recordedRequest.getPath());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Użytkownik o podanym adresie email już istnieje.")));
    }

    //19/12/2023 - ok
    @Test
    public void testUserIsRegister() throws InterruptedException {

        String baseUrl = "/api/users/emails";
        String email = "marinamarinatestmarinatesttest@gmail.com";
        String password = "password";
        String name = "Marina";

//        MockResponse response = new MockResponse()
//                .setResponseCode(200)
//                .setBody("User successfully registered and API sends back code that it sends yo user's email");
//        server.enqueue(response);

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.checkbox_mam_lat_13)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());

//        RecordedRequest recordedRequest = server.takeRequest();
//
//        String url = Uri.parse(baseUrl)
//                .buildUpon()
//                .appendQueryParameter("email", email)
//                .build()
//                .toString();
//
//        assertEquals(url, recordedRequest.getPath());
    }

    //19/12/2023 - ok
    @Test
    public void testSignInButton() {

        String name = "Karol";
        String email = "test1234@test.com";
        String password = "password";

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.checkbox_mam_lat_13)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());

    }

    //19/12/2023 - ok
    @Test
    public void testNoName() {

        String name = "";
        String email = "test1234@test.com";
        String password = "password";


        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.checkbox_mam_lat_13)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wprowadź imię użytkownika!")));

    }

    //19/12/2023 - ok
    @Test
    public void testNoEmail() {

        String name = "Karol";
        String email = "";
        String password = "password";

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.checkbox_mam_lat_13)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wprowadź adres email!")));

    }


    //19/12/2023 - ok
    @Test
    public void testNoPassword() {

        String name = "Karol";
        String email = "test1234@test.com";
        String password = "";

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.checkbox_mam_lat_13)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wprowadź hasło!")));

    }

    //19/12/2023 - ok
    @Test
    public void testNoLongPassword() {

        String name = "Karol";
        String email = "test1234@test.com";
        String password = "p";

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.checkbox_mam_lat_13)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Hasło musi być długie (co najmniej 7 znaków)")));

    }

    //19/12/2023 - ok
    @Test
    public void testNoChecked() {

        String name = "Karol";
        String email = "test1234@test.com";
        String password = "password";

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.checkbox_mam_lat_13)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Musisz wyrazić zgodę na przetwarzanie danych osobowych")));
    }

    //19/12/2023 - ok
    @Test
    public void testNoCheckedAge() {

        String name = "Karol";
        String email = "test1234@test.com";
        String password = "password";

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Musisz ukończyć 13 lat, aby założyć konto")));
    }



    //19/12/2023 - ok
    @Test
    public void testCreateAccountButton() {

        onView(withId(R.id.masz_konto)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));

    }

    //19/12/2023 - ok
    @Test
    public void testInvalidCredentials_mail() {

        String name = "Karol";
        String email = "invalidtest";
        String password = "invalid";

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.checkbox_mam_lat_13)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Email jest niepoprawny!")));

    }

    //19/12/2023 - ok
    @Test
    public void testInvalidCredentials_password() {

        String name = "Karol";
        String email = "invalidtest@gmail.com";
        String password = "i";

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.checkbox_mam_lat_13)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Hasło musi być długie (co najmniej 7 znaków)")));

    }
}

