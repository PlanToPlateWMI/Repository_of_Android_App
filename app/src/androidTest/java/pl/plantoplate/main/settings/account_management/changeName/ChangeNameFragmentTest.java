package pl.plantoplate.main.settings.account_management.changeName;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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

import java.io.IOException;

import mockwebserver3.MockWebServer;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.main.settings.account_management.change_name.ChangeNameFragment;

@RunWith(AndroidJUnit4.class)
public class ChangeNameFragmentTest {

    @Rule
    public ActivityScenarioRule<ActivityMain> fragmentRule =
            new ActivityScenarioRule<>(ActivityMain.class);

    //serwer
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        // Initialize Intents before each test
        Intents.init();

        // Navigate to the SettingsFragment
        navigateToChangeNameFragment();

        // server
        server = new MockWebServer();
        server.start(8080);
    }

    @After
    public void cleanup() throws IOException{
        // Release Intents after each test
        Intents.release();

        // Shutdown server
        server.shutdown();
    }

    public void navigateToChangeNameFragment() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, ChangeNameFragment.class, null)
                    .commit();
        });
    }

    @Test
    public void testViewDisplayed() {

        onView(withId(R.id.zmiana_imienia)).check(matches(isDisplayed()));
        onView(withId(R.id.wprowadz_imie)).check(matches(isDisplayed()));
        onView(withId(R.id.enterName)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zatwierdz)).check(matches(isDisplayed()));

    }

    @Test
    public void testSucc() {

        String name = "Alina";

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

    }

    @Test
    public void testNoName() {

        String name = "";

        onView(withId(R.id.enterName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.button_zatwierdz)).perform(click());

//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText("Pole nie może być puste")));

        onView(withText("Pole nie może być puste"))
                .check(matches(isDisplayed()));

    }

}
