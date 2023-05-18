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
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.registration.GroupEnterActivity;
import pl.plantoplate.ui.registration.GroupSelectActivity;
import pl.plantoplate.ui.registration.RegisterActivity;

@RunWith(AndroidJUnit4.class)
public class GroupSelectActivityTest {

    @Rule
    public ActivityScenarioRule<GroupSelectActivity> activityScenarioRule
            = new ActivityScenarioRule<>(GroupSelectActivity.class);

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
    public void testGroupSelectViewDisplayed() {
        onView(withId(R.id.button_mam_zaproszenie)).check(matches(isDisplayed()));
        onView(withId(R.id.button_swoja_grupa)).check(matches(isDisplayed()));
    }

    @Test
    public void testOldGroupButton() {
        onView(withId(R.id.button_mam_zaproszenie)).perform(click());
        intended(hasComponent(GroupEnterActivity.class.getName()));
    }

    //need database
    @Test
    public void testNewGroupButton() {
        onView(withId(R.id.button_swoja_grupa)).perform(click());
        intended(hasComponent(ActivityMain.class.getName()));
    }
}


