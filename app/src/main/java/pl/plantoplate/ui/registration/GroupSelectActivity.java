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

import pl.plantoplate.databinding.GroupChooseBinding;
import pl.plantoplate.repository.remote.models.JwtResponse;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.group.GroupRepository;
import pl.plantoplate.repository.remote.models.CreateGroupData;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import pl.plantoplate.ui.main.ActivityMain;

/**

 This activity allows the user to select an option between entering an existing group or creating a new one.
 If the user chooses to create a new group, this activity will make an API call to create a new group with the
 user's email and password as credentials.
 */
public class GroupSelectActivity extends AppCompatActivity implements ApplicationStateController {
    private GroupChooseBinding group_select_view;

    private Button enter_group;
    private Button create_group;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using the View Binding Library
        group_select_view = GroupChooseBinding.inflate(getLayoutInflater());
        setContentView(group_select_view.getRoot());

        // find views
        enter_group = group_select_view.buttonMamZaproszenie;
        create_group = group_select_view.buttonSwojaGrupa;

        // get shared preferences
        prefs = getSharedPreferences("prefs", 0);

        // set buttons onClickListeners
        enter_group.setOnClickListener(v -> goToGroupEnterActivity());
        create_group.setOnClickListener(this::createGroup);

    }

    /**
     * This method starts the GroupEnterActivity to allow the user to enter an existing group.
     */
    public void goToGroupEnterActivity() {
        Intent intent = new Intent(getApplicationContext(), GroupEnterActivity.class);
        startActivity(intent);
    }

    public CreateGroupData getCreateGroupData() {
        // get user credentials from shared preferences
        String email = prefs.getString("email", "");
        String password = prefs.getString("password", "");

        // create a new CreateGroupData object with user credentials
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

        // make API call to create a new group with user credentials
        GroupRepository groupRepository = new GroupRepository();
        groupRepository.createGroup(createGroupData, new ResponseCallback<JwtResponse>() {
            @Override
            public void onSuccess(JwtResponse jwt) {

                // save user token and role in shared preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("token", jwt.getToken());
                editor.putString("role", jwt.getRole());
                editor.apply();

                // start main activity
                Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    @Override
    public void saveAppState(ApplicationState applicationState) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }
}
