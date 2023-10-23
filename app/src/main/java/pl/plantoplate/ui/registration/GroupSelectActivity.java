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

import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.ResponseCallback;
import pl.plantoplate.data.remote.models.CreateGroupData;
import pl.plantoplate.data.remote.models.JwtResponse;
import pl.plantoplate.data.remote.repository.GroupRepository;
import pl.plantoplate.databinding.GroupChooseBinding;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import pl.plantoplate.ui.main.ActivityMain;

/**

 This activity allows the user to select an option between entering an existing group or creating a new one.
 If the user chooses to create a new group, this activity will make an API call to create a new group with the
 user's email and password as credentials.
 */
public class GroupSelectActivity extends AppCompatActivity implements ApplicationStateController {

    private Button enter_group;
    private Button create_group;
    private SharedPreferences prefs;

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
    }

    private void initViews(GroupChooseBinding group_select_view) {
        enter_group = group_select_view.buttonMamZaproszenie;
        create_group = group_select_view.buttonSwojaGrupa;
    }

    private void setClickListeners() {
        enter_group.setOnClickListener(v -> goToGroupEnterActivity());
        create_group.setOnClickListener(this::createGroup);
    }

    /**
     * This method starts the GroupEnterActivity to allow the user to enter an existing group.
     */
    public void goToGroupEnterActivity() {
        startActivity(new Intent(this, GroupEnterActivity.class));
    }

    /**
     * Retrieves the CreateGroupData object with user credentials.
     *
     * @return The CreateGroupData object containing user credentials.
     */
    public CreateGroupData getCreateGroupData() {
        String email = prefs.getString("email", "");
        String password = prefs.getString("password", "");
        return new CreateGroupData(email, password);
    }

    /**
     * This method is called when the user clicks the "Create Group" button.
     * It creates a new group using the user's email and password as credentials.
     *
     * @param view The view that was clicked
     */
    public void createGroup(View view) {
        CreateGroupData createGroupData = getCreateGroupData();
        GroupRepository groupRepository = new GroupRepository();

        Disposable disposable = groupRepository.createGroup(createGroupData)
                .subscribe(
                        jwt -> {
                            saveUserData(jwt);
                            startMainActivity();
                            saveAppState(ApplicationState.MAIN_ACTIVITY);
                        },
                        error -> showSnackbar(view, error.getMessage())
                );
    }

    private void saveUserData(JwtResponse jwt) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", jwt.getToken());
        editor.putString("role", jwt.getRole());
        editor.apply();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

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
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }
}