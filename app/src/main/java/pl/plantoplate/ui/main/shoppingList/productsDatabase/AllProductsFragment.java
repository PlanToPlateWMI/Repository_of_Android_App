package pl.plantoplate.ui.main.shoppingList.productsDatabase;

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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentWszystkieBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.product.ProductRepository;
import pl.plantoplate.repository.remote.shoppingList.ShoppingListRepository;
import pl.plantoplate.repository.models.Product;
import pl.plantoplate.ui.main.shoppingList.ShoppingListFragment;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.repository.models.Category;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.popups.AddToCartPopUp;

public class AllProductsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentWszystkieBinding fragmentWszystkieBinding;

    private RecyclerView categoryRecyclerView;
    private TextView welcomeTextView;
    private SearchView searchView;

    private SharedPreferences prefs;
    private ProductRepository productRepository;

    private ArrayList<Category> allProductsList;

    @Override
    public void onStart() {
        super.onStart();
        getProducts();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentWszystkieBinding = FragmentWszystkieBinding.inflate(inflater, container, false);

        // get views
        welcomeTextView = fragmentWszystkieBinding.textView3;
        searchView = requireActivity().findViewById(R.id.search);

        // Get the SharedPreferences object
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // get repository
        productRepository = new ProductRepository();

        // set up listeners
        searchView.setOnQueryTextListener(this);

        // set up recycler view
        setUpRecyclerView();
        return fragmentWszystkieBinding.getRoot();
    }

    private void getProducts(){
        String token = "Bearer " + prefs.getString("token", "");
        productRepository.getAllProducts(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> productsDataBase) {
                allProductsList = CategorySorter.sortCategoriesByProduct(productsDataBase);
                if (allProductsList.isEmpty()) {
                    welcomeTextView.setVisibility(View.VISIBLE);
                } else {
                    welcomeTextView.setVisibility(View.GONE);
                }
                CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
                Objects.requireNonNull(categoryAdapter).setCategoriesList(allProductsList);
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void addProductToShoppingList(Product product) {
        String token = "Bearer " + prefs.getString("token", "");
        ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
        shoppingListRepository.addProductToShopList(token, product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), "Dodano produkt do listy zakupów", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });
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

    public void showAddProductPopup(Product product) {
        AddToCartPopUp addToCartPopUp = new AddToCartPopUp(requireContext(), product);
        addToCartPopUp.acceptButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(addToCartPopUp.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                addToCartPopUp.quantity.setError("Podaj ilość");
                return;
            }
            if (quantityValue.endsWith(".")) {
                // Remove dot at the end
                quantityValue = quantityValue.substring(0, quantityValue.length() - 1);
            }
            float quantity = BigDecimal.valueOf(Float.parseFloat(quantityValue)).setScale(3, RoundingMode.HALF_UP).floatValue();
            product.setAmount(quantity);
            addProductToShoppingList(product);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ShoppingListFragment()).commit();
            addToCartPopUp.dismiss();
        });
        addToCartPopUp.show();
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
                product.setAmount(1.0f);
                addProductToShoppingList(product);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ShoppingListFragment()).commit();
            }

            @Override
            public void onProductItemClick(View v, Product product) {
                showAddProductPopup(product);
            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
