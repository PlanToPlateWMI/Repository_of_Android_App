package pl.plantoplate.ui.main.recepies;

import android.os.Bundle;
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
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.databinding.FragmentRecipeInsideAllBinding;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.recepies.recyclerViews.adapters.RecipeCategoryAdapter;
import timber.log.Timber;

public class RecipeCategoriesFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private RecipeCategoryAdapter recipeCategoryAdapter;
    private FloatingActionButton floatingActionButton;

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

    public void setupRecyclerView(FragmentRecipeInsideAllBinding fragmentRecipeInsideAllBinding){
        RecyclerView recyclerView = fragmentRecipeInsideAllBinding.recipeRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeCategoryAdapter = new RecipeCategoryAdapter(new ArrayList<>());
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
//
//    public void setupRecyclerView(FragmentRecipeInsideAllBinding fragmentRecipeInsideAllBinding){
//        RecyclerView recyclerView = fragmentRecipeInsideAllBinding.recipeRecyclerView;
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new RecipeCategoryAdapter(new ArrayList<>()));
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        compositeDisposable.dispose();
//    }
}
