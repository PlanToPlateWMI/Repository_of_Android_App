package pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.product;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.plantoplate.R;
import pl.plantoplate.requests.products.Product;

public class ProductAllAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private ArrayList<Product> products;

    public ProductAllAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductsList(ArrayList<Product> filterlist) {
        products = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wszystkie_produkt, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}