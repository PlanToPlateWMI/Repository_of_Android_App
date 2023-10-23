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

package pl.plantoplate.ui.main.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentSettingsInsideBinding;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.ui.main.settings.changePermissions.ChangePermissionsFragment;
import pl.plantoplate.ui.main.settings.developerContact.MailDevelops;
import pl.plantoplate.ui.main.settings.accountManagement.ChangeTheData;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.GroupCodeTypeActivity;
import pl.plantoplate.ui.main.settings.viewModels.SettingsViewModel;
import timber.log.Timber;

/**
 * The fragment that is displayed when the user clicks the settings button.
 */
public class SettingsInsideFragment extends Fragment{

    private FragmentSettingsInsideBinding settings_view;
    private SettingsViewModel settingsViewModel;

    private TextView username;
    private Button generate_group_code_button;
    private Button exit_account_button;
    private Button button_zarzadzanie_uyztkownikamu;
    private Button button_zmiana_danych;
    private Button button_about_us;
    private Switch switchButton;

    private SharedPreferences prefs;

    @Override
    public void onResume() {
        super.onResume();
        settingsViewModel.fetchUserCount();
        settingsViewModel.fetchUserInfo();
        checkUsers(prefs.getString("role", ""));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Timber.d("onCreate() called");

        // Inflate the layout using the View Binding Library
        settings_view = FragmentSettingsInsideBinding.inflate(inflater, container, false);

        // Get views
        generate_group_code_button = settings_view.buttonWygenerowanieKodu;
        exit_account_button = settings_view.buttonWyloguj;
        button_zarzadzanie_uyztkownikamu = settings_view.buttonZarzadyanieUyztkownikamu;
        button_zmiana_danych = settings_view.buttonZmianaDanych;
        button_about_us = settings_view.buttonAboutUs;
        username = settings_view.imie;
        switchButton=settings_view.switchButtonChangeColorTheme;

        username.setVisibility(View.GONE);

        // Get the shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        exit_account_button.setOnClickListener(this::exitAccount);
        button_zmiana_danych.setOnClickListener(v -> replaceFragment(new ChangeTheData()));
        button_about_us.setOnClickListener(v -> replaceFragment(new MailDevelops()));

        setUpViewModel();
        setupTheme();

        return settings_view.getRoot();
    }

    public void setupTheme() {
        SharedPreferences.Editor editor = prefs.edit();
        // depends on SharedPreferences key set checked of Switch button
        switch (prefs.getString("theme", "")) {
            case "dark":
                switchButton.setChecked(true);
                break;

            case "light":
                switchButton.setChecked(false);
                break;
        }


        // switch listener - change theme and change key of SharedPreferences
        switchButton.setOnClickListener(view -> {
            if (switchButton.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // set dark mode
                editor.putString("theme", "dark");
                editor.apply();

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // set light mode
                editor.putString("theme", "light");
                editor.apply();

            }
        });
    }

    public void checkUsers(String role) {
        if(role.equals("ROLE_ADMIN")) {
            generate_group_code_button.setOnClickListener(v -> replaceFragment(new GroupCodeTypeActivity()));
        }else {
            generate_group_code_button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.back_pop_ups));
            generate_group_code_button.setClickable(false);
        }
    }

    public void setUpViewModel() {

        // get storage view model
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        // get user info
        settingsViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
            Spannable spans = new SpannableString("Imię: " + userInfo.getUsername() + "\n" + "Email: " + userInfo.getEmail());
            username.setText(spans);
            username.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("role", userInfo.getRole());
            editor.putString("username", userInfo.getUsername());
            editor.putString("email", userInfo.getEmail());
            editor.apply();

            checkUsers(userInfo.getRole());
        });

        // get user count
        settingsViewModel.getUserCount().observe(getViewLifecycleOwner(), count -> {
            String role = prefs.getString("role", "");
            if(role.equals("ROLE_ADMIN") && count > 1) {
                button_zarzadzanie_uyztkownikamu.setOnClickListener(v -> replaceFragment(new ChangePermissionsFragment()));
            }else {
                button_zarzadzanie_uyztkownikamu.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.back_pop_ups));
                button_zarzadzanie_uyztkownikamu.setClickable(false);
            }
        });

        // get response message
        settingsViewModel.getResponseMessage().observe(getViewLifecycleOwner(), successMessage ->
                Toast.makeText(requireActivity(), successMessage, Toast.LENGTH_SHORT).show());
    }

    /**
     * Logs the user out of the app.
     * @param view The view object that was clicked.
     */
    public void exitAccount(View view) {

        //delete the user's data from the shared preferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("name");
        editor.remove("email");
        editor.remove("password");
        editor.remove("role");
        editor.remove("token");
        editor.apply();

        //go back to the login screen
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        // save the app state
        saveAppState(ApplicationState.LOGIN);
    }

    /**
     * Saves the app state to the shared preferences.
     * @param applicationState The app state to save.
     */
    public void saveAppState(ApplicationState applicationState) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }

    /**
     * Replaces the current fragment with the specified fragment.
     * @param fragment The fragment to replace the current fragment with.
     */
    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settings_default, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
