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
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.registration.EmailConfirmActivity;
import pl.plantoplate.ui.registration.GroupEnterActivity;
import pl.plantoplate.ui.registration.GroupSelectActivity;
import pl.plantoplate.ui.registration.RegisterActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    private static final int SPLASH_TIME_OUT = 1000;

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
    }

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




