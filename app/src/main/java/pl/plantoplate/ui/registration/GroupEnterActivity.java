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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Optional;

import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.ResponseCallback;
import pl.plantoplate.data.remote.models.JwtResponse;
import pl.plantoplate.data.remote.models.UserJoinGroupData;
import pl.plantoplate.data.remote.repository.GroupRepository;
import pl.plantoplate.databinding.GroupPageBinding;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import pl.plantoplate.ui.main.ActivityMain;

/**
 * An activity for user entering group code to join group.
 */
public class GroupEnterActivity extends AppCompatActivity implements ApplicationStateController {

    private GroupPageBinding group_enter_view;
    private TextInputEditText group_code_enter;
    private Button group_code_enter_button;
    private SharedPreferences prefs;

    /**
     * This method is called when the activity is created.
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group_enter_view = GroupPageBinding.inflate(getLayoutInflater());
        setContentView(group_enter_view.getRoot());

        initViews();
        setClickListeners();
        prefs = getSharedPreferences("prefs", 0);
    }

    private void initViews() {
        group_code_enter = group_enter_view.wprowadzKod;
        group_code_enter_button = group_enter_view.buttonZatwierdz;
    }

    private void setClickListeners() {
        group_code_enter_button.setOnClickListener(this::validateGroupCode);
    }

    /**
     * This method is called when the activity is resumed.
     * @param v The view that was clicked
     * @param code The group code entered by the user
     */
    public void getJoinGroupData(View v, String code) {
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
        Optional<CharSequence> code = Optional.ofNullable(group_code_enter.getText());

        if (code.map(CharSequence::toString).orElse("").isEmpty()) {
            Snackbar.make(v, "Wprowadź kod grupy!", Snackbar.LENGTH_LONG).show();
        } else {
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
        GroupRepository groupRepository = new GroupRepository();

        Disposable disposable = groupRepository.joinGroupByCode(userJoinGroupData)
                .subscribe(
                        jwt -> {
                            saveUserData(jwt);
                            if (jwt.getRole().equals("ROLE_USER")) {
                                showRoleChildAboutInfoPopUp();
                            } else {
                                startMainActivity();
                            }
                        },
                        error -> showSnackbar(view, error.getMessage())
                );
    }

    public void showRoleChildAboutInfoPopUp() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_pop_up_dziecko);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        acceptButton.setOnClickListener(v -> {
            startMainActivity();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void saveUserData(JwtResponse jwt) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", jwt.getToken());
        editor.putString("role", jwt.getRole());
        editor.apply();
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        saveAppState(ApplicationState.MAIN_ACTIVITY);
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * This method is called when the user clicks the back button.
     * It starts the previous activity.
     */
    @Override
    public void saveAppState(ApplicationState applicationState) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }
}