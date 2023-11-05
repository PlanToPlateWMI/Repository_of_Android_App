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
package pl.plantoplate.ui.main.settings.accountManagement.changePassword;

import static android.content.Context.MODE_PRIVATE;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputLayout;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.repository.UserRepository;
import pl.plantoplate.databinding.FragmentPasswordChange2Binding;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.SCryptStretcher;
import pl.plantoplate.ui.login.LoginActivity;

/**
 * This fragment is used to change the password.
 */
public class PasswordChangeNewPasswords extends Fragment {

    private CompositeDisposable compositeDisposable;
    private UserRepository userRepository;
    private Button acceptButton;
    private TextInputLayout enterNewPasswordInputLayout;
    private TextInputLayout repeatNewPasswordInputLayout;
    private EditText newPasswordEditText;
    private EditText repeatNewPasswordEditText;
    private SharedPreferences prefs;


    /**
     * Create the view
     * @param inflater The layout inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPasswordChange2Binding fragmentPasswordChange2Binding = FragmentPasswordChange2Binding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();
        userRepository = new UserRepository();
        prefs = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE);

        initViews(fragmentPasswordChange2Binding);
        setClickListeners();
        return fragmentPasswordChange2Binding.getRoot();
    }

    public void initViews(FragmentPasswordChange2Binding fragmentPasswordChange2Binding){
        acceptButton = fragmentPasswordChange2Binding.buttonZatwierdz;
        enterNewPasswordInputLayout = fragmentPasswordChange2Binding.wprowadzNoweHaslo;
        repeatNewPasswordInputLayout = fragmentPasswordChange2Binding.wprowadzNoweHasloPonownie;
        newPasswordEditText = enterNewPasswordInputLayout.getEditText();
        repeatNewPasswordEditText = repeatNewPasswordInputLayout.getEditText();
    }

    private void setClickListeners() {
        acceptButton.setOnClickListener(v -> validatePasswords());
    }

    public void validatePasswords(){
        String password = newPasswordEditText.getText().toString().trim();
        String password2 = repeatNewPasswordEditText.getText().toString().trim();

        if(password.isEmpty()) {
            enterNewPasswordInputLayout.setError("Wprowadź hasło");
            enterNewPasswordInputLayout.requestFocus();
        } else if(!password.equals(password2)) {
            repeatNewPasswordInputLayout.setError("Hasła nie są takie same");
            repeatNewPasswordInputLayout.requestFocus();
        } else if(password.length() < 7) {
            System.out.println("Hasło musi mieć co najmniej 7 znaków");
            enterNewPasswordInputLayout.setError("Hasło musi mieć co najmniej 7 znaków");
            enterNewPasswordInputLayout.requestFocus();
        } else {
            String email = prefs.getString("email", "");
            password = SCryptStretcher.stretch(password, email);
            changePassword(password);
        }
    }

    private void changePassword(String password) {
        String token = "Bearer " + prefs.getString("token", "");
        Disposable disposable = userRepository.changePassword(token, password)
                .subscribe(userInfo -> {
                    Toast.makeText(requireActivity(), "Hasło zostało zmienione", Toast.LENGTH_SHORT).show();
                    exitAccount();
                }, throwable ->
                        Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());

        compositeDisposable.add(disposable);
    }

    /**
     * Logs the user out of the app.
     */
    public void exitAccount() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("name");
        editor.remove("email");
        editor.remove("password");
        editor.remove("role");
        editor.remove("token");
        editor.apply();

        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}