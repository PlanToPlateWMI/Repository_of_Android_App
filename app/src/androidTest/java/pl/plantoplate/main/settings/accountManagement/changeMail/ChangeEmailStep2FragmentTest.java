package pl.plantoplate.main.settings.accountManagement.changeMail;

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
import pl.plantoplate.ui.main.settings.accountManagement.changeEmail.ChangeEmailStep2Fragment;
import pl.plantoplate.ui.main.settings.accountManagement.changePassword.PasswordChangeNewPasswords;

@RunWith(AndroidJUnit4.class)
public class ChangeEmailStep2FragmentTest {

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
        navigateToEmail2();

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

    public void navigateToEmail2() {

        fragmentRule.getScenario().onActivity(activity -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, ChangeEmailStep2Fragment.class, null)
                    .commit();
        });
    }

    @Test
    public void testOldPasswordViewDisplayed() {

        onView(withId(R.id.zmiana_emeil)).check(matches(isDisplayed()));

        onView(withId(R.id.wprowadz_nowy_email)).check(matches(isDisplayed()));
        onView(withId(R.id.enterNewMail)).check(matches(isDisplayed()));

        onView(withId(R.id.wprowadz_nowy_email_ponownie)).check(matches(isDisplayed()));
        onView(withId(R.id.enterNewMailAgain)).check(matches(isDisplayed()));

        onView(withId(R.id.button_zatwierdz)).check(matches(isDisplayed()));

    }

    @Test
    public void testNoEmailDisplayed() {

        String email = "";

        onView(withId(R.id.enterNewMail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enterNewMailAgain)).perform(typeText(email), closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Pole nie może być puste")));
    }

    @Test
    public void testEmailsNotTheSame() {

        String email1 = "testmail@gmail.com";
        String email2 = "testmail2@gmail.com";

        onView(withId(R.id.enterNewMail)).perform(typeText(email1), closeSoftKeyboard());
        onView(withId(R.id.enterNewMailAgain)).perform(typeText(email2), closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Adresy email nie są takie same")));
    }

    @Test
    public void testEmailsNotOk1() {

        String email = "tesqedrtmail";

        onView(withId(R.id.enterNewMail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enterNewMailAgain)).perform(typeText(email), closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wprowadź poprawny adres email")));
    }

    @Test
    public void testEmailsNotOk2() {

        String email = "tesqedrtmail@";

        onView(withId(R.id.enterNewMail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enterNewMailAgain)).perform(typeText(email), closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wprowadź poprawny adres email")));
    }


    @Test
    public void testEmailsNotOk3() {

        String email = "+++tesqedrtmail";

        onView(withId(R.id.enterNewMail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enterNewMailAgain)).perform(typeText(email), closeSoftKeyboard());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Wprowadź poprawny adres email")));
    }

    @Test
    public void testSucc() {

        String email = "testmail@gmail.com";

        onView(withId(R.id.enterNewMail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.enterNewMailAgain)).perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.button_zatwierdz)).perform(click());
    }

}
