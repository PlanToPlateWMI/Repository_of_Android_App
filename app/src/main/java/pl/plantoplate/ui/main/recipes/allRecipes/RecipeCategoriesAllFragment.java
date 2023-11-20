package pl.plantoplate.ui.main.recipes.allRecipes;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.databinding.FragmentRecipeInsideAllBinding;
import pl.plantoplate.utils.CategorySorter;
import pl.plantoplate.ui.main.recipes.recipeInfo.RecipeInfoFragment;
import pl.plantoplate.ui.main.recipes.recyclerViews.adapters.RecipeCategoryAdapter;
import pl.plantoplate.ui.main.recipes.recyclerViews.listeners.SetupRecipeButtons;

public class RecipeCategoriesAllFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private RecipeCategoryAdapter recipeCategoryAdapter;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentRecipeInsideAllBinding fragmentRecipeInsideAllBinding =
                FragmentRecipeInsideAllBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();

        floatingActionButton = fragmentRecipeInsideAllBinding.plusInAllRecipes;
        floatingActionButton.setVisibility(View.INVISIBLE);

        setupRecyclerView(fragmentRecipeInsideAllBinding);
        getAllRecepies();
        return fragmentRecipeInsideAllBinding.getRoot();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            Parcelable recyclerViewState = savedInstanceState.getParcelable("recycler_state");
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }


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


    public void getAllRecepies(){
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