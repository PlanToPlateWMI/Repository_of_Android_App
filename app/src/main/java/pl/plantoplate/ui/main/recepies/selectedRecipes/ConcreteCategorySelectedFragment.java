package pl.plantoplate.ui.main.recepies.selectedRecipes;

import static android.content.Context.MODE_PRIVATE;
import android.content.SharedPreferences;
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
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.databinding.FragmentRecipeInsideNotAllBinding;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.recepies.recyclerViews.adapters.RecipeAdapter;
public class ConcreteCategorySelectedFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private RecipeAdapter recipeCategoryAdapter;
    private FloatingActionButton floatingActionButton;
    private String category;
    private SharedPreferences prefs;

    public ConcreteCategorySelectedFragment(String category) {
        this.category = category;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentRecipeInsideNotAllBinding fragmentRecipeInsideNotAllBinding =
                FragmentRecipeInsideNotAllBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();
        prefs = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE);

        floatingActionButton = fragmentRecipeInsideNotAllBinding.plusInKalendarz;

        setupRecyclerView(fragmentRecipeInsideNotAllBinding);
        getCategoryRecepies();
        return fragmentRecipeInsideNotAllBinding.getRoot();
    }


    public void setupRecyclerView(FragmentRecipeInsideNotAllBinding fragmentRecipeInsideNotAllBinding){
        RecyclerView recyclerView = fragmentRecipeInsideNotAllBinding.productsOwnRecyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recipeCategoryAdapter = new RecipeAdapter(new ArrayList<>());
        recyclerView.setAdapter(recipeCategoryAdapter);
    }

    public void getCategoryRecepies(){
        String token = "Bearer " + prefs.getString("token", "");
        RecipeRepository recipeRepository = new RecipeRepository();

        Disposable disposable = recipeRepository.getSelectedRecipes(category, token)
                .subscribe(
                        recipes -> recipeCategoryAdapter.setRecipesList(CategorySorter.sortRecipesByName(recipes)),
                        throwable -> Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show()
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}