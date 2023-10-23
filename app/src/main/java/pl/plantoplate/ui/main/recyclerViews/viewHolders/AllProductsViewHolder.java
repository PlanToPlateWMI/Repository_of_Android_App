package pl.plantoplate.ui.main.recyclerViews.viewHolders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public class AllProductsViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView unit;
    private final AppCompatImageView addToShoppingListButton;
    private final LinearLayout layout;

    public AllProductsViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nazwaProduktuWszystkie);
        unit = itemView.findViewById(R.id.jednostkiMiaryWszsystkie);
        addToShoppingListButton = itemView.findViewById(R.id.addBazaWszystkie);
        layout = itemView.findViewById(R.id.layoutWszystkieProdukty);
    }

    public void bind(Product product, SetupItemButtons listener) {
        name.setText(product.getName());
        unit.setText(product.getUnit().toLowerCase());
        listener.setupAddToShoppingListButtonClick(addToShoppingListButton, product);
        listener.setupProductItemClick(layout, product);
    }
}
