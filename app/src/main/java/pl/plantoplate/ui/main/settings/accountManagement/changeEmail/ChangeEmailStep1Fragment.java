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
import pl.plantoplate.data.remote.repository.UserRepository;
import pl.plantoplate.databinding.FragmentEmailChangeBinding;
import pl.plantoplate.utils.SCryptStretcher;

/**
 * This class is responsible for changing the user's email address.
 */
public class ChangeEmailStep1Fragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private UserRepository userRepository;
    private Button acceptButton;
    private TextInputLayout enterPasswordInputLayout;
    private EditText passwordEditText;

    private SharedPreferences sharedPreferences;

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
        FragmentEmailChangeBinding fragmentEmailChangeBinding = FragmentEmailChangeBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();
        userRepository = new UserRepository();
        sharedPreferences = requireActivity().getSharedPreferences("prefs", 0);

        initViews(fragmentEmailChangeBinding);
        setClickListeners();
        return fragmentEmailChangeBinding.getRoot();
    }

    public void initViews(FragmentEmailChangeBinding fragmentEmailChangeBinding){
        acceptButton = fragmentEmailChangeBinding.buttonZatwierdz;
        enterPasswordInputLayout = fragmentEmailChangeBinding.wprowadzHaslo;
        passwordEditText = enterPasswordInputLayout.getEditText();
    }

    public void setClickListeners(){
        acceptButton.setOnClickListener(v -> validatePasswordMatch());
    }

    public void validatePasswordMatch() {
        String password = passwordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            enterPasswordInputLayout.setError("Pole nie może być puste");
            return;
        }

        String email = sharedPreferences.getString("email", "");
        String passwordStretched = SCryptStretcher.stretch(password, email);
        String token = "Bearer " + sharedPreferences.getString("token", "");

        Disposable disposable = userRepository.validatePasswordMatch(token, passwordStretched)
                .subscribe(response -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("password", password);

                    Fragment fragment = new ChangeEmailStep2Fragment();
                    fragment.setArguments(bundle);

                    replaceFragment(fragment);
                }, throwable -> Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());

        compositeDisposable.add(disposable);
    }

    /**
     * Replaces the current fragment with the specified fragment.
     *
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