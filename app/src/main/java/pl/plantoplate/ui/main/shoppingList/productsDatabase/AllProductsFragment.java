package pl.plantoplate.ui.main.shoppingList.productsDatabase;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.storage.StorageRepository;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.repository.remote.models.Category;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.popups.ModifyProductpopUp;

public class AllProductsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentWszystkieBinding fragmentWszystkieBinding;

    private RecyclerView categoryRecyclerView;
    private TextView welcomeTextView;
    private SearchView searchView;

    private SharedPreferences prefs;
    private ProductRepository productRepository;

    private ArrayList<Category> allProductsList;

    public AllProductsFragment() {
    }

    public AllProductsFragment(String comesFrom) {

        Bundle bundle = new Bundle();
        bundle.putString("comesFrom", comesFrom);
        this.setArguments(bundle);
    }

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

        //test
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        for (int i=0; i<backStackEntryCount; i++) {
            FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(i);
            System.out.println(entry.getName());
        }

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

    public void addProduct(Product product){
        String comesFrom = "";
        if (getArguments() != null) {
            comesFrom = getArguments().getString("comesFrom");
        }
        else{
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            for (int i=backStackEntryCount - 1; i>0; i--) {
                FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(i);
                String name = entry.getName();
                if (Objects.equals(name, "shoppingList") || Objects.equals(name, "storage")) {
                    comesFrom = name;
                }
            }
        }
        switch (comesFrom) {
            case "shoppingList":
                addProductToShoppingList(product);
                break;
            case "storage":
                addProductToStorage(product);
                break;
            default:
                addProductToShoppingList(product);
                break;
        }
    }

    public void addProductToShoppingList(Product product) {
        String token = "Bearer " + prefs.getString("token", "");
        ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
        shoppingListRepository.addProductToShopList(token, product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                String message = "Produkt " + product.getName() + " w ilości " + product.getAmount() + " " + product.getUnit() + " został dodany do listy zakupów";
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), message, Snackbar.LENGTH_LONG).show();
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

    public void addProductToStorage(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        StorageRepository storageRepository = new StorageRepository();
        storageRepository.addProductToStorage(token, product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                String message = "Produkt " + product.getName() + " w ilości " + product.getAmount() + " " + product.getUnit() + " został dodany do spiżarni";
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), message, Snackbar.LENGTH_LONG).show();
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
        ModifyProductpopUp addToCartPopUp = new ModifyProductpopUp(requireContext(), product);
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
            addProduct(product);
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
        categoryAdapter.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupAddToShoppingListButtonClick(View v, Product product) {
                v.setOnClickListener(view -> {
                    product.setAmount(1.0f);
                    addProduct(product);
                });
            }

            @Override
            public void setupProductItemClick(View v, Product product) {
                v.setOnClickListener(view -> showAddProductPopup(product));
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
