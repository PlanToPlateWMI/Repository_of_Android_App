package pl.plantoplate.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentBazaProduktowBinding;
import pl.plantoplate.ui.main.shoplist.KupioneFragment;
import pl.plantoplate.ui.main.shoplist.TrzebaKupicFragment;

public class BazaProduktowFragment extends Fragment {

    private FragmentBazaProduktowBinding bazaProduktowBinding;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bazaProduktowBinding = FragmentBazaProduktowBinding.inflate(inflater, container, false);
        replaceFragment(new WszystkieFragment());

        bazaProduktowBinding.bottomNavigationView2.setOnItemSelectedListener(item ->{
            switch (item.getItemId()) {
                case R.id.wlasne:
                    replaceFragment(new WlasneFragment());
                    return true;
                case R.id.wszystkie:
                    replaceFragment(new WszystkieFragment());
                    return true;
            }
            return false;
        });

        return bazaProduktowBinding.getRoot();
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.baza_def, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}