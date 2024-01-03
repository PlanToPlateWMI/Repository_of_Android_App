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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.Objects;
import java.util.Optional;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.repository.ProductRepository;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.databinding.FragmentAddYourOwnProductBinding;
import pl.plantoplate.ui.main.productsDatabase.events.ChangeCategoryEvent;
import timber.log.Timber;

/**
 * This fragment is responsible for adding a new product to the database.
 */
public class AddYourOwnProductFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private SharedPreferences prefs;
    private RadioGroup chooseProductUnitRadioGroup;
    private Button addProductButton;
    private Button changeProductCategoryButton;
    private TextInputEditText productNameEditText;
    private TextView productCategoryTextView;
    private Product product;
    private ProductRepository productRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        product = product == null ? new Product() : product;
        product.setCategory("Produkty własne");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAddYourOwnProductBinding addYourOwnProductBinding = FragmentAddYourOwnProductBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();
        prefs = requireActivity().getSharedPreferences("prefs", 0);
        productRepository = new ProductRepository();

        initViews(addYourOwnProductBinding);
        setClickListeners();
        return addYourOwnProductBinding.getRoot();
    }

    public void initViews(FragmentAddYourOwnProductBinding add_own_product_view) {
        chooseProductUnitRadioGroup = add_own_product_view.toggleGroup;
        productCategoryTextView = add_own_product_view.kategoriaNapisac;
        addProductButton = add_own_product_view.buttonZatwierdz;
        productNameEditText = add_own_product_view.enterTheName;
        changeProductCategoryButton = add_own_product_view.zmienKategorie;

        productCategoryTextView.setText("Kategoria: " + product.getCategory());
    }

    private void setClickListeners() {
        chooseProductUnitRadioGroup.setOnCheckedChangeListener((group, checkedId) -> setProductUnit(checkedId));
        addProductButton.setOnClickListener(v -> addProduct());
        changeProductCategoryButton.setOnClickListener(v -> replaceFragment(new ChangeCategoryOfProductFragment()));
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

    @Subscribe
    public void onCategoryChosen(ChangeCategoryEvent event) {
        product.setCategory(event.getCategory());
        productCategoryTextView.setText("Kategoria: " + event.getCategory());
        Timber.e("Category changed to: %s", event.getCategory());
    }

    public void addProduct() {
        String product_name = Optional.ofNullable(productNameEditText.getText()).map(Objects::toString).orElse("");
        product.setName(product_name);
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = productRepository.addProduct(token, product)
                .subscribe(
                        response -> {
                            Toast.makeText(requireActivity(), "Produkt został dodany",
                                    Toast.LENGTH_SHORT).show();

                            // Go back to the products database fragment
                            getParentFragmentManager().popBackStack("addOwn", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        },
                        error ->
                                Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show()
                );

        compositeDisposable.add(disposable);
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