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
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Optional;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.auth.JwtResponse;
import pl.plantoplate.data.remote.models.user.UserJoinGroupData;
import pl.plantoplate.data.remote.repository.GroupRepository;
import pl.plantoplate.databinding.GroupPageBinding;
import pl.plantoplate.utils.ApplicationState;
import pl.plantoplate.utils.ApplicationStateController;
import pl.plantoplate.ui.main.ActivityMain;
import timber.log.Timber;

/**
 * An activity for user entering group code to join group.
 */
public class GroupEnterActivity extends AppCompatActivity implements ApplicationStateController {

    private CompositeDisposable compositeDisposable;
    private TextInputEditText groupCodeEnterEditText;
    private Button groupCodeEnterButton;
    private SharedPreferences prefs;
    private static HashMap<Integer, Integer> quickStart = new HashMap<>();

    /**
     * This method is called when the activity is created.
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupPageBinding groupPageBinding = GroupPageBinding.inflate(getLayoutInflater());
        setContentView(groupPageBinding.getRoot());

        initViews(groupPageBinding);
        setClickListeners();
        prefs = getSharedPreferences("prefs", 0);

        quickStart.put(0, R.layout.quick_start_1);
        quickStart.put(1, R.layout.quick_start_2);
        quickStart.put(2, R.layout.quick_start_3);
        quickStart.put(3, R.layout.quick_start_4);
        quickStart.put(4, R.layout.quick_start_5);
        quickStart.put(5, R.layout.quick_start_6);

        compositeDisposable = new CompositeDisposable();
        Timber.d("Activity created");
    }

    /**
     * This method is called when the activity is resumed.
     */
    private void initViews(GroupPageBinding groupPageBinding) {
        Timber.d("Initializing views...");
        groupCodeEnterEditText = groupPageBinding.wprowadzKod;
        groupCodeEnterButton = groupPageBinding.buttonZatwierdz;
    }

    /**
     * This method is called when the activity is resumed.
     */
    private void setClickListeners() {
        Timber.d("Setting click listeners...");
        groupCodeEnterButton.setOnClickListener(this::validateGroupCode);
    }

    /**
     * This method is called when the activity is resumed.
     * @param v The view that was clicked
     * @param code The group code entered by the user
     */
    public void getJoinGroupData(View v, String code) {
        Timber.d("Getting join group data...");
        String email = prefs.getString("email", "");
        String password = prefs.getString("password", "");
        UserJoinGroupData data = new UserJoinGroupData(code, email, password);
        joinGroup(v, data);
    }

    /**
     * This method is called when the user clicks the confirm button.
     * It checks if the group code entered by the user is valid and if so, it makes an API call to join the group.
     * @param v The view that was clicked
     */
    public void validateGroupCode(View v) {
        Timber.d("Validating group code...");
        Optional<CharSequence> code = Optional.ofNullable(groupCodeEnterEditText.getText());

        if (code.map(CharSequence::toString).orElse("").isEmpty()) {
            Timber.d("Group code is empty");
            showSnackbar(v, "Wprowadź kod grupy!");
        } else {
            Timber.d("Group code is correct");
            getJoinGroupData(v, code.get().toString());
        }
    }

    /**
     * This method is called when the user clicks the confirm button.
     * It makes an API call to join the group.
     * @param view The view that was clicked
     * @param userJoinGroupData The data needed to join the group
     */
    public void joinGroup(View view, UserJoinGroupData userJoinGroupData) {
        Timber.d("Joining group...");
        GroupRepository groupRepository = new GroupRepository();

        Disposable disposable = groupRepository.joinGroupByCode(userJoinGroupData)
                .subscribe(
                        jwt -> {
                            saveUserData(jwt);
                            //before starting main activity, we need to check if user is child or parent
                            if (jwt.getRole().equals("ROLE_USER")) {
                                showRoleChildAboutInfoPopUp();
                            } else {
                                //quick start option
                                showQuickStartGuadeQuestion();
                                //startMainActivity();
                            }
                        },
                        error -> showSnackbar(view, error.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    /**
     * This method is called when the activity is resumed.
     */
    private void saveUserData(JwtResponse jwt) {
        Timber.d("Saving user data...");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", jwt.getToken());
        editor.putString("role", jwt.getRole());
        editor.apply();
    }

    /**
     * This method is called when role child about info pop up is shown.
     */
    public void showRoleChildAboutInfoPopUp() {
        Timber.d("Showing role child about info pop up...");
        Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.new_pop_up_dziecko);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);

        acceptButton.setOnClickListener(v -> {
            Timber.d("Accepting role child about info pop up...");
            showQuickStartGuadeQuestion();
            //startMainActivity();
            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * This method is called when the showQuickStartGuadeQuestion() method is called.
     */
    public void showQuickStartGuadeQuestion() {
        Timber.d("Showing quick start guade question pop up...");
        Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.new_pop_up_question_quick_start);

        TextView closeButton = dialog.findViewById(R.id.button_no);
        TextView acceptButton = dialog.findViewById(R.id.button_yes);

        acceptButton.setOnClickListener(v -> {
            Timber.d("Start showing quick guade pop ups...");
            //TODO: start showing quick guade pop ups
            showQuickStartGuade(0);
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
    public void showQuickStartGuade(Integer popUpKeyNumber) {
        Timber.d("Showing quick start guade pop ups...");
        Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(quickStart.get(popUpKeyNumber));

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
//        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), quickStart.get(popUpKeyNumber));
//        imageView.setImageDrawable(drawable);

        next.setOnClickListener(v -> {
            Timber.d("Next pop up...");
            //TODO: start showing quick guade pop ups
            showQuickStartGuade(popUpKeyNumber + 1);
            dialog.dismiss();
        });

        previouse.setOnClickListener(v -> {
            Timber.d("Previouse pop up...");
            //TODO: start showing quick guade pop ups
            showQuickStartGuade(popUpKeyNumber - 1);
            dialog.dismiss();
        });

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing quick start guade pops up...");
            startMainActivity();
            dialog.dismiss();
        });

        dialog.show();
    }


    /**
     * This method is called when the activity is resumed.
     */
    public void startMainActivity() {
        Timber.d("Starting main activity...");
        Intent intent = new Intent(this, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        saveAppState(ApplicationState.MAIN_ACTIVITY);
    }

    /**
     * This method is called when the activity is resumed.
     * @param view The view that was clicked
     * @param message The message to be shown
     */
    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * This method is called when the user clicks the back button.
     * It starts the previous activity.
     * @param applicationState The state of the application.
     */
    @Override
    public void saveAppState(ApplicationState applicationState) {
        Timber.d("Saving application state: %s", applicationState.toString());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }

    /**
     * This method is called when the user clicks the back button.
     * It starts the previous activity.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("Destroying activity...");
        compositeDisposable.dispose();
    }
}