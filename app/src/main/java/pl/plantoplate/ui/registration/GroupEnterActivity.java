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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import pl.plantoplate.databinding.GroupPageBinding;
import pl.plantoplate.repository.remote.models.JwtResponse;
import pl.plantoplate.repository.remote.models.UserJoinGroupData;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.group.GroupRepository;
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

        // Inflate the layout using the View Binding Library
        group_enter_view = GroupPageBinding.inflate(getLayoutInflater());
        setContentView(group_enter_view.getRoot());

        // Get the group code entered by the user
        group_code_enter = group_enter_view.wprowadzKod;
        group_code_enter_button = group_enter_view.buttonZatwierdz;

        // Set listener for the group code entered by the user
        group_code_enter_button.setOnClickListener(this::validateGroupCode);

        // Get shared preferences
        prefs = getSharedPreferences("prefs", 0);
    }

    /**
     * This method is called when the activity is resumed.
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
        String code = Objects.requireNonNull(group_code_enter.getText()).toString();
        if (code.isEmpty()) {
            Snackbar.make(v, "Wprowadź kod grupy!", Snackbar.LENGTH_LONG).show();
            return;
        }
        getJoinGroupData(v, code);
    }

    /**
     * This method is called when the user clicks the confirm button.
     * It makes an API call to join the group.
     * @param view The view that was clicked
     * @param userJoinGroupdata The data needed to join the group
     */
    public void joinGroup(View view, UserJoinGroupData userJoinGroupdata) {
        GroupRepository groupRepository = new GroupRepository();
        groupRepository.joinGroupByCode(userJoinGroupdata, new ResponseCallback<JwtResponse>() {
            @Override
            public void onSuccess(JwtResponse jwt) {
                // Save token and role in shared preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("token", jwt.getToken());
                editor.putString("role", jwt.getRole());

                // delete email from shared preferences
                editor.remove("email").apply();

                // start main activity
                Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // save app state
                saveAppState(ApplicationState.MAIN_ACTIVITY);
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(view, failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });
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
