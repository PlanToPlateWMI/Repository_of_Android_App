package pl.plantoplate.ui.main.shoppingList.productsDatabase;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import okhttp3.ResponseBody;
import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentProductChangeBinding;
import pl.plantoplate.requests.BaseCallback;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.ui.main.ChangeCategoryOfProductFragment;
import retrofit2.Call;

public class EditOwnProductFragment extends Fragment {

    private FragmentProductChangeBinding fragmentProductChangeBinding;

    private SharedPreferences prefs;
    private SharedPreferences productPrefs;

    private RadioGroup choose_product_unit;
    private Button add_product_button;
    private Button change_kategory;
    private Button cancel_button;
    private TextInputEditText add_product_name;

    private Product product;

    public EditOwnProductFragment(Product product) {
        this.product = product;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the product name
        add_product_name.setText(product.getName());

        // Set the product unit
        switch (product.getUnit()) {
            case "SZT":
                choose_product_unit.check(R.id.button_szt);
                break;

            case "L":
                choose_product_unit.check(R.id.button_l);
                break;

            case "KG":
                choose_product_unit.check(R.id.button_kg);
                break;

            case "GR":
                choose_product_unit.check(R.id.button_gr);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentProductChangeBinding = FragmentProductChangeBinding.inflate(inflater, container, false);

        // Get the shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);
        productPrefs = requireActivity().getSharedPreferences("product", 0);

        // Get the radio group
        choose_product_unit = fragmentProductChangeBinding.toggleGroup;

        // Set the radio group listener
        choose_product_unit.setOnCheckedChangeListener((group, checkedId) -> setProductUnit(checkedId));

        // Set add product button
        add_product_button = fragmentProductChangeBinding.buttonZatwierdz;

        //Set cansel product button
        cancel_button = fragmentProductChangeBinding.buttonAnuluj;

        // Set the product name button
        add_product_name = fragmentProductChangeBinding.enterTheName;

        // Set the button listener
        add_product_button.setOnClickListener(v -> applyProductChange(requireActivity().findViewById(R.id.frame_layout)));
        cancel_button.setOnClickListener(v -> replaceFragment(new ProductsDbaseFragment()));

        change_kategory = fragmentProductChangeBinding.zmienKategorie;

        change_kategory.setOnClickListener(v -> replaceFragment(new ChangeCategoryOfProductFragment()));


        return fragmentProductChangeBinding.getRoot();
    }

    @SuppressLint("NonConstantResourceId")
    public void setProductUnit(int checkedId) {
        switch (checkedId) {
            case R.id.button_szt:
                product.setUnit("SZT");
                break;

            case R.id.button_l:
                product.setUnit("L");
                break;

            case R.id.button_kg:
                product.setUnit("KG");
                break;

            case R.id.button_gr:
                product.setUnit("GR");
                break;
        }
    }

    public void applyProductChange(View view) {

        // Get the product name
        String product_name = Objects.requireNonNull(add_product_name.getText()).toString();

        // Set the product name
        product.setName(product_name);
        String category = productPrefs.getString("category", "");
        if (category.isEmpty()) {
            category = product.getCategory();
        }
        product.setCategory(category);

        // Get the token
        String token = "Bearer " + prefs.getString("token", "");

        // Send the request to add the product to the database
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().changeOwnProduct(token, product.getId(), product);
        call.enqueue(new BaseCallback(view) {
            @Override
            public void handleSuccessResponse(String response) {
                Snackbar.make(view, "Pomyślnie zmieniono produkt.", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void handleErrorResponse(int code) {
                Snackbar.make(view, "Nie udało się zmienić produktu.", Snackbar.LENGTH_LONG).show();
            }
        });

        // Go back to the products database fragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
