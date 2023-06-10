package pl.plantoplate.main.settings.groupCodeGeneration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.main.settings.SettingsFragment;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.GeneratedGroupCodeActivity;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.GroupCodeTypeActivity;

@RunWith(AndroidJUnit4.class)
public class GroupCodeTypeActivityTest {

    @Rule
    public ActivityScenarioRule<ActivityMain> fragmentRule = new ActivityScenarioRule<>(ActivityMain.class);

    @Before
    public void setUp() {
        // Initialize Intents before each test
        Intents.init();

        // Navigate to the GroupCodeTypeFragment
        navigateToGroupCodeTypeFragment();
    }

    @After
    public void cleanup() {
        // Release Intents after each test
        Intents.release();
    }

    public void navigateToGroupCodeTypeFragment() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, SettingsFragment.class, null)
                    .commit();
        });
    }

    @Test
    public void generateChildCodeButtonClicked() {
        // Perform a click on the child code button
        onView(withId(R.id.code_for_child)).perform(click());

        intended(hasComponent(GeneratedGroupCodeActivity.class.getName()));

        //TODO: check that TextView has the 6-digit code
    }

    @Test
    public void generateAdultCodeButtonClicked() {
        // Perform a click on the adult code button
        onView(withId(R.id.code_for_adult)).perform(click());

        //TODO: check that TextView has the 6-digit code
    }
}
