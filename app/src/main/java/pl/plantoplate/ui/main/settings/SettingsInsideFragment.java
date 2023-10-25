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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.UserInfo;
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
public class SettingsInsideFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private TextView usernameTextView;
    private Button generateGroupCodeButton;
    private Button exitAccountButton;
    private Button manageUsersButton;
    private Button changeDataButton;
    private Button aboutUsButton;
    private SwitchCompat themeSwitch;
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
        FragmentSettingsInsideBinding settingsView = FragmentSettingsInsideBinding.inflate(inflater, container, false);
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        initViews(settingsView);
        setClickListeners();
        setupTheme();

        setUpViewModel();
        return settingsView.getRoot();
    }

    private void initViews(FragmentSettingsInsideBinding settingsView) {
        Timber.d("Initializing views...");
        generateGroupCodeButton = settingsView.buttonWygenerowanieKodu;
        exitAccountButton = settingsView.buttonWyloguj;
        manageUsersButton = settingsView.buttonZarzadyanieUyztkownikamu;
        changeDataButton = settingsView.buttonZmianaDanych;
        aboutUsButton = settingsView.buttonAboutUs;
        usernameTextView = settingsView.imie;
        themeSwitch = settingsView.switchButtonChangeColorTheme;

        usernameTextView.setVisibility(View.GONE);
    }

    private void setClickListeners() {
        Timber.d("Setting click listeners...");
        exitAccountButton.setOnClickListener(this::exitAccount);
        changeDataButton.setOnClickListener(v -> replaceFragment(new ChangeTheData()));
        aboutUsButton.setOnClickListener(v -> replaceFragment(new MailDevelops()));
    }

    private void setupTheme() {
        SharedPreferences.Editor editor = prefs.edit();
        themeSwitch.setChecked("dark".equals(prefs.getString("theme", "")));

        themeSwitch.setOnClickListener(view -> {
            if (themeSwitch.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putString("theme", "dark");
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putString("theme", "light");
            }
            editor.apply();
        });
    }

    private void setUpViewModel() {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
            updateUserInfo(userInfo);
            checkUsers(userInfo.getRole());
        });
        settingsViewModel.getUserCount().observe(getViewLifecycleOwner(), this::checkUserCount);
        settingsViewModel.getResponseMessage().observe(getViewLifecycleOwner(), successMessage ->
                Toast.makeText(requireActivity(), successMessage, Toast.LENGTH_SHORT).show());
    }

    /**
     * Updates the user information in shared preferences and displays new information on the screen.
     * @param userInfo The user information to be displayed and updated.
     */
    private void updateUserInfo(UserInfo userInfo) {
        String infoText = String.format("Imię: %s\nEmail: %s", userInfo.getUsername(), userInfo.getEmail());
        usernameTextView.setText(infoText);
        usernameTextView.setVisibility(View.VISIBLE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("role", userInfo.getRole());
        editor.putString("username", userInfo.getUsername());
        editor.putString("email", userInfo.getEmail());
        editor.apply();
    }

    /**
     * Checks if the user is an admin and if so, sets the button to clickable and changes its color.
     * If the user is not an admin, the button is not clickable and its color is changed.
     * @param role The role of the user.
     */
    public void checkUsers(String role) {
        if(role.equals("ROLE_ADMIN")) {
            generateGroupCodeButton.setOnClickListener(v -> replaceFragment(new GroupCodeTypeActivity()));
        }else {
            generateGroupCodeButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.back_pop_ups));
            generateGroupCodeButton.setClickable(false);
        }
    }

    /**
     * Checks if the user is an admin and if there is more than one user in the database
     * and if so, sets the button to clickable and changes its color.
     * @param count The number of users in the database.
     */
    private void checkUserCount(int count) {
        String role = prefs.getString("role", "");
        if (role.equals("ROLE_ADMIN") && count > 1) {
            manageUsersButton.setOnClickListener(v -> replaceFragment(new ChangePermissionsFragment()));
        } else {
            manageUsersButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.back_pop_ups));
            manageUsersButton.setClickable(false);
        }
    }

    /**
     * Logs the user out of the app.
     * @param view The view object that was clicked.
     */
    public void exitAccount(View view) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("name");
        editor.remove("email");
        editor.remove("password");
        editor.remove("role");
        editor.remove("token");
        editor.apply();

        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        saveAppState(ApplicationState.LOGIN);
    }

    /**
     * Replaces the current fragment with the specified fragment.
     * @param fragment The fragment to replace the current fragment with.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settings_default, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
}