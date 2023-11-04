package pl.plantoplate.ui.main.recepies.recyclerViews.viewHolders;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.category.Recipe;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import timber.log.Timber;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout recipeLinearLayout;
    private ImageView recipeImageView;
    private TextView recipeNameTextView;
    private TextView recipeTimeToCookTextView;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeLinearLayout = itemView.findViewById(R.id.layoutRecipeItem);
        recipeImageView = itemView.findViewById(R.id.iconUncheck_kupione);
        recipeNameTextView = itemView.findViewById(R.id.textView2);
        recipeTimeToCookTextView = itemView.findViewById(R.id.textView5);
    }

    public void bind(Recipe recipe, SetupItemButtons listener) {
        recipeLinearLayout.findViewById(R.id.wegeText).setVisibility(recipe.getVege() ? View.VISIBLE : View.GONE);
        if (TextUtils.isEmpty(recipe.getImage())) {
            Picasso.get()
                    .load(R.drawable.noimage)
                    .into(recipeImageView);
        } else {
            Picasso.get()
                    .load(recipe.getImage())
                    .error(R.drawable.noimage)
                    .into(recipeImageView);
        }
        recipeNameTextView.setText(recipe.getTitle());
        String cookTime = recipe.getTime() + " min";
        recipeTimeToCookTextView.setText(cookTime);
    }
}
