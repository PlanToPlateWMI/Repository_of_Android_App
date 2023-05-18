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

import pl.plantoplate.R;
import pl.plantoplate.ui.login.remindPassword.ChangePasswordActivity;

@RunWith(AndroidJUnit4.class)
public class ChangePasswordActivityTest {

    @Rule
    public ActivityScenarioRule<ChangePasswordActivity> activityScenarioRule
            = new ActivityScenarioRule<>(ChangePasswordActivity.class);

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

    //remind password 3
    @Test
    public void testChangePasswordViewDisplayed() {
        onView(ViewMatchers.withId(R.id.nowe_haslo)).check(matches(isDisplayed()));
        onView(withId(R.id.nowe_haslo2)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdzenie)).check(matches(isDisplayed()));
    }

    @Test
    public void testSignInButton() {
        onView(withId(R.id.nowe_haslo)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.nowe_haslo2)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie)).perform(click());
    }

    // eye
    @Test
    public void testInvalidNotTheSameCredentials() {
        onView(withId(R.id.nowe_haslo)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.nowe_haslo2)).perform(typeText("password1"), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie)).perform(click());
//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText("Hasła nie są takie same")));
    }

    @Test
    public void testInvalidCredentials() {
        onView(withId(R.id.nowe_haslo)).perform(typeText("p"), closeSoftKeyboard());
        onView(withId(R.id.nowe_haslo2)).perform(typeText("p"), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdzenie)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Hasło musi mieć co najmniej 7 znaków")));
    }
}

