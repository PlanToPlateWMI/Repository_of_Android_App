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
package pl.plantoplate.ui.login.remindPassword;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pl.plantoplate.databinding.RemindPassword2Binding;

public class EnterCodeActivity extends AppCompatActivity {

    private RemindPassword2Binding change_password_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        change_password_view = RemindPassword2Binding.inflate(getLayoutInflater());
        setContentView(change_password_view.getRoot());


    }
}
