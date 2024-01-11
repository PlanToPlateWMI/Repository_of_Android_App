package pl.plantoplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import pl.plantoplate.ui.main.ActivityMain;

/**
 * This class is responsible for displaying the splash screen and initializing the application.
 * It is also responsible for redirecting the user to the appropriate activity.
 */
public class RoutingActivity extends AppCompatActivity {

    /**
     * This method is responsible for displaying the splash screen and initializing the application.
     * It is also responsible for redirecting the user to the appropriate activity.
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // Keep the splash screen visible for this Activity.
        splashScreen.setKeepOnScreenCondition(() -> true );
        startSomeNextActivity();
        finish();
    }

    /**
     * This method is responsible for redirecting the user to the appropriate activity.
     */
    private void startSomeNextActivity() {
        Intent intent = new Intent(this, ActivityMain.class);

        startActivity(intent);
    }
}