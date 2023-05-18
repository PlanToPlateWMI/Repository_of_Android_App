package pl.plantoplate.remindPasswordTests;

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
        import pl.plantoplate.ui.login.remindPassword.ChangePasswordActivity;
        import pl.plantoplate.ui.login.remindPassword.EnterEmailActivity;
        import pl.plantoplate.ui.registration.RegisterActivity;

@RunWith(AndroidJUnit4.class)
public class EnterMailActivityTest {

    @Rule
    public ActivityScenarioRule<EnterEmailActivity> activityScenarioRule
            = new ActivityScenarioRule<>(EnterEmailActivity.class);

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


    //remind password 1
    @Test
    public void testChangePasswordViewDisplayed() {
        onView(ViewMatchers.withId(R.id.enter_the_name)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdz)).check(matches(isDisplayed()));
    }

    @Test
    public void testChangePasswordButton() {
        onView(withId(R.id.enter_the_name)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());
    }

    //with database

//    @Test
//    public void testInvalidCredentials() {
//        onView(withId(R.id.enter_the_name)).perform(typeText("testinvalid"), closeSoftKeyboard());
//        onView(withId(R.id.button_zatwierdz)).perform(click());
//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText("Użytkownik o podanym adresie email nie istnieje!")));
//    }
}

