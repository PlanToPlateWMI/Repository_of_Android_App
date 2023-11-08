package pl.plantoplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import pl.plantoplate.ui.main.ActivityMain;

public class RoutingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // Keep the splash screen visible for this Activity.
        splashScreen.setKeepOnScreenCondition(() -> true );
        startSomeNextActivity();
        finish();
    }

    private void startSomeNextActivity() {
        Intent intent = new Intent(this, ActivityMain.class);

        startActivity(intent);
    }
}