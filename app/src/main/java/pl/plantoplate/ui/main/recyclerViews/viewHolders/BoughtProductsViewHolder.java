package pl.plantoplate.ui.main.recyclerViews.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public class BoughtProductsViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView unit;
    private final ImageView checkShoppingListButton;
    private final ImageView deleteProductButton;

    public BoughtProductsViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nazwaProduktu_kupione);
        unit = itemView.findViewById(R.id.jednostkiMiary_kupione);
        checkShoppingListButton = itemView.findViewById(R.id.iconUncheck_kupione);
        deleteProductButton = itemView.findViewById(R.id.iconDelete_kupione);
    }

    public void bind(Product product, SetupItemButtons listener) {
        name.setText(product.getName());
        String unitText = product.getAmount() + " " + product.getUnit().toLowerCase();
        unit.setText(unitText);
        listener.setupCheckShoppingListButtonClick(checkShoppingListButton, product);
        listener.setupDeleteProductButtonClick(deleteProductButton, product);
    }
}
