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
package com.example.plantoplate.ui.registration;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plantoplate.databinding.EmailConfirmationBinding;
import com.example.plantoplate.databinding.RegisterActivityBinding;

public class EmailConfirmActivity extends AppCompatActivity {

    private EmailConfirmationBinding email_confirm_view;

    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using the View Binding Library
        email_confirm_view = EmailConfirmationBinding.inflate(getLayoutInflater());
        setContentView(email_confirm_view.getRoot());
    }

}
