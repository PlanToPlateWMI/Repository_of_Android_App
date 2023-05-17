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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import pl.plantoplate.databinding.GeneratedCodeBinding;
import pl.plantoplate.ui.main.ActivityMain;

/**
 * An activity that displays the generated group code.
 */
public class GeneratedGroupCodeActivity extends AppCompatActivity {

    private GeneratedCodeBinding generated_code_view;

    private TextInputEditText group_code;
    private Button apply_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using the View Binding Library
        generated_code_view = GeneratedCodeBinding.inflate(getLayoutInflater());

        // Set the content view to the inflated layout
        setContentView(generated_code_view.getRoot());

        // Get the TextView for the group code
        group_code = generated_code_view.kod;

        // Get the button for accepting the code
        apply_button = generated_code_view.applyButton;

        // Set the group code view
        setGroupCodeView();

        // Set the onClickListener for the button
        apply_button.setOnClickListener(this::applyCode);
    }

    /**
     * Sets the group code view, so that the user can see the generated group code.
     */
    public void setGroupCodeView() {
        // get group code from extras
        String groupCode = getIntent().getStringExtra("group_code");

        // set the group code text view
        group_code.setText(groupCode);
    }

    /**
     * Starts the MainActivity.
     * @param view The view object that was clicked.
     */
    private void applyCode(View view) {
        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }

}
