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

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import pl.plantoplate.databinding.GroupPageBinding;

public class GroupEnterActivity extends AppCompatActivity {

    private GroupPageBinding group_enter_view;

    private TextInputEditText group_code_enter;
    private Button group_code_enter_button;

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
        group_code_enter_button.setOnClickListener(v -> checkGroupCode());



    }

    public void checkGroupCode() {

    }
}
