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

package pl.plantoplate.ui.main.settings.groupCodeGeneration;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import pl.plantoplate.databinding.ChoiceAdultOrChildBinding;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.groupCodeGeneration.GenerateGroupCodeCallback;
import retrofit2.Call;

/**
 * An activity that allows the user to choose the type of group code to generate.
 */
public class GroupCodeTypeActivity extends AppCompatActivity {

    private ChoiceAdultOrChildBinding choose_group_code_type_view;

    private Button child_code_button;
    private Button adult_code_button;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using the View Binding Library
        choose_group_code_type_view = ChoiceAdultOrChildBinding.inflate(getLayoutInflater());

        // Set the content view to the inflated layout
        setContentView(choose_group_code_type_view.getRoot());

        // Get the buttons for choosing group code type
        child_code_button = choose_group_code_type_view.codeForChild;
        adult_code_button = choose_group_code_type_view.codeForAdult;

        // Set the onClickListeners for the buttons
        child_code_button.setOnClickListener(this::generateChildCode);
        adult_code_button.setOnClickListener(this::generateAdultCode);

        // Get shared preferences
        prefs = getSharedPreferences("prefs", 0);
    }

    /**
     * Generates a group code for a child (USER permission).
     *
     * @param view The view that was clicked.
     */
    public void generateChildCode(View view) {
        String token = "Bearer " + prefs.getString("token", "");
        String role = "USER";

        Call<ResponseBody> myCall = RetrofitClient.getInstance().getApi().generateGroupCode(token, role);
        myCall.enqueue(new GenerateGroupCodeCallback(view));
    }

    /**
     * Generates a group code for an adult (ADMIN permission).
     *
     * @param view The view that was clicked.
     */
    public void generateAdultCode(View view) {
        String token = "Bearer " + prefs.getString("token", "");
        String role = "ADMIN";

        Call<ResponseBody> myCall = RetrofitClient.getInstance().getApi().generateGroupCode(token, role);
        myCall.enqueue(new GenerateGroupCodeCallback(view));
    }
}
