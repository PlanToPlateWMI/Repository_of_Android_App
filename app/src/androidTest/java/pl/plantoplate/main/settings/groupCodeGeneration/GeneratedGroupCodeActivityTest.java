package pl.plantoplate.main.settings.groupCodeGeneration;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.GeneratedGroupCodeActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class GeneratedGroupCodeActivityTest {

    @Rule
    public ActivityScenarioRule<GeneratedGroupCodeActivity> activityRule =
            new ActivityScenarioRule<>(GeneratedGroupCodeActivity.class);

    @Before
    public void setUp() {
        // Initialize Intents before each test
        Intents.init();
    }

    @After
    public void cleanup() {
        // Release Intents after each test
        Intents.release();
    }

    @Test
    public void groupCodeViewDisplaysCorrectly() {
        // Check if the group code view is displayed
        onView(withId(R.id.kod)).check(matches(isDisplayed()));
    }

    @Test
    public void applyButtonNavigatesToMainActivity() {
        // Perform a click on the apply button
        onView(withId(R.id.apply_button)).perform(click());

        // Check if the MainActivity is displayed
        //onView(withId(R.layout.activity_main_for_fragments)).check(matches(isDisplayed()));
        intended(hasComponent(ActivityMain.class.getName()));
    }
}

