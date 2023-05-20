package pl.plantoplate.ui.main.shoppingList.productsDatabase;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentWszystkieBinding;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.products.GetProductsDBaseCallback;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.requests.products.ProductsListCallback;
import pl.plantoplate.requests.shoppingList.AddProductToShopListCallback;
import pl.plantoplate.ui.main.shoppingList.ShoppingListFragment;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.Category;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.tools.CategorySorter;
import retrofit2.Call;

public class AllProductsFragment extends Fragment implements SearchView.OnQueryTextListener, ProductsListCallback {

    private FragmentWszystkieBinding fragmentWszystkieBinding;


    private FloatingActionButton floatingActionButton_wszystkie;
    private RecyclerView categoryRecyclerView;
    private TextView welcomeTextView;
    private SearchView searchView;

    private SharedPreferences prefs;

    private ArrayList<Category> allProductsList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentWszystkieBinding = FragmentWszystkieBinding.inflate(inflater, container, false);
        welcomeTextView = fragmentWszystkieBinding.textView3;
        searchView = requireActivity().findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        floatingActionButton_wszystkie = fragmentWszystkieBinding.floatingActionButtonWszystkie;
        floatingActionButton_wszystkie.setOnClickListener(v -> replaceFragment(new AddYourOwnProductFragment()));
        prefs = requireActivity().getSharedPreferences("prefs", 0);
        setUpRecyclerView();
        return fragmentWszystkieBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getProducts();
    }

    private void getProducts() {
        String token = "Bearer " + prefs.getString("token", "");
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getProducts(token);
        call.enqueue(new GetProductsDBaseCallback(requireActivity().findViewById(R.id.frame_layout), this));
    }

    @Override
    public void onProductsListsReceived(ArrayList<Product> generalProductsList, ArrayList<Product> groupProductsList) {
        ArrayList<Product> allProductsList = new ArrayList<>();
        allProductsList.addAll(generalProductsList);
        allProductsList.addAll(groupProductsList);
        this.allProductsList = CategorySorter.sortCategoriesByProduct(allProductsList);
        if (allProductsList.isEmpty()) {
            welcomeTextView.setVisibility(View.VISIBLE);
        } else {
            welcomeTextView.setVisibility(View.GONE);
        }
        CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
        Objects.requireNonNull(categoryAdapter).setCategoriesList(this.allProductsList);
    }

    public void addProductToShoppingList(Product product, int amount) {
        String token = "Bearer " + prefs.getString("token", "");
        product.setAmount(amount);
        Call<ResponseBody> myCall = RetrofitClient.getInstance().getApi().addProductToShopList(token, product);
        myCall.enqueue(new AddProductToShopListCallback(requireActivity().findViewById(R.id.frame_layout)));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        ArrayList<Product> filteredProducts = CategorySorter.filterCategoriesBySearch(allProductsList, query);
        ArrayList<Category> filteredList = CategorySorter.sortCategoriesByProduct(filteredProducts);
        CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
        Objects.requireNonNull(categoryAdapter).setCategoriesList(filteredList);
        return false;
    }

    public void setUpRecyclerView() {
        if (allProductsList == null) {
            allProductsList = new ArrayList<>();
        }
        categoryRecyclerView = fragmentWszystkieBinding.categoryRecyclerView;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdapter categoryAdapter = new CategoryAdapter(allProductsList, R.layout.item_wszystkie_produkt);
        categoryAdapter.setOnProductItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onAddToShoppingListButtonClick(View v, Product product) {
                addProductToShoppingList(product, 1);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ShoppingListFragment()).commit();
            }

            @Override
            public void onProductItemClick(View v, Product product) {
                showAddProductPopup(product);
            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    public void showAddProductPopup(Product product) {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pop_up_change_in_product_quantity);
        ImageView plusButton = dialog.findViewById(R.id.plus);
        ImageView minusButton = dialog.findViewById(R.id.minus);
        TextInputEditText quantityTextView = dialog.findViewById(R.id.ilosc);
        ImageView closeButton = dialog.findViewById(R.id.close);
        TextView productUnitTextView = dialog.findViewById(R.id.jednostki_miary_napisac);
        Button acceptButton = dialog.findViewById(R.id.zatwierdzenie);
        quantityTextView.setText("0");
        productUnitTextView.setText(product.getUnit());
        closeButton.setOnClickListener(v -> dialog.dismiss());
        plusButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(Objects.requireNonNull(quantityTextView.getText()).toString());
            quantity++;
            quantityTextView.setText(String.valueOf(quantity));
        });
        minusButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(Objects.requireNonNull(quantityTextView.getText()).toString());
            if (quantity > 1) {
                quantity--;
                quantityTextView.setText(String.valueOf(quantity));
            }
        });
        acceptButton.setOnClickListener(v -> {
            product.setAmount(Integer.parseInt(Objects.requireNonNull(quantityTextView.getText()).toString()));
            addProductToShoppingList(product, product.getAmount());
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ShoppingListFragment()).commit();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
