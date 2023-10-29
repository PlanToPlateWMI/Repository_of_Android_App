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
package pl.plantoplate.ui.main.recepies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;

import pl.plantoplate.databinding.FragmentRecipeInsideAllBinding;
import pl.plantoplate.tools.DateUtils;
import pl.plantoplate.ui.main.calendar.recyclerViews.adapters.CalendarAdapter;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import timber.log.Timber;

public class AllRecipeFragment extends Fragment {

    FragmentRecipeInsideAllBinding fragmentRecipeInsideAllBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRecipeInsideAllBinding fragmentRecipeInsideAllBinding = FragmentRecipeInsideAllBinding.inflate(inflater, container, false);

        return fragmentRecipeInsideAllBinding.getRoot();
    }

//    public void setupRecyclerView(){
//        RecyclerView recyclerView = fragmentRecipeInsideAllBinding.recipeRecyclerView;
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//    }
}
