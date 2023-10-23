package pl.plantoplate.ui.main.recepies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentRecipeInsideAllBinding;
import pl.plantoplate.databinding.FragmentRecipeInsideLikeBinding;
import timber.log.Timber;

public class LikeRecipeFragment extends Fragment {

    private FragmentRecipeInsideLikeBinding fragmentRecipeInsideLikeBinding;

    private SharedPreferences prefs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Timber.d("onCreate() called");

        // Inflate the layout for this fragment
        fragmentRecipeInsideLikeBinding = FragmentRecipeInsideLikeBinding.inflate(inflater, container, false);
        // = FragmentRecipeCategoriesBinding.inflate(inflater, container, false);
        //plus_in_kalendarz = fragmentCalendarInsideBldBinding.plusInKalendarz;
        //plus_in_kalendarz.setOnClickListener(v -> replaceFragment(new ProductsDbaseFragment("shoppingList")));

        // get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        return fragmentRecipeInsideLikeBinding.getRoot();
        //return fragmentRecipeCategoriesBinding.getRoot();
    }

    /**
     * Replaces the current fragment with the specified fragment
     *
     * @param fragment the fragment to replace the current fragment with
     */
    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack("trzebaKupicFragment");
        transaction.commit();
    }

}
