package pl.plantoplate.ui.main.recipes.own_recipes;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.databinding.FragmentRecipeInsideNotAllBinding;
import pl.plantoplate.ui.main.recipes.own_recipes.view_models.OwnRecipesViewModel;
import pl.plantoplate.ui.main.recipes.recycler_views.RecipeCategory;
import pl.plantoplate.utils.CategorySorter;
import pl.plantoplate.ui.main.recipes.recipe_info.RecipeInfoFragment;
import pl.plantoplate.ui.main.recipes.recycler_views.adapters.RecipeAdapter;
import pl.plantoplate.ui.main.recipes.recycler_views.listeners.SetupRecipeButtons;

/**
 * Fragment class for displaying concrete own recipes with search functionality.
 */
public class ConcreteOwnRecipesFragment extends Fragment implements SearchView.OnQueryTextListener{

    private CompositeDisposable compositeDisposable;
    private OwnRecipesViewModel ownRecipesViewModel;
    private RecipeAdapter recipeCategoryAdapter;
    private FloatingActionButton floatingActionButton;
    private SearchView searchView;
    private String category;
    private SharedPreferences prefs;
    private String webLink = "https://plantoplatewmi.github.io/WebPage/";
    private TextView welcomeText;
    public ConcreteOwnRecipesFragment() {}
    public ConcreteOwnRecipesFragment(String category) {
        this.category = category;
    }

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
        List<Recipe> recipesCategory = Optional.ofNullable(ownRecipesViewModel.getOwnRecipes().getValue())
                .map(categories -> categories.stream()
                        .filter(currentCategory -> currentCategory.getName().equals(category))
                        .findFirst()
                        .map(RecipeCategory::getRecipes)
                        .orElse(new ArrayList<>())).orElse(new ArrayList<>());

        recipesCategory = CategorySorter.filterRecipesBySearch(recipesCategory, query);
        recipeCategoryAdapter.setRecipesList(CategorySorter.sortRecipesByName(recipesCategory));
        return true;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentRecipeInsideNotAllBinding fragmentRecipeInsideNotAllBinding =
                FragmentRecipeInsideNotAllBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();
        prefs = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE);

        floatingActionButton = fragmentRecipeInsideNotAllBinding.plusInKalendarz;
        welcomeText = fragmentRecipeInsideNotAllBinding.welcomeRecipeAll;
        searchView = requireParentFragment().requireView().findViewById(R.id.search);

        setupRecyclerView(fragmentRecipeInsideNotAllBinding);
        getCategoryRecepies();
        setupViewModel();
        return fragmentRecipeInsideNotAllBinding.getRoot();
    }

    private void setupViewModel() {
        ownRecipesViewModel = new ViewModelProvider(requireParentFragment()).get(OwnRecipesViewModel.class);
        ownRecipesViewModel.getOwnRecipes().observe(getViewLifecycleOwner(),
                recipes -> {
                    List<Recipe> recipesCategory = Optional.ofNullable(ownRecipesViewModel.getOwnRecipes().getValue())
                            .map(categories -> categories.stream()
                                    .filter(currentCategory -> currentCategory.getName().equals(category))
                                    .findFirst()
                                    .map(RecipeCategory::getRecipes)
                                    .orElse(new ArrayList<>())).orElse(new ArrayList<>());
                    recipeCategoryAdapter.setRecipesList(CategorySorter.sortRecipesByName(recipesCategory));
                });
    }

    public void setupRecyclerView(FragmentRecipeInsideNotAllBinding fragmentRecipeInsideNotAllBinding){
        RecyclerView recyclerView = fragmentRecipeInsideNotAllBinding.productsOwnRecyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recipeCategoryAdapter = new RecipeAdapter(new ArrayList<>());
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

    public void getCategoryRecepies(){
        String token = "Bearer " + prefs.getString("token", "");
        RecipeRepository recipeRepository = new RecipeRepository();

        Disposable disposable = recipeRepository.getOwnRecipes(category, token)
                .subscribe(
                        recipes -> {
                            recipeCategoryAdapter.setRecipesList(CategorySorter.sortRecipesByName(recipes));
                            if(recipes.isEmpty()) {
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