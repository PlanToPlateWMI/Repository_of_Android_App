package pl.plantoplate.ui.main.recipes.ownRecipes;

import static android.content.Context.MODE_PRIVATE;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Optional;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.databinding.FragmentRecipeInsideAllBinding;
import pl.plantoplate.ui.main.recipes.ownRecipes.viewModels.OwnRecipesViewModel;
import pl.plantoplate.utils.CategorySorter;
import pl.plantoplate.ui.main.recipes.recipeInfo.RecipeInfoFragment;
import pl.plantoplate.ui.main.recipes.recyclerViews.adapters.RecipeCategoryAdapter;
import pl.plantoplate.ui.main.recipes.recyclerViews.listeners.SetupRecipeButtons;

public class RecipeCategoriesOwnFragment extends Fragment implements SearchView.OnQueryTextListener{

    private CompositeDisposable compositeDisposable;
    private RecipeCategoryAdapter recipeCategoryAdapter;
    private FloatingActionButton floatingActionButton;
    private SearchView searchView;
    private OwnRecipesViewModel ownRecipesViewModel;
    private SharedPreferences prefs;
    private String webLink = "https://plantoplatewmi.github.io/WebPage/";
    private TextView welcomeText;

    public RecipeCategoriesOwnFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.clearFocus();
        searchView.setQuery("", false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        ArrayList<Recipe> recipes = CategorySorter.filterRecipesCategoriesBySearch(
                Optional.ofNullable(ownRecipesViewModel.getOwnRecipes()
                        .getValue()).orElse(new ArrayList<>()), query.trim());
        recipeCategoryAdapter.setCategoriesList(CategorySorter.sortCategoriesByRecipe(recipes));
        return true;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentRecipeInsideAllBinding fragmentRecipeInsideAllBinding =
                FragmentRecipeInsideAllBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();

        prefs = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE);

        floatingActionButton = fragmentRecipeInsideAllBinding.plusInAllRecipes;
        welcomeText = fragmentRecipeInsideAllBinding.welcomeRecipeAll;
        searchView = requireParentFragment().requireView().findViewById(R.id.search);

        setupRecyclerView(fragmentRecipeInsideAllBinding);
        getAllRecepies();
        setupViewModel();
        return fragmentRecipeInsideAllBinding.getRoot();
    }

    private void setupViewModel() {
        ownRecipesViewModel = new ViewModelProvider(requireParentFragment()).get(OwnRecipesViewModel.class);
        ownRecipesViewModel.getOwnRecipes().observe(getViewLifecycleOwner(),
                recipes -> recipeCategoryAdapter.setCategoriesList(recipes));
        ownRecipesViewModel.fetchOwnRecipes();
    }

    public void setupRecyclerView(FragmentRecipeInsideAllBinding fragmentRecipeInsideAllBinding){
        RecyclerView recyclerView = fragmentRecipeInsideAllBinding.recipeRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeCategoryAdapter = new RecipeCategoryAdapter(new ArrayList<>());
        recipeCategoryAdapter.setUpRecipeButtons(new SetupRecipeButtons() {
            @Override
            public void setupOnItemClick(int id) {
                RecipeInfoFragment recipeInfoFragment = new RecipeInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("recipeId", id);
                recipeInfoFragment.setArguments(bundle);
                replaceFragment(recipeInfoFragment);
            }
        });

        String role = prefs.getString("role", "");

        if(role.equals("ROLE_ADMIN")) {
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setOnClickListener(item -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webLink));
                startActivity(browserIntent);
            });
        }
        else {
            floatingActionButton.setVisibility(View.INVISIBLE);
        }

        recyclerView.setAdapter(recipeCategoryAdapter);
    }


    public void getAllRecepies(){
        String token = "Bearer " + prefs.getString("token", "");
        RecipeRepository recipeRepository = new RecipeRepository();

        Disposable disposable = recipeRepository.getOwnRecipes("", token)
                .subscribe(
                        recipes -> {
                            recipeCategoryAdapter.setCategoriesList(CategorySorter.sortCategoriesByRecipe(recipes));
                            if(recipes.size() == 0) {
                                welcomeText.setText(R.string.wprowadzenie_przepisy_ulubione);
                            }
                            else {
                                welcomeText.setText("");
                            }
                        },
                        throwable -> Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show()
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
