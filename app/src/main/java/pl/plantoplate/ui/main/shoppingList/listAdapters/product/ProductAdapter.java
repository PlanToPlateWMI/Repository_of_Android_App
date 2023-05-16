package pl.plantoplate.ui.main.shoppingList.listAdapters.product;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.plantoplate.requests.products.Product;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private ArrayList<Product> products;
    private int itemType;

    private OnProductItemClickListener listener;

    public ProductAdapter(ArrayList<Product> products, int itemType) {
        this.products = products;
        this.itemType = itemType;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductsList(ArrayList<Product> filterlist) {
        products = filterlist;
        notifyDataSetChanged();
    }

    public void setOnProductItemClickListener(OnProductItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(this.itemType, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}