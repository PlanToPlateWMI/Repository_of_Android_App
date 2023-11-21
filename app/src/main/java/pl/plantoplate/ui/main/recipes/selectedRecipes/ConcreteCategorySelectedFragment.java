package pl.plantoplate.ui.main.recipes.selectedRecipes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

public class ConcreteCategorySelectedFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private RecipeAdapter recipeCategoryAdapter;
    private FloatingActionButton floatingActionButton;
    private String category;
    private SharedPreferences prefs;
    private String webLink = "https://plantoplatewmi.github.io/WebPage/";

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

    public void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}