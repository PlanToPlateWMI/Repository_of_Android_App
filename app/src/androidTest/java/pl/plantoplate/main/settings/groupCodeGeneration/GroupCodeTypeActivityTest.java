package pl.plantoplate.main.settings.groupCodeGeneration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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
import pl.plantoplate.ui.main.settings.group_code_generation.GeneratedGroupCodeActivity;
import pl.plantoplate.ui.main.settings.group_code_generation.GroupCodeTypeActivity;

@RunWith(AndroidJUnit4.class)
public class GroupCodeTypeActivityTest {

    @Rule
    public ActivityScenarioRule<ActivityMain> fragmentRule = new ActivityScenarioRule<>(ActivityMain.class);

    //serwer
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        // Initialize Intents before each test
        Intents.init();

        // Navigate to the SettingsFragment
        navigateToGroupCodeTypeFragment();

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

    public void navigateToGroupCodeTypeFragment() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, GroupCodeTypeActivity.class, null)
                    .commit();
        });
    }

    @Test
    public void generateChildCodeButtonDisplayed() {

        onView(withId(R.id.code_generation)).check(matches(isDisplayed()));
        onView(withId(R.id.choose_an_option)).check(matches(isDisplayed()));
        onView(withId(R.id.uwaga)).check(matches(isDisplayed()));
        // Check that the child code button is displayed
        onView(withId(R.id.code_for_child)).check(matches(isDisplayed()));
        onView(withId(R.id.code_for_adult)).check(matches(isDisplayed()));

    }

    public void navigateToGeneratedGroupCodeActivity() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, GeneratedGroupCodeActivity.class, null)
                    .commit();
        });
    }

    @Test
    public void generateChildCodeButtonClicked() {
        // Perform a click on the child code button
        onView(withId(R.id.code_for_child)).perform(click());

        //intended(hasComponent(GeneratedGroupCodeActivity.class.getName()));
        //navigateToGeneratedGroupCodeActivity();

    }

    public void navigateToGenerateAdultCodeButtonClickedy() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, GeneratedGroupCodeActivity.class, null)
                    .commit();
        });
    }

    @Test
    public void generateAdultCodeButtonClicked() {
        // Perform a click on the adult code button
        onView(withId(R.id.code_for_adult)).perform(click());

        //intended(hasComponent(GeneratedGroupCodeActivity.class.getName()));
        //navigateToGenerateAdultCodeButtonClickedy();

    }
}
