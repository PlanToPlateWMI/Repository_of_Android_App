package pl.plantoplate.ui.main.storage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentStorageBinding;
import pl.plantoplate.databinding.FragmentStorageInsideBinding;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.ProductsDbaseFragment;

public class StorageInsideFragment extends Fragment {

    private FragmentStorageInsideBinding fragmentStorageInsideBinding;
    private FloatingActionButton plus_in_storage;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentStorageInsideBinding = FragmentStorageInsideBinding.inflate(inflater, container, false);
        plus_in_storage = fragmentStorageInsideBinding.plusInStorage;
        plus_in_storage.setOnClickListener(v -> replaceFragment(new ProductsDbaseFragment()));
        return fragmentStorageInsideBinding.getRoot();
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutStorage, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
