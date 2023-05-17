package pl.plantoplate;

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
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.ui.registration.RegisterActivity;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

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
    public void testLoginViewDisplayed() {
        onView(withId(R.id.enter_mail)).check(matches(isDisplayed()));
        onView(withId(R.id.enter_pass)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zaloguj_sie)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zaloz_konto)).check(matches(isDisplayed()));
        onView(withId(R.id.przyp_haslo)).check(matches(isDisplayed()));
    }

    @Test
    public void testSignInButton() {
        onView(withId(R.id.enter_mail)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.enter_pass)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.button_zaloguj_sie)).perform(click());
    }

    @Test
    public void testCreateAccountButton() {
        onView(withId(R.id.button_zaloz_konto)).perform(click());
        intended(hasComponent(RegisterActivity.class.getName()));
    }

    @Test
    public void testInvalidCredentials() {
        onView(withId(R.id.enter_mail)).perform(typeText("invalidtest.com"), closeSoftKeyboard());
        onView(withId(R.id.enter_pass)).perform(typeText("invalid"), closeSoftKeyboard());
        onView(withId(R.id.button_zaloguj_sie)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Użytkownik o podanym adresie email nie istnieje!")));
    }
}

