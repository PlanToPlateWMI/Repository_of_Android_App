package pl.plantoplate.ui.main.calendar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentCalendarInsideAllBinding;
import pl.plantoplate.databinding.FragmentCalendarInsideBldBinding;
import pl.plantoplate.databinding.FragmentTrzebaKupicBinding;
import pl.plantoplate.ui.main.shoppingList.viewModels.ShoppingListViewModel;

public class AllCategoryProductsFragment extends Fragment {

    private FragmentCalendarInsideAllBinding fragmentCalendarInsideBldBinding;

    private FloatingActionButton plus_in_kalendarz;

    private SharedPreferences prefs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentCalendarInsideBldBinding = FragmentCalendarInsideAllBinding.inflate(inflater, container, false);
        //plus_in_kalendarz = fragmentCalendarInsideBldBinding.plusInKalendarz;
        //plus_in_kalendarz.setOnClickListener(v -> replaceFragment(new ProductsDbaseFragment("shoppingList")));

        // get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        return fragmentCalendarInsideBldBinding.getRoot();
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
