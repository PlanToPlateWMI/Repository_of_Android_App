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
package pl.plantoplate.ui.main.products_database;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.textfield.TextInputEditText;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.repository.ProductRepository;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.databinding.FragmentProductChangeBinding;
import pl.plantoplate.ui.main.products_database.events.ChangeCategoryEvent;

/**
 * This fragment is used to edit the product.
 */
public class EditOwnProductFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private SharedPreferences prefs;
    private RadioGroup productUnitRadioGroup;
    private Button applyChangesButton;
    private Button changeCategoryButton;
    private Button deleteProductButton;
    private TextInputEditText productNameEditText;
    private TextView productCategoryTextView;
    private Product product;
    private ProductRepository productRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        if (getArguments() != null) {
            product = getArguments().getParcelable("product");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentProductChangeBinding fragmentProductChangeBinding = FragmentProductChangeBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();
        productRepository = new ProductRepository();
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        initViews(fragmentProductChangeBinding);
        setClickListeners();
        return fragmentProductChangeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productNameEditText.setText(product.getName());
        setupProductUnit();
    }

    public void initViews(FragmentProductChangeBinding fragmentProductChangeBinding) {
        productUnitRadioGroup = fragmentProductChangeBinding.toggleGroup;
        applyChangesButton = fragmentProductChangeBinding.buttonZatwierdz;
        productNameEditText = fragmentProductChangeBinding.enterTheName;
        changeCategoryButton = fragmentProductChangeBinding.zmienKategorie;
        productCategoryTextView = fragmentProductChangeBinding.kategoriaNapis;
        deleteProductButton = fragmentProductChangeBinding.buttonUsun;

        productCategoryTextView.setText("Kategoria: " + product.getCategory());
    }

    private void setClickListeners() {
        productUnitRadioGroup.setOnCheckedChangeListener((group, checkedId) -> setProductUnit(checkedId));
        applyChangesButton.setOnClickListener(v -> applyProductChange());
        changeCategoryButton.setOnClickListener(v -> replaceFragment(new ChangeCategoryOfProductFragment()));
        deleteProductButton.setOnClickListener(v -> showConfirmDeleteProductPopUp());
    }

    public void setupProductUnit() {
        Map<String, Integer> unitToButtonId = new HashMap<>();
        unitToButtonId.put("SZT", R.id.button_szt);
        unitToButtonId.put("L", R.id.button_l);
        unitToButtonId.put("KG", R.id.button_kg);
        unitToButtonId.put("GR", R.id.button_gr);
        unitToButtonId.put("ML", R.id.button_ml);
        Integer buttonId = unitToButtonId.get(product.getUnit());
        if (buttonId != null) {
            productUnitRadioGroup.check(buttonId);
        }
    }

    public void setProductUnit(int checkedId) {
        SparseArray<String> unitMapping = new SparseArray<>();
        unitMapping.put(R.id.button_szt, "SZT");
        unitMapping.put(R.id.button_l, "L");
        unitMapping.put(R.id.button_kg, "KG");
        unitMapping.put(R.id.button_gr, "GR");
        unitMapping.put(R.id.button_ml, "ML");
        String selectedUnit = unitMapping.get(checkedId);
        if (selectedUnit != null) {
            product.setUnit(selectedUnit);
        }
    }

    public void applyProductChange() {
        String productName = Optional.ofNullable(productNameEditText.getText()).map(CharSequence::toString)
                .orElse("");
        product.setName(productName);
        saveProduct();
    }

    public void showConfirmDeleteProductPopUp() {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_delete_product_from_database);
        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {
            deleteProduct();
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void saveProduct() {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = productRepository.modifyProduct(token, product.getId(), product)
                .subscribe(
                        response -> {
                            Toast.makeText(requireActivity(), "Pomyślnie zmieniono produkt.", Toast.LENGTH_SHORT).show();
                            requireActivity().getSupportFragmentManager().popBackStack();
                        },
                        error -> Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show()
                );

        compositeDisposable.add(disposable);
    }

    public void deleteProduct() {
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = productRepository.deleteProduct(token, product.getId())
                .subscribe(
                        response -> {
                            Toast.makeText(requireActivity(), "Pomyślnie usunięto produkt.", Toast.LENGTH_SHORT).show();
                            requireActivity().getSupportFragmentManager().popBackStack();
                        },
                        error -> Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show()
                );

        compositeDisposable.add(disposable);
    }

    @Subscribe
    public void onCategoryChosen(ChangeCategoryEvent event) {
        product.setCategory(event.getCategory());
        productCategoryTextView.setText("Kategoria: " + event.getCategory());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        EventBus.getDefault().unregister(this);
    }
}