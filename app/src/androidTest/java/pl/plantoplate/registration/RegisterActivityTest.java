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

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.plantoplate.R;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.ui.registration.EmailConfirmActivity;
import pl.plantoplate.ui.registration.RegisterActivity;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityScenarioRule
            = new ActivityScenarioRule<>(RegisterActivity.class);

    @Before
    public void setUp() {
        // Initialize Intents
        Intents.init();
    }

    @After
    public void tearDown() {
        // Release Intents
        Intents.release();
    }

    @Test
    public void testRegisterViewDisplayed() {
        onView(ViewMatchers.withId(R.id.enterName)).check(matches(isDisplayed()));
        onView(withId(R.id.enter_email)).check(matches(isDisplayed()));
        onView(withId(R.id.enter_password)).check(matches(isDisplayed()));
        onView(withId(R.id.checkbox_wyrazam_zgode)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zaloz_konto)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zaloguj_sie)).check(matches(isDisplayed()));
    }

    @Test
    public void testSignInButton() {
        onView(withId(R.id.enterName)).perform(typeText("Karol"), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText("test123@test.com"), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());
    }

    @Test
    public void testCreateAccountButton() {
        onView(withId(R.id.button_zaloguj_sie)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void testInvalidCredentials_mail() {
        onView(withId(R.id.enterName)).perform(typeText("Karol"), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText("invalidtest"), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText("invalid"), closeSoftKeyboard());
        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Email jest niepoprawny!")));
    }

    @Test
    public void testInvalidCredentials_password() {
        onView(withId(R.id.enterName)).perform(typeText("Karol"), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText("invalidtest@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.enter_password)).perform(typeText("i"), closeSoftKeyboard());
        onView(withId(R.id.checkbox_wyrazam_zgode)).perform(click());
        onView(withId(R.id.button_zaloz_konto)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Hasło musi być długie (co najmniej 7 znaków)")));
    }
}

