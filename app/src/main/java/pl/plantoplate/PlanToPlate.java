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

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PlanToPlate extends Application {

    private static PlanToPlate instance;

    /**
     * Called when the application is starting. This is where you should initialize
     * any resources that your app will need throughout its lifecycle.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        if(instance == null){
            instance = this;
        }
    }

    /**
     * Returns the instance of the PlanToPlate class.
     *
     * @return The instance of the PlanToPlate class.
     */
    public static PlanToPlate getInstance(){
        return instance;
    }

    /**
     * Checks whether the device has network connectivity.
     *
     * @return {@code true} if the device has network connectivity, {@code false} otherwise.
     */
    public static boolean hasNetwork(){
        return instance.isNetworkConnected();
    }

    /**
     * Checks whether the device is currently connected to a network.
     *
     * @return {@code true} if the device is connected to a network, {@code false} otherwise.
     */
    private boolean isNetworkConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
