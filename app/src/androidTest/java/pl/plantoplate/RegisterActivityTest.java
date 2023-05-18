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
        onView(withId(R.id.enterName)).check(matches(isDisplayed()));
        onView(withId(R.id.enter_email)).check(matches(isDisplayed()));
        onView(withId(R.id.enter_password)).check(matches(isDisplayed()));
        onView(withId(R.id.checkbox_wyrazam_zgode)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zaloz_konto)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zaloguj_sie)).check(matches(isDisplayed()));
    }

    @Test
    public void testSignInButton() {
        onView(withId(R.id.enterName)).perform(typeText("Karol"), closeSoftKeyboard());
        onView(withId(R.id.enter_email)).perform(typeText("test@test.com"), closeSoftKeyboard());
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

