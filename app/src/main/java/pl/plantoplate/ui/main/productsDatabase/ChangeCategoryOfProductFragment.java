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
package pl.plantoplate.ui.main.productsDatabase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import org.greenrobot.eventbus.EventBus;
import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentChangeCategoryBinding;
import pl.plantoplate.ui.customViews.RadioGridGroup;
import pl.plantoplate.ui.main.productsDatabase.events.ChangeCategoryEvent;

/**
 * This fragment is used to change category of product.
 */
public class ChangeCategoryOfProductFragment extends Fragment {

    private RadioGridGroup categoriesRadioGroup;
    private Button acceptButton;

    /**
     * Called when the fragment should create its view hierarchy.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentChangeCategoryBinding fragmentChangeCategoryBinding = FragmentChangeCategoryBinding.inflate(inflater, container, false);

        initViews(fragmentChangeCategoryBinding);
        setClickListeners();
        return fragmentChangeCategoryBinding.getRoot();
    }

    public void initViews(FragmentChangeCategoryBinding fragmentChangeCategoryBinding){
        categoriesRadioGroup = fragmentChangeCategoryBinding.radioGroupNowaKategoria;
        acceptButton = fragmentChangeCategoryBinding.buttonZatwierdz;
    }

    public void setClickListeners(){
        acceptButton.setOnClickListener(v -> applyChanges());
    }

    /**
     * Applies the changes made in the fragment.
     * It retrieves the checked radio button from the categories radio group.
     * If no radio button is checked, it displays a Snackbar with an error message and returns.
     * Otherwise, it calls the onCategoryChosen() method of the change category listener,
     * passing the text of the checked radio button as the chosen category.
     * Finally, it closes the fragment by popping the back stack of the parent fragment manager.
     */
    public void applyChanges() {
        RadioButton checkedRadioButton = categoriesRadioGroup.getCheckedRadioButton();
        if (checkedRadioButton == null) {
            Snackbar.make(requireActivity().findViewById(R.id.frame_layout), "Wybierz kategorię!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        EventBus.getDefault().post(new ChangeCategoryEvent(checkedRadioButton.getText().toString()));
        getParentFragmentManager().popBackStack();
    }
}