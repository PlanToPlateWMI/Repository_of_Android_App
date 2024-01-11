package pl.plantoplate.main.settings.help_manager;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static pl.plantoplate.tools.SharedPreferencesHelper.getSharedPreferencesFromActivityScenario;
import static pl.plantoplate.tools.SharedPreferencesHelper.setUpSharedPreferences;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import mockwebserver3.MockWebServer;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.user.Role;
import pl.plantoplate.service.push_notification.PushNotificationService;
import pl.plantoplate.tools.MockHelper;
import pl.plantoplate.tools.ServiceHelper;
import pl.plantoplate.tools.TestDataJsonGenerator;
import pl.plantoplate.ui.main.ActivityMain;

@RunWith(AndroidJUnit4.class)
public class HelpManagerFragmentTest {

    private ActivityScenario<ActivityMain> activityScenario;
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        // Initialize Intents before each test
        Intents.init();

        // server
        server = new MockWebServer();
        server.start(8080);

        MockHelper.enqueueResponse(server,  200, TestDataJsonGenerator.generateUserInfo(Role.ROLE_ADMIN));
        MockHelper.enqueueResponse(server,  200, TestDataJsonGenerator.generateProducts());
        MockHelper.enqueueResponse(server,  200, TestDataJsonGenerator.generateUserInfos());
        MockHelper.enqueueResponse(server,  200, TestDataJsonGenerator.generateUserInfo(Role.ROLE_ADMIN));

        // start activity
        activityScenario = ActivityScenario.launch(ActivityMain.class);

        // disable firebase service
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ServiceHelper.disableService(appContext, PushNotificationService.class);

        setUpSharedPreferences(getSharedPreferencesFromActivityScenario(activityScenario));

        // Navigate to the SettingsFragment
        onView(withId(R.id.settings)).perform(click());
    }

    @After
    public void cleanup() throws IOException {
        // test Helper
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ServiceHelper.enableService(appContext, PushNotificationService.class);

        // Release Intents after each test
        Intents.release();

        // Shutdown server
        server.shutdown();
    }

    @Test
    public void testHelpDisplayed() {

        // Perform a click on the generate group code button
        onView(withId(R.id.button_help)).perform(click());

        // check all buttons are displayed
        onView(withId(R.id.button_wygenerowanie_kodu)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zmiana_danych)).check(matches(isDisplayed()));
        onView(withId(R.id.button_zarzadyanie_uyztkownikamu)).check(matches(isDisplayed()));
        onView(withId(R.id.button_about_us)).check(matches(isDisplayed()));
        onView(withId(R.id.button_help)).check(matches(isDisplayed()));
        onView(withId(R.id.button_wyloguj)).check(matches(isDisplayed()));
    }
}
