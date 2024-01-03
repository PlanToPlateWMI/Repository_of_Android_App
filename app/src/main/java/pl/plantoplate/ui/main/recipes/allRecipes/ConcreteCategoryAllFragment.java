package pl.plantoplate.ui.main.recipes.allRecipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
import pl.plantoplate.ui.main.recipes.allRecipes.viewModels.AllRecipesViewModel;
import pl.plantoplate.ui.main.recipes.recyclerViews.RecipeCategory;
import pl.plantoplate.utils.CategorySorter;
import pl.plantoplate.ui.main.recipes.recipeInfo.RecipeInfoFragment;
import pl.plantoplate.ui.main.recipes.recyclerViews.adapters.RecipeAdapter;
import pl.plantoplate.ui.main.recipes.recyclerViews.listeners.SetupRecipeButtons;

public class ConcreteCategoryAllFragment extends Fragment implements SearchView.OnQueryTextListener {

    private CompositeDisposable compositeDisposable;
    private AllRecipesViewModel allRecipesViewModel;
    private RecipeAdapter recipeAdapter;
    private FloatingActionButton floatingActionButton;
    private SearchView searchView;
    private String category;

    public ConcreteCategoryAllFragment() {
    }

    public ConcreteCategoryAllFragment(String category) {
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
        ArrayList<Recipe> recipesCategory = Optional.ofNullable(allRecipesViewModel.getAllRecipes().getValue())
                .map(categories -> categories.stream()
                        .filter(currentCategory -> currentCategory.getName().equals(category))
                        .findFirst()
                        .map(RecipeCategory::getRecipes)
                        .orElse(new ArrayList<>())).orElse(new ArrayList<>());

        recipesCategory = CategorySorter.filterRecipesBySearch(recipesCategory, query);
        recipeAdapter.setRecipesList(CategorySorter.sortRecipesByName(recipesCategory));
        return true;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentRecipeInsideNotAllBinding fragmentRecipeInsideNotAllBinding =
                FragmentRecipeInsideNotAllBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();

        floatingActionButton = fragmentRecipeInsideNotAllBinding.plusInKalendarz;
        floatingActionButton.setVisibility(View.INVISIBLE);
        searchView = requireParentFragment().requireView().findViewById(R.id.search);

        setupRecyclerView(fragmentRecipeInsideNotAllBinding);
        setupViewModel();
        return fragmentRecipeInsideNotAllBinding.getRoot();
    }

    private void setupViewModel() {
        allRecipesViewModel = new ViewModelProvider(requireParentFragment()).get(AllRecipesViewModel.class);
        allRecipesViewModel.getAllRecipes().observe(getViewLifecycleOwner(),
                recipes -> {
                    ArrayList<Recipe> recipesCategory = Optional.ofNullable(allRecipesViewModel.getAllRecipes().getValue())
                            .map(categories -> categories.stream()
                                    .filter(currentCategory -> currentCategory.getName().equals(category))
                                    .findFirst()
                                    .map(RecipeCategory::getRecipes)
                                    .orElse(new ArrayList<>())).orElse(new ArrayList<>());
                    recipeAdapter.setRecipesList(CategorySorter.sortRecipesByName(recipesCategory));
                });
    }


    public void setupRecyclerView(FragmentRecipeInsideNotAllBinding fragmentRecipeInsideNotAllBinding){
        RecyclerView recyclerView = fragmentRecipeInsideNotAllBinding.productsOwnRecyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recipeAdapter = new RecipeAdapter(new ArrayList<>());
        recipeAdapter.setUpRecipeButtons(new SetupRecipeButtons() {
            @Override
            public void setupOnItemClick(int id) {
                RecipeInfoFragment recipeInfoFragment = new RecipeInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("recipeId", id);
                recipeInfoFragment.setArguments(bundle);
                replaceFragment(recipeInfoFragment);
            }
        });
        recyclerView.setAdapter(recipeAdapter);
    }


    public void getCategoryRecepies(){
        RecipeRepository recipeRepository = new RecipeRepository();

        Disposable disposable = recipeRepository.getAllRecipes(category)
                .subscribe(
                        recipes -> recipeAdapter.setRecipesList(CategorySorter.sortRecipesByName(recipes)),
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