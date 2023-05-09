package pl.plantoplate.ui.main.shoplist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentShoppingListBinding;
import pl.plantoplate.databinding.FragmentTrzebaKupicBinding;
import pl.plantoplate.ui.main.BazaProduktowFragment;

public class TrzebaKupicFragment extends Fragment {

    private FragmentTrzebaKupicBinding fragmentTrzebaKupicBinding;
    private FloatingActionButton plus_in_trzeba_kupic;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentTrzebaKupicBinding = FragmentTrzebaKupicBinding.inflate(inflater, container, false);
        plus_in_trzeba_kupic = fragmentTrzebaKupicBinding.plusInTrzebaKupic;
        plus_in_trzeba_kupic.setOnClickListener(v -> replaceFragment(new BazaProduktowFragment()));
        return fragmentTrzebaKupicBinding.getRoot();
        //return inflater.inflate(R.layout.fragment_trzeba_kupic, container, false);
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}