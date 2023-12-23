package pl.plantoplate.ui.main.settings.helpManager;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentHelpBinding;
import timber.log.Timber;

public class HelpManager extends Fragment {

    private Button shoppinglist;
    private Button baseOfProducts;
    private Button storageList;
    private Button recipeOne;
    private Button recipeAll;
    private Button calendarRecipe;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHelpBinding fragmentHelpBinding = FragmentHelpBinding.inflate(inflater,
                container, false);

        initViews(fragmentHelpBinding);
        setClickListeners();
        return fragmentHelpBinding.getRoot();
    }

    /**
     * Initialize the views
     * @param fragmentDeveloperBinding The binding object
     */
    public void initViews(FragmentHelpBinding fragmentHelpBinding) {
        Timber.d("Initializing views...");
        shoppinglist = fragmentHelpBinding.buttonWygenerowanieKodu;
        baseOfProducts = fragmentHelpBinding.buttonZmianaDanych;
        storageList = fragmentHelpBinding.buttonZarzadyanieUyztkownikamu;
        recipeOne = fragmentHelpBinding.buttonAboutUs;
        recipeAll = fragmentHelpBinding.buttonHelp;
        calendarRecipe = fragmentHelpBinding.buttonWyloguj;
    }

    /**
     * Set the click listeners
     */
    public void setClickListeners() {
        shoppinglist.setOnClickListener(v -> showShoppingList());
        baseOfProducts.setOnClickListener(v -> showbaseOfProducts());
        storageList.setOnClickListener(v -> showStorageList());
        recipeOne.setOnClickListener(v -> showRecipeOne());
        recipeAll.setOnClickListener(v -> showRecipeAll());
        calendarRecipe.setOnClickListener(v -> showCalendarRecipe());
    }

    public void showShoppingList() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.quick_start_1);

        ImageView next = dialog.findViewById(R.id.next);
        ImageView previouse = dialog.findViewById(R.id.previouse);
        ImageView closeButton = dialog.findViewById(R.id.close);

        next.setVisibility(View.INVISIBLE);
        previouse.setVisibility(View.INVISIBLE);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showbaseOfProducts() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.quick_start_2);

        ImageView closeButton = dialog.findViewById(R.id.close);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showStorageList() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.quick_start_3);

        ImageView closeButton = dialog.findViewById(R.id.close);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showRecipeOne() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.quick_start_4);

        ImageView closeButton = dialog.findViewById(R.id.close);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showRecipeAll() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.quick_start_5);

        ImageView closeButton = dialog.findViewById(R.id.close);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showCalendarRecipe() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.quick_start_6);

        ImageView closeButton = dialog.findViewById(R.id.close);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }
}
