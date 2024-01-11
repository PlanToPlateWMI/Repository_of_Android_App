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