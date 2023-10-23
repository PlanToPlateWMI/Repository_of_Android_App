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

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentChoiceAdultOrChildBinding;
import pl.plantoplate.data.remote.ResponseCallback;
import pl.plantoplate.data.remote.repository.GroupRepository;

/**
 * An activity that allows the user to choose the type of group code to generate.
 */
public class GroupCodeTypeActivity extends Fragment {

    private FragmentChoiceAdultOrChildBinding choose_group_code_type_view;

    private Button child_code_button;
    private Button adult_code_button;
    private ImageView question_button;

    private SharedPreferences prefs;

    /**
     * Create the view for this fragment, get the buttons for choosing group code type and set the
     * @param inflater The layout inflater
     * @param container The container for the fragment
     * @param savedInstanceState The saved instance state
     * @return The view for this fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        choose_group_code_type_view = FragmentChoiceAdultOrChildBinding.inflate(inflater, container, false);


        // Get the buttons for choosing group code type
        child_code_button = choose_group_code_type_view.codeForChild;
        adult_code_button = choose_group_code_type_view.codeForAdult;
        question_button = choose_group_code_type_view.questionButton;

        // Get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // Set the onClickListeners for the buttons
        child_code_button.setOnClickListener(this::generateChildCode);
        adult_code_button.setOnClickListener(this::generateAdultCode);
        question_button.setOnClickListener(v -> showQuestionPopUp());

        return choose_group_code_type_view.getRoot();
    }

    /**
     * Set the User (Child) role of the user and call the generateGroupCode method.
     *
     * @param view The view that was clicked.
     */
    public void generateChildCode(View view) {
        String role = "USER";
        generateGroupCode(view, role);
    }

    /**
     * Set the ADMIN (Parent) role of the user and call the generateGroupCode method.
     *
     * @param view The view that was clicked.
     */
    public void generateAdultCode(View view) {
        String role = "ADMIN";
        generateGroupCode(view, role);
    }

    /**
     * Generate a group code for the user.
     *
     * @param view The view that was clicked.
     * @param role The role of the user.
     */
    public void generateGroupCode(View view, String role){
        String token = "Bearer " + prefs.getString("token", "");

        GroupRepository groupRepository = new GroupRepository();

        Disposable disposable = groupRepository.generateGroupCode(token, role)
                .subscribe(
                        groupCode -> {
                            Fragment fragment = new GeneratedGroupCodeActivity();
                            Bundle bundle = new Bundle();
                            bundle.putString("group_code", groupCode.getCode());
                            fragment.setArguments(bundle);
                            replaceFragment(fragment);
                        },
                        throwable -> Snackbar.make(view, Objects.requireNonNull(throwable.getMessage()), Snackbar.LENGTH_LONG).show()
                );
    }

    public void showQuestionPopUp(){
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_about_kod);

        TextView cancelButton = dialog.findViewById(R.id.button_no);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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
