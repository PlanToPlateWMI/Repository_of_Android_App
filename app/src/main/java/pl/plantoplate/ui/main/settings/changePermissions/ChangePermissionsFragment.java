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

/**
 * This fragment is responsible for changing the permissions of the user.
 */

package pl.plantoplate.ui.main.settings.changePermissions;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentNameChangeBinding;
import pl.plantoplate.databinding.FragmentPermissionsChangeBinding;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.models.UserInfo;
import pl.plantoplate.repository.remote.user.UserRepository;
import pl.plantoplate.ui.main.settings.viewModels.SettingsViewModel;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;

/**
 * This fragment is responsible for changing the permissions of the user.
 */
public class ChangePermissionsFragment extends Fragment {

    private FragmentPermissionsChangeBinding fragmentPermissionsChangeBinding;
    private SharedPreferences prefs;
    private UserRepository userRepository;

    private RecyclerView usersRecyclerView;

    private ChangePermissionsViewModel changePermissionsViewModel;

    /**
     * Create the view for this fragment, get the buttons for choosing group code type and set the
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return root view of this fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentPermissionsChangeBinding = FragmentPermissionsChangeBinding.inflate(inflater, container, false);

        userRepository = new UserRepository();

        // get the recycler view
        usersRecyclerView = fragmentPermissionsChangeBinding.usersRecyclerView;

        // get shared preferences object
        // prefs = requireActivity().getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE);

        setUpRecyclerView();
        setUpViewModel();

        return fragmentPermissionsChangeBinding.getRoot();
    }

    public void showChangePermissionsPopUp(UserInfo userInfo){
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.new_pop_up_permissions_change);

        TextView info = dialog.findViewById(R.id.text_permissions);
        TextView button_no = dialog.findViewById(R.id.button_no);
        TextView button_yes = dialog.findViewById(R.id.button_yes);

        String perm_from = userInfo.getRole().equals("USER") ? "Użytkownik": "Administrator";
        String perm_to = perm_from.equals("Użytkownik") ? "Administrator": "Użytkownik";

        // set info message
        String msg = "Czy zmienić uprawnienia użytkownika " + userInfo.getUsername() + " z " + perm_to +
                " na " + perm_from + " ?";
        info.setText(msg);

        button_no.setOnClickListener(v -> dialog.dismiss());
        button_yes.setOnClickListener(v -> {
            changePermissionsViewModel.changePermissions(userInfo);
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showSuccessPopUp(){
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.new_pop_up_correct_permisions_change);

        TextView button_ok = dialog.findViewById(R.id.button_yes);

        button_ok.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void setUpViewModel() {

        // get storage view model
        changePermissionsViewModel = new ViewModelProvider(this).get(ChangePermissionsViewModel.class);

        // get storage title
        changePermissionsViewModel.getUsersInfo().observe(getViewLifecycleOwner(), userInfo -> {

            // update recycler view
            UsersInfoAdapter usersInfoAdapter = (UsersInfoAdapter) usersRecyclerView.getAdapter();
            Objects.requireNonNull(usersInfoAdapter).setUserInfosList(userInfo);
        });

        changePermissionsViewModel.getSuccess().observe(getViewLifecycleOwner(), successMessage -> {
            showSuccessPopUp();
        });

        // get error message
        changePermissionsViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void setUpRecyclerView() {

        usersRecyclerView = fragmentPermissionsChangeBinding.usersRecyclerView;
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        UsersInfoAdapter usersInfoAdapter = new UsersInfoAdapter(new ArrayList<>(), R.layout.item_user);
        usersInfoAdapter.setUpItemButtons(new SetupUserPermissionsItems() {
            @Override
            public void setupEditPermissionsButtonClick(View v, UserInfo userInfo) {
                UserInfo newUserInfo = new UserInfo();
                newUserInfo.setEmail(userInfo.getEmail());
                newUserInfo.setUsername(userInfo.getUsername());
                if (userInfo.getRole().equals("ROLE_ADMIN")) {
                    newUserInfo.setRole("USER");
                } else {
                    newUserInfo.setRole("ADMIN");
                }
                v.setOnClickListener(view -> showChangePermissionsPopUp(newUserInfo));
            }
        });
        usersRecyclerView.setAdapter(usersInfoAdapter);
    }




}
