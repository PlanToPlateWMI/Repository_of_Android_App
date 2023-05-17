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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import okhttp3.ResponseBody;
import pl.plantoplate.databinding.GroupPageBinding;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.joinGroup.UserJoinGroupCallback;
import pl.plantoplate.requests.joinGroup.UserJoinGroupData;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import retrofit2.Call;

/**
 * An activity for user entering group code to join group.
 */
public class GroupEnterActivity extends AppCompatActivity implements ApplicationStateController {

    private GroupPageBinding group_enter_view;

    private TextInputEditText group_code_enter;
    private Button group_code_enter_button;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using the View Binding Library
        group_enter_view = GroupPageBinding.inflate(getLayoutInflater());
        setContentView(group_enter_view.getRoot());

        // Get the group code entered by the user
        group_code_enter = group_enter_view.wprowadzKod;
        group_code_enter_button = group_enter_view.buttonZatwierdz;

        // Get shared preferences
        prefs = getSharedPreferences("prefs", 0);

        // Set listener for the group code entered by the user
        group_code_enter_button.setOnClickListener(this::checkGroupCode);



    }

    /**
     * This method is called when the user clicks the confirm button.
     * It checks if the group code entered by the user is valid and if so, it makes an API call to join the group.
     * @param v The view that was clicked
     */
    public void checkGroupCode(View v) {
        String code = Objects.requireNonNull(group_code_enter.getText()).toString();
        if (code.isEmpty()) {
            Snackbar.make(v, "Wprowadź kod grupy!", Snackbar.LENGTH_LONG).show();
            return;
        }
        String email = prefs.getString("email", "");
        String password = prefs.getString("password", "");
        UserJoinGroupData data = new UserJoinGroupData(code, email, password);
        Call<ResponseBody> myCall = RetrofitClient.getInstance().getApi().joinGroupByCode(data);

        myCall.enqueue(new UserJoinGroupCallback(v, this));
    }

    @Override
    public void saveAppState(ApplicationState applicationState) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }
}
