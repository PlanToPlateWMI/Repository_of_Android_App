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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Optional;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.product.ProductCategory;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.databinding.FragmentRecipeInsideAllBinding;
import pl.plantoplate.ui.main.recipes.allRecipes.viewModels.AllRecipesViewModel;
import pl.plantoplate.utils.CategorySorter;
import pl.plantoplate.ui.main.recipes.recipeInfo.RecipeInfoFragment;
import pl.plantoplate.ui.main.recipes.recyclerViews.adapters.RecipeCategoryAdapter;
import pl.plantoplate.ui.main.recipes.recyclerViews.listeners.SetupRecipeButtons;
import timber.log.Timber;

public class RecipeCategoriesAllFragment extends Fragment implements SearchView.OnQueryTextListener {

    private AllRecipesViewModel allRecipesViewModel;
    private CompositeDisposable compositeDisposable;
    private RecipeCategoryAdapter recipeCategoryAdapter;
    private FloatingActionButton floatingActionButton;
    private SearchView searchView;
    private RecyclerView recyclerView;

    public RecipeCategoriesAllFragment() {}

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
                Optional.ofNullable(allRecipesViewModel.getAllRecipes()
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

        floatingActionButton = fragmentRecipeInsideAllBinding.plusInAllRecipes;
        floatingActionButton.setVisibility(View.INVISIBLE);
        searchView = requireParentFragment().requireView().findViewById(R.id.search);

        setupRecyclerView(fragmentRecipeInsideAllBinding);
        setupViewModel();
        return fragmentRecipeInsideAllBinding.getRoot();
    }

    private void setupViewModel() {
        allRecipesViewModel = new ViewModelProvider(requireParentFragment()).get(AllRecipesViewModel.class);
        allRecipesViewModel.getAllRecipes().observe(getViewLifecycleOwner(),
                recipes -> recipeCategoryAdapter.setCategoriesList(recipes));
        allRecipesViewModel.fetchAllRecipes();
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Timber.e("onSaveInstanceState");
//        outState.putParcelable("recycler_state", Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState());
//    }
//
//    @Override
//    public void onViewStateRestored(Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        Timber.e("onViewStateRestored");
//        if(savedInstanceState != null){
//            Parcelable recyclerViewState = savedInstanceState.getParcelable("recycler_state");
//            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
//        }
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(savedInstanceState != null){
//            Parcelable recyclerViewState = savedInstanceState.getParcelable("recycler_state");
//            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
//        }
//    }


    public void setupRecyclerView(FragmentRecipeInsideAllBinding fragmentRecipeInsideAllBinding){
        recyclerView = fragmentRecipeInsideAllBinding.recipeRecyclerView;
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
        recyclerView.setAdapter(recipeCategoryAdapter);
    }


    public void getAllRecipes(){
        RecipeRepository recipeRepository = new RecipeRepository();

        Disposable disposable = recipeRepository.getAllRecipes("")
                .subscribe(
                        recipes -> recipeCategoryAdapter.setCategoriesList(CategorySorter.sortCategoriesByRecipe(recipes)),
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