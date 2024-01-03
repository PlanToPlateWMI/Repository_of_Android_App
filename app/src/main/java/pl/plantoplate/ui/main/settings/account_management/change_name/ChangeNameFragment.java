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
package pl.plantoplate.ui.main.settings.account_management.change_name;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputLayout;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.repository.UserRepository;
import pl.plantoplate.databinding.FragmentNameChangeBinding;

/**
 * This fragment is responsible for changing the name of the user.
 */
public class ChangeNameFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private Button acceptButton;
    private TextInputLayout enterNameInputLayout;
    private EditText enterNameEditText;
    private SharedPreferences prefs;
    private UserRepository userRepository;

    /**
     * Create the view for this fragment, get the buttons for choosing group code type and set the
     * @param inflater The layout inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The view for this fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNameChangeBinding fragmentNameChangeBinding = FragmentNameChangeBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();
        userRepository = new UserRepository();
        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        initViews(fragmentNameChangeBinding);
        setClickListeners();
        return fragmentNameChangeBinding.getRoot();
    }

    /**
     * This method is responsible for initializing the views.
     * @param fragmentNameChangeBinding The binding for this fragment
     */
    public void initViews(FragmentNameChangeBinding fragmentNameChangeBinding){
        acceptButton = fragmentNameChangeBinding.buttonZatwierdz;
        enterNameInputLayout = fragmentNameChangeBinding.wprowadzImie;
        enterNameEditText = fragmentNameChangeBinding.wprowadzImie.getEditText();
    }

    private void setClickListeners() {
        acceptButton.setOnClickListener(this::onAcceptName);
    }

    /**
     * This method is called when the user clicks the button to accept the name.
     * @param view The view that was clicked
     */
    public void onAcceptName(View view) {
        String username = String.valueOf(enterNameEditText.getText()).trim();
        if (username.isEmpty()) {
            Toast.makeText(requireActivity(), "Pole nie może być puste", Toast.LENGTH_SHORT).show();
            enterNameInputLayout.setError("Pole nie może być puste");
            return;
        }

        String token = "Bearer " + prefs.getString("token", "");
        Disposable disposable = userRepository.changeUsername(token, username)
                .subscribe(userInfo -> {
                        prefs.edit().putString("username", username).apply();
                        Toast.makeText(requireActivity(), "Imię zostało zmienione na: " + username, Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                },
                        throwable -> Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());

        compositeDisposable.add(disposable);
    }

    /**
     * This method is called when the fragment is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}