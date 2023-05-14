package pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.product;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.requests.products.Product;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView name;
    private TextView unit;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nazwaProduktuWlasne);
        unit = itemView.findViewById(R.id.jednostkiMiaryWlasne);
    }

    public void bind(Product product) {
        name.setText(product.getName());
        unit.setText(product.getUnit());
    }

    @Override
    public void onClick(View v) {
        // Handle click event for product item
        // Here you can start a new activity or show a dialog, etc.
    }
}
