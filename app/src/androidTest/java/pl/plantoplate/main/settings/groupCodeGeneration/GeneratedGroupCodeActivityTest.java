package pl.plantoplate.main.settings.groupCodeGeneration;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockwebserver3.MockWebServer;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.settings.group_code_generation.GeneratedGroupCodeActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.Bundle;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class GeneratedGroupCodeActivityTest {

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
        navigateToGroupCodeActivityFragment();

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

    public void navigateToGroupCodeActivityFragment() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, GeneratedGroupCodeActivity.class, null)
                    .commit();
        });
    }

    //??
    @Test
    public void ifDisplay() {

        onView(withId(R.id.code_generation)).check(matches(isDisplayed()));
        onView(withId(R.id.forward_the_code)).check(matches(isDisplayed()));
        onView(withId(R.id.uwagaKod)).check(matches(isDisplayed()));
        onView(withId(R.id.apply_button)).check(matches(isDisplayed()));

    }

    @Test
    public void generatedGroupCodeFragment() {

        String groupCode = "999999";

        GeneratedGroupCodeActivity generatedGroupCodeActivity = new GeneratedGroupCodeActivity();
        Bundle bundle = new Bundle();
        bundle.putString("group_code", groupCode);
        generatedGroupCodeActivity.setArguments(bundle);

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, generatedGroupCodeActivity, null)
                    .commit();
        });
    }

    @Test
    public void applyButtonNavigatesToMainActivity() {
        // Perform a click on the apply button
        onView(withId(R.id.apply_button)).perform(click());

        // Check if the MainActivity is displayed
        //onView(withId(R.layout.activity_main_for_fragments)).check(matches(isDisplayed()));
        // intended(hasComponent(ActivityMain.class.getName()));

    }
}

