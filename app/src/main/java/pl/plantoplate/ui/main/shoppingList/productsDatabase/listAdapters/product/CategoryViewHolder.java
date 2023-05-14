package pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.product;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.category.Category;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private TextView categoryOfProduct;
    private RecyclerView productRecyclerView;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        categoryOfProduct = itemView.findViewById(R.id.сategory_of_product);
        productRecyclerView = itemView.findViewById(R.id.product_recycler_view);
    }

    public void bind(Category category) {

        categoryOfProduct.setText(category.getName());
        productRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        productRecyclerView.setAdapter(new ProductAdapter(category.getProducts()));
    }
}