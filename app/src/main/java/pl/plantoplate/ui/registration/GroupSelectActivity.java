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
package pl.plantoplate.ui.registration;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.auth.UserCredentials;
import pl.plantoplate.data.remote.models.auth.JwtResponse;
import pl.plantoplate.data.remote.repository.GroupRepository;
import pl.plantoplate.databinding.GroupChooseBinding;
import pl.plantoplate.utils.ApplicationState;
import pl.plantoplate.utils.ApplicationStateController;
import pl.plantoplate.ui.main.ActivityMain;
import timber.log.Timber;

/**
 This activity allows the user to select an option between entering an existing group or creating a new one.
 If the user chooses to create a new group, this activity will make an API call to create a new group with the
 user's email and password as credentials.
 */
public class GroupSelectActivity extends AppCompatActivity implements ApplicationStateController {

    private CompositeDisposable compositeDisposable;
    private Button enterGroupButton;
    private Button createGroupButton;
    private SharedPreferences prefs;

    private static HashMap<Integer, Integer> quickStart = new HashMap<>();

    /**
     * This method is called when the activity is created.
     * It inflates the layout and sets the onClickListeners for the buttons.
     *
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupChooseBinding group_select_view = GroupChooseBinding.inflate(getLayoutInflater());
        setContentView(group_select_view.getRoot());

        initViews(group_select_view);
        setClickListeners();
        prefs = getSharedPreferences("prefs", 0);

        quickStart.put(0, R.drawable.quick_start_1);
        quickStart.put(1, R.drawable.quick_start_2);
        quickStart.put(2, R.drawable.quick_start_3);
        quickStart.put(3, R.drawable.quick_start_4);
        quickStart.put(4, R.drawable.quick_start_5);
        quickStart.put(5, R.drawable.quick_start_6);

        compositeDisposable = new CompositeDisposable();
        Timber.d("Activity created");
    }


    /**
     * This method initializes the views.
     *
     * @param group_select_view The view binding object
     */
    private void initViews(GroupChooseBinding group_select_view) {
        Timber.d("Initializing views...");
        enterGroupButton = group_select_view.buttonMamZaproszenie;
        createGroupButton = group_select_view.buttonSwojaGrupa;
    }


    /**
     * This method sets the onClickListeners for the buttons.
     */
    private void setClickListeners() {
        Timber.d("Setting click listeners...");
        enterGroupButton.setOnClickListener(v ->
                startActivity(new Intent(this, GroupEnterActivity.class)));
        createGroupButton.setOnClickListener(this::createGroup);
    }

    /**
     * Retrieves the CreateGroupData object with user credentials.
     *
     * @return The CreateGroupData object containing user credentials.
     */
    public UserCredentials getUserCredentials() {
        Timber.d("Getting user credentials...");
        String email = prefs.getString("email", "");
        String password = prefs.getString("password", "");
        return new UserCredentials(email, password);
    }

    /**
     * This method is called when the user clicks the "Create Group" button.
     * It creates a new group using the user's email and password as credentials.
     *
     * @param view The view that was clicked
     */
    public void createGroup(View view) {
        Timber.d("Creating group...");
        UserCredentials userCredentials = getUserCredentials();
        GroupRepository groupRepository = new GroupRepository();

        Disposable disposable = groupRepository.createGroup(userCredentials)
                .subscribe(
                        jwt -> {
                            saveUserData(jwt);
                            showQuickStartGuadeQuestionSelect();
                        },
                        error -> showSnackbar(view, error.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    /**
     * This method is called when the showQuickStartGuadeQuestion() method is called.
     */
    public void showQuickStartGuadeQuestionSelect() {
        Timber.d("Showing quick start guade question pop up...");
        Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.new_pop_up_question_quick_start);

        TextView closeButton = dialog.findViewById(R.id.button_no);
        TextView acceptButton = dialog.findViewById(R.id.button_yes);

        acceptButton.setOnClickListener(v -> {
            Timber.d("Start showing quick guade pop ups...");
            //TODO: start showing quick guade pop ups
            showQuickStartGuadeSelect(0);
            dialog.dismiss();
        });

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing quick start guade question pop up...");
            startMainActivity();
            saveAppState(ApplicationState.MAIN_ACTIVITY);
            dialog.dismiss();
        });

        dialog.show();
    }


    /**
     * This method is called when the showQuickStartGuadeQuestion() method is called.
     */
    public void showQuickStartGuadeSelect(Integer popUpKeyNumber) {
        Timber.d("Showing quick start guade pop ups...");
        Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.quick_start_1);

        ImageView imageView = dialog.findViewById(R.id.imageView);
        ImageView next = dialog.findViewById(R.id.next);
        ImageView previouse = dialog.findViewById(R.id.previouse);
        ImageView closeButton = dialog.findViewById(R.id.close);

        if (popUpKeyNumber == 0) {
            previouse.setVisibility(View.INVISIBLE);
        } else if (popUpKeyNumber == 5){
            next.setVisibility(View.INVISIBLE);
        } else {
            previouse.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        }

        //imageView set image "quick_"
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), quickStart.get(popUpKeyNumber));
        imageView.setImageDrawable(drawable);

        next.setOnClickListener(v -> {
            Timber.d("Next pop up...");
            //TODO: start showing quick guade pop ups
            showQuickStartGuadeSelect(popUpKeyNumber + 1);
            dialog.dismiss();
        });

        previouse.setOnClickListener(v -> {
            Timber.d("Previouse pop up...");
            //TODO: start showing quick guade pop ups
            showQuickStartGuadeSelect(popUpKeyNumber - 1);
            dialog.dismiss();
        });

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing quick start guade pops up...");
            startMainActivity();
            saveAppState(ApplicationState.MAIN_ACTIVITY);
            dialog.dismiss();
        });

        dialog.show();
    }


    /**
     * This method saves the user's token and role in shared preferences.
     *
     * @param jwt The JWT response
     */
    private void saveUserData(JwtResponse jwt) {
        Timber.d("Saving user data...");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", jwt.getToken());
        editor.putString("role", jwt.getRole());
        editor.apply();
    }


    /**
     * This method starts the main activity.
     */
    private void startMainActivity() {
        Timber.d("Starting main activity...");
        Intent intent = new Intent(this, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    /**
     * This method shows a snackbar with the given message.
     *
     * @param view    The view to show the snackbar on
     * @param message The message to show
     */
    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * This method saves the application state in shared preferences.
     *
     * @param applicationState The application state
     */
    @Override
    public void saveAppState(ApplicationState applicationState) {
        Timber.d("Saving application state: %s", applicationState.toString());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }


    /**
     * This method is called when the activity is destroyed.
     * It disposes the composite disposable.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("Destroying activity...");
        compositeDisposable.dispose();
    }
}