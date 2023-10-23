package pl.plantoplate.ui.main.recyclerViews.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public class PantryProductsViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView unit;
    private final ImageView deleteProductButton;
    private final LinearLayout layout;

    public PantryProductsViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nazwaProduktu_spizarnia);
        unit = itemView.findViewById(R.id.jednostkiMiary_spizarnia);
        deleteProductButton = itemView.findViewById(R.id.iconDelete_spizarnia);
        layout = itemView.findViewById(R.id.item_produkt_spizarnia);
    }

    public void bind(Product product, SetupItemButtons listener) {
        name.setText(product.getName());
        String unitText = product.getAmount() + " " + product.getUnit().toLowerCase();
        unit.setText(unitText);
        listener.setupProductItemClick(layout, product);
        listener.setupDeleteProductButtonClick(deleteProductButton, product);
    }
}
