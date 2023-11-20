package pl.plantoplate.ui.main.recipes.allRecipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.databinding.FragmentRecipeInsideNotAllBinding;
import pl.plantoplate.utils.CategorySorter;
import pl.plantoplate.ui.main.recipes.recipeInfo.RecipeInfoFragment;
import pl.plantoplate.ui.main.recipes.recyclerViews.adapters.RecipeAdapter;
import pl.plantoplate.ui.main.recipes.recyclerViews.listeners.SetupRecipeButtons;

public class ConcreteCategoryAllFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private RecipeAdapter recipeAdapter;
    private FloatingActionButton floatingActionButton;
    private String category;

    public ConcreteCategoryAllFragment(String category) {
        this.category = category;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentRecipeInsideNotAllBinding fragmentRecipeInsideNotAllBinding =
                FragmentRecipeInsideNotAllBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();

        floatingActionButton = fragmentRecipeInsideNotAllBinding.plusInKalendarz;
        floatingActionButton.setVisibility(View.INVISIBLE);

        setupRecyclerView(fragmentRecipeInsideNotAllBinding);
        getCategoryRecepies();
        return fragmentRecipeInsideNotAllBinding.getRoot();
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
        compositeDisposable.dispose();
    }

    public void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}