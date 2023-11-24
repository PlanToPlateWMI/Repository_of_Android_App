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
package pl.plantoplate.ui.main.settings.accountManagement.changeEmail;

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
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.textfield.TextInputLayout;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.auth.JwtResponse;
import pl.plantoplate.data.remote.models.user.UserInfo;
import pl.plantoplate.data.remote.repository.UserRepository;
import pl.plantoplate.databinding.FragmentEmailChange2Binding;
import pl.plantoplate.utils.EmailValidator;
import pl.plantoplate.utils.SCryptStretcher;
import pl.plantoplate.ui.main.settings.accountManagement.ChangeTheData;
import timber.log.Timber;


/**
 * This class is responsible for changing the email address of the user.
 * It is used in the settings menu.
 */
public class ChangeEmailStep2Fragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private UserRepository userRepository;
    private Button acceptButton;
    private TextInputLayout enterNewEmailInputLayout;
    private TextInputLayout repeatNewEmailInputLayout;
    private EditText enterNewEmailEditText;
    private EditText repeatNewEmailEditText;
    private SharedPreferences prefs;

    /**
     * Create the view
     * @param inflater The layout inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The view of the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEmailChange2Binding fragmentEmailChange2Binding = FragmentEmailChange2Binding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();
        userRepository = new UserRepository();
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        initViews(fragmentEmailChange2Binding);
        setClickListeners();
        return fragmentEmailChange2Binding.getRoot();
    }

    public void initViews(FragmentEmailChange2Binding fragmentEmailChange2Binding){
        acceptButton = fragmentEmailChange2Binding.buttonZatwierdz;
        enterNewEmailInputLayout = fragmentEmailChange2Binding.wprowadzNowyEmail;
        repeatNewEmailInputLayout = fragmentEmailChange2Binding.wprowadzNowyEmailPonownie;
        enterNewEmailEditText = enterNewEmailInputLayout.getEditText();
        repeatNewEmailEditText = repeatNewEmailInputLayout.getEditText();
    }

    public void setClickListeners(){
        acceptButton.setOnClickListener(v -> validateEmail());
    }

    public void validateEmail(){
        String newEmail = enterNewEmailEditText.getText().toString().trim();
        String newEmailAgain = repeatNewEmailEditText.getText().toString().trim();

        if(newEmail.isEmpty()){
            Toast.makeText(requireActivity(), "Pole nie może być puste", Toast.LENGTH_SHORT).show();
            enterNewEmailInputLayout.setError("Pole nie może być puste");
            enterNewEmailInputLayout.requestFocus();
        } else if(!newEmail.equals(newEmailAgain)){
            Toast.makeText(requireActivity(), "Adresy email nie są takie same", Toast.LENGTH_SHORT).show();
            repeatNewEmailInputLayout.setError("Adresy email nie są takie same");
            repeatNewEmailInputLayout.requestFocus();
        } else if(!EmailValidator.isEmail(newEmail)){
            Toast.makeText(requireActivity(), "Wprowadź poprawny adres email", Toast.LENGTH_SHORT).show();
            enterNewEmailInputLayout.setError("Wprowadź poprawny adres email");
            enterNewEmailInputLayout.requestFocus();
        } else if(!EmailValidator.isEmail(newEmailAgain)){
            Toast.makeText(requireActivity(), "Wprowadź poprawny adres email", Toast.LENGTH_SHORT).show();
            repeatNewEmailInputLayout.setError("Wprowadź poprawny adres email");
            repeatNewEmailInputLayout.requestFocus();
        } else {
            setNewPassword(newEmail);
        }
    }

    public void setNewPassword(String newEmail) {
        String token = "Bearer " + prefs.getString("token", "");
        String newPassword = SCryptStretcher.stretch(requireArguments().getString("password"), newEmail);

        Disposable disposable = userRepository.changePassword(token, newPassword)
                .subscribe(userInfo -> {
                    UserInfo updatedUserInfo = new UserInfo();
                    updatedUserInfo.setEmail(newEmail);
                    updatedUserInfo.setPassword(newPassword);

                    setNewEmail(token, updatedUserInfo);
                }, throwable ->
                        Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());

        compositeDisposable.add(disposable);
    }

    public void setNewEmail(String token, UserInfo userInfo){

        Disposable disposable = userRepository.changeEmail(token, userInfo)
                .subscribe(jwt -> {
                    Toast.makeText(requireContext(), "Email został zmieniony", Toast.LENGTH_LONG).show();
                    saveNewUserData(jwt, userInfo);
                    replaceFragment(new ChangeTheData());
                }, throwable ->
                        Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_LONG).show());

        compositeDisposable.add(disposable);
    }

    public void saveNewUserData(JwtResponse jwt, UserInfo userInfo){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", userInfo.getEmail());
        editor.putString("role", jwt.getRole());
        editor.putString("token", jwt.getToken());
        editor.apply();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}