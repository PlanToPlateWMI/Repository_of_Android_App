package pl.plantoplate.ui.main.recyclerViews.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public class BuyProductsViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView unit;
    private final ImageView checkShoppingListButton;
    private final ImageView deleteProductButton;
    private final LinearLayout layout;

    public BuyProductsViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nazwaProduktu_trzebaKupic);
        unit = itemView.findViewById(R.id.jednostkiMiary_trzebaKupic);
        checkShoppingListButton = itemView.findViewById(R.id.iconCheckbox_trzebaKupic);
        deleteProductButton = itemView.findViewById(R.id.iconDelete_trzebaKupic);
        layout = itemView.findViewById(R.id.layoutTrzebaKupicProdukty);
    }

    public void bind(Product product, SetupItemButtons listener) {
        name.setText(product.getName());
        String unitText = product.getAmount() + " " + product.getUnit().toLowerCase();
        unit.setText(unitText);
        listener.setupCheckShoppingListButtonClick(checkShoppingListButton, product);
        listener.setupDeleteProductButtonClick(deleteProductButton, product);
        listener.setupProductItemClick(layout, product);
    }
}
