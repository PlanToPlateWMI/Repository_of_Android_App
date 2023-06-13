/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.plantoplate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Objects;

import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.registration.EmailConfirmActivity;
import pl.plantoplate.ui.registration.GroupSelectActivity;
import pl.plantoplate.ui.registration.RegisterActivity;

/**
 * This activity is responsible for displaying the splash screen and initializing the application.
 * It is also responsible for redirecting the user to the appropriate activity.
 */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    private static final int SPLASH_TIME_OUT = 1000;

    /**
     * This method is responsible for displaying the splash screen and initializing the application.
     * It is also responsible for redirecting the user to the appropriate activity.
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Get the SharedPreferences object
        prefs = getSharedPreferences("prefs", 0);

        // Start the splash screen timer
        new Handler().postDelayed(() -> {
            // init the application
            ApplicationState applicationState = ApplicationState.valueOf(prefs.getString("applicationState", ApplicationState.INIT.toString()));
            initApplication(applicationState);

            // Close the splash activity
            finish();
        }, SPLASH_TIME_OUT);


        SharedPreferences.Editor editor = prefs.edit();

        String key = "theme";
        String light = "light";
        String dark = "dark";

        //check if SharedPreferences contains information about theme
        // if not - set default theme of device
        if (!prefs.contains(key)) {

            // get default theme of device
            int nightModeFlags =
                    getApplicationContext().getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;

            // add to SharedPreferences information about theme
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    editor.putString(key, dark);
                    editor.apply();
                    break;

                case Configuration.UI_MODE_NIGHT_NO:
                    editor.putString(key, light);
                    editor.apply();

                    break;
            }

        // if SharedPreferences contains information about theme - set theme from SharedPreferences
        }else {

            switch (prefs.getString(key, "")) {
                case "dark":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;

                case "light":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;

            }
        }
    }

    /**
     * This method is responsible for initializing the application.
     * It is also responsible for redirecting the user to the appropriate activity.
     *
     * @param applicationState The application state.
     */
    public void initApplication(ApplicationState applicationState) {
        if (applicationState == ApplicationState.INIT) {
            prefs.edit().putString("applicationState", ApplicationState.LOGIN.toString()).apply();

            // start the login activity
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        Intent activityIntent;
        switch (Objects.requireNonNull(applicationState)){
            case LOGIN:
                // start the login activity
                activityIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(activityIntent);
                break;
            case REGISTER:
                // start the register activity
                activityIntent = new Intent(SplashActivity.this, RegisterActivity.class);
                startActivity(activityIntent);
                break;
            case CONFIRM_MAIL:
                // start the confirm mail activity
                activityIntent = new Intent(SplashActivity.this, EmailConfirmActivity.class);
                startActivity(activityIntent);
                break;
            case GROUP_CHOOSE:
                // start the group choose activity
                activityIntent = new Intent(SplashActivity.this, GroupSelectActivity.class);
                startActivity(activityIntent);
                break;
            case MAIN_ACTIVITY:
                // start the main activity
                activityIntent = new Intent(SplashActivity.this, ActivityMain.class);
                startActivity(activityIntent);
                break;
        }

    }
}




