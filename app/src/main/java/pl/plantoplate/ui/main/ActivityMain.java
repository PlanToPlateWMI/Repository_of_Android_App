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
package pl.plantoplate.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Optional;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.FCMToken;
import pl.plantoplate.data.remote.repository.FCMTokenRepository;
import pl.plantoplate.databinding.ActivityMainForFragmentsBinding;
import pl.plantoplate.ui.main.calendar.CalendarFragment;
import pl.plantoplate.ui.main.recipes.RecipesFragment;
import pl.plantoplate.ui.main.settings.SettingsFragment;
import pl.plantoplate.ui.main.shopping_list.ShoppingListFragment;
import pl.plantoplate.ui.main.storage.StorageFragment;
import timber.log.Timber;

/**
 * This is the main activity of the application. It is responsible for displaying the bottom navigation bar and
 * switching between the fragments.
 */
public class ActivityMain extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private CompositeDisposable compositeDisposable;
    private static final int MENU_CALENDAR = R.id.calendar;
    private static final int MENU_COTTAGE = R.id.cottage;
    private static final int MENU_SHOPPING_CART = R.id.shopping_cart;
    private static final int MENU_RECEIPT = R.id.receipt_long;
    private static final int MENU_SETTINGS = R.id.settings;

    /**
     * Called when the activity is starting. Inflates the layout using view binding
     * and sets the initial fragment to be displayed.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it
     *                           most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainForFragmentsBinding binding = ActivityMainForFragmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        compositeDisposable = new CompositeDisposable();

        binding.bottomNavigationView.setOnItemSelectedListener(this);
        if (savedInstanceState == null) {
            binding.bottomNavigationView.setSelectedItemId(MENU_SHOPPING_CART);
        }
        checkActions(getIntent().getExtras());
        Timber.d("Activity created");
    }

    /**
     * Called when a navigation item in the bottom navigation view is selected.
     *
     * @param item The menu item that was selected.
     * @return True if the item was handled, false otherwise.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Timber.d("Selected navigation item: %s", item.getTitle());
        if(item.getItemId() == MENU_CALENDAR){
            replaceFragment(new CalendarFragment(), "CALENDAR");
        }else if(item.getItemId() == MENU_COTTAGE){
            replaceFragment(new StorageFragment(), "STORAGE");
        }else if(item.getItemId() == MENU_SHOPPING_CART){
            replaceFragment(new ShoppingListFragment(), "SHOPPING_LIST");
        }else if(item.getItemId() == MENU_RECEIPT){
            replaceFragment(new RecipesFragment(), "RECIPE");
        }else if(item.getItemId() == MENU_SETTINGS){
            replaceFragment(new SettingsFragment(), "SETTINGS");
        }
        return true;
    }

    /**
     * Replaces the current fragment with the given one.
     */
    public void checkActions(Bundle bundle){
        if(bundle != null){
            String action = Optional.ofNullable(bundle.getString("action")).orElse("");

            if (action.equals("updateFcmToken")){
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Timber.w("Fetching FCM registration token failed");
                        return;
                    }
                    String token = task.getResult();
                    Timber.d("FCM registration token: %s", token);
                    SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("fcmToken", token).apply();
                    updateFcmToken(new FCMToken(token));
                    Timber.d("FCM token updated %s", token);
                });
            }
        }
    }

    /**
     * Replaces the current fragment with the given one.
     */
    public void updateFcmToken(FCMToken fcmToken){
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", "");
        FCMTokenRepository fcmTokenRepository = new FCMTokenRepository();
        Disposable disposable = fcmTokenRepository.updateFcmToken(token, fcmToken)
                .subscribe(
                        response -> Timber.d("FCM token updated"),
                        Timber::e
                );

        compositeDisposable.add(disposable);

        // subscribe to topic
        FirebaseMessaging.getInstance().subscribeToTopic("notify")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Subscription successful
                        Timber.d("Subscribed to topic");
                    } else {
                        // Subscription failed
                        Timber.d("Subscription failed");
                    }
                });
    }

    /**
     * Replaces the current fragment with the specified fragment.
     *
     * @param fragment The fragment to replace the current fragment with.
     */
    private void replaceFragment(Fragment fragment, String tag) {
        Timber.d("Replacing fragment with tag: %s", tag);
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment, tag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * If the current fragment is not the shopping list fragment, the shopping list fragment is displayed.
     * Otherwise, the application is closed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}