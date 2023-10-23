package pl.plantoplate.ui.main.recyclerViews.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public class OwnProductsViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView unit;
    private AppCompatImageView addToShoppingListButton;
    private ImageView editProductButton;
    private LinearLayout layout;

    public OwnProductsViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nazwaProduktu_wlasny);
        unit = itemView.findViewById(R.id.jednostkiMiary_wlasny);
        addToShoppingListButton = itemView.findViewById(R.id.addBazaWlasne);
        editProductButton = itemView.findViewById(R.id.iconEdit_wlasny);
        layout = itemView.findViewById(R.id.layoutWlasneProdukty);
    }

    public void bind(Product product, SetupItemButtons listener) {
        name.setText(product.getName());
        unit.setText(product.getUnit().toLowerCase());
        listener.setupAddToShoppingListButtonClick(addToShoppingListButton, product);
        listener.setupEditProductButtonClick(editProductButton, product);
        listener.setupProductItemClick(layout, product);
    }
}
