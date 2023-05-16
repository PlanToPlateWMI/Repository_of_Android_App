package pl.plantoplate.ui.main.shoppingList.listAdapters.category;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.ui.main.shoppingList.listAdapters.product.ProductAdapter;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private TextView categoryOfProduct;
    private RecyclerView productRecyclerView;
    private int itemType;

    public CategoryViewHolder(View itemView, int itemType) {
        super(itemView);
        this.itemType = itemType;
        categoryOfProduct = itemView.findViewById(R.id.сategory_of_product);
        productRecyclerView = itemView.findViewById(R.id.product_recycler_view);

    }

    public void bind(Category category, OnProductItemClickListener listener) {

        categoryOfProduct.setText(category.getName());
        productRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        ProductAdapter productAdapter = new ProductAdapter(category.getProducts(), this.itemType);
        productAdapter.setOnProductItemClickListener(listener);
        productRecyclerView.setAdapter(productAdapter);
    }
}