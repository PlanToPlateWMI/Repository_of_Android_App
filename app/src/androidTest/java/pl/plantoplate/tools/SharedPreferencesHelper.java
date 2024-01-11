package pl.plantoplate.tools;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.core.app.ActivityScenario;

public class SharedPreferencesHelper {

    private SharedPreferencesHelper() {}

    public static SharedPreferences getSharedPreferencesFromActivityScenario(
            ActivityScenario<? extends AppCompatActivity> activityScenario) {

        final SharedPreferences[] sharedPreferences = {null};

        // Get the SharedPreferences instance
        activityScenario.onActivity(activity -> sharedPreferences[0] = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE));

        return sharedPreferences[0];
    }

    public static void setUpSharedPreferences(SharedPreferences prefs) {

        // user info
        String name = "test_name";
        String email = "test_email";
        String password = "test_password";
        String role = "test_role";
        String token = "test_token";

        // theme
        String theme = "light";

        prefs.edit().putString("name", name);
        prefs.edit().putString("email", email);
        prefs.edit().putString("password", password);
        prefs.edit().putString("role", role);
        prefs.edit().putString("token", token);
        prefs.edit().putString("theme", theme).apply();
    }
}
