package pl.plantoplate.ui.main.shoppingList.listAdapters.category;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private ArrayList<Category> categories;
    private int itemType;
    private OnProductItemClickListener listener;

    public CategoryAdapter(ArrayList<Category> categories, int itemType) {
        this.categories = categories;
        this.itemType = itemType;
    }

    public void setOnProductItemClickListener(OnProductItemClickListener listener) {
        this.listener = listener;
    }

    // method for filtering our recyclerview items.
    @SuppressLint("NotifyDataSetChanged")
    public void setCategoriesList(ArrayList<Category> filterlist) {

        categories = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(itemView, this.itemType);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
