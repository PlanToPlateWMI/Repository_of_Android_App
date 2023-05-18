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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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
import pl.plantoplate.ui.login.remindPassword.EnterCodeActivity;

@RunWith(AndroidJUnit4.class)
public class EnterCodeActivityTest {

    @Rule
    public ActivityScenarioRule<EnterCodeActivity> activityScenarioRule
            = new ActivityScenarioRule<>(EnterCodeActivity.class);

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

    //remind password 2
    @Test
    public void testChangePasswordViewDisplayed() {
        onView(ViewMatchers.withId(R.id.wprowadz_kod)).check(matches(isDisplayed()));
        onView(withId(R.id.wy_lij_pono)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdzenie_link)).check(matches(isDisplayed()));
    }

    //no database
//    @Test
//    public void testSignInButton() {
//        onView(withId(R.id.wprowadz_kod)).perform(typeText("1111"), closeSoftKeyboard());
//        onView(withId(R.id.button_zatwierdzenie_link)).perform(click());
//    }
//

    //not implemented
//    @Test
//    public void testCreateAccountButton() {
//        onView(withId(R.id.wy_lij_pono)).perform(click());
//        intended(hasComponent(EnterEmailActivity.class.getName()));
//    }

    //no snackbar
//    @Test
//    public void testInvalidCredentials() {
//        onView(withId(R.id.enter_mail)).perform(typeText("invalidtest.com"), closeSoftKeyboard());
//        onView(withId(R.id.enter_pass)).perform(typeText("invalid"), closeSoftKeyboard());
//        onView(withId(R.id.button_zaloguj_sie)).perform(click());
//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText("Użytkownik o podanym adresie email nie istnieje!")));
//    }
}

