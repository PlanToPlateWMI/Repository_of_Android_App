package pl.plantoplate.ui.main.shoppingList.listAdapters.product;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pl.plantoplate.R;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;

public class ProductViewHolder extends RecyclerView.ViewHolder{
    private int itemType;
    private TextView name;
    private TextView unit;

    private FloatingActionButton addToShoppingListButton;
    private ImageView deleteProductButton;
    private ImageView editProductButton;
    private ImageView checkShoppingListButton;

    @SuppressLint("NonConstantResourceId")
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemType = itemView.getId();
        switch (this.itemType) {
            case R.id.layoutWszystkieProdukty:
                name = itemView.findViewById(R.id.nazwaProduktuWszystkie);
                unit = itemView.findViewById(R.id.jednostkiMiaryWszsystkie);
                addToShoppingListButton = itemView.findViewById(R.id.addBazaWszystkie);
                break;
            case R.id.layoutWlasneProdukty:
                name = itemView.findViewById(R.id.nazwaProduktu_wlasny);
                unit = itemView.findViewById(R.id.jednostkiMiary_wlasny);
                addToShoppingListButton = itemView.findViewById(R.id.addBazaWlasne);
                editProductButton = itemView.findViewById(R.id.iconEdit_wlasny);
                break;
            case R.id.layoutTrzebaKupicProdukty:
                name = itemView.findViewById(R.id.nazwaProduktu_trzebaKupic);
                unit = itemView.findViewById(R.id.jednostkiMiary_trzebaKupic);
                checkShoppingListButton = itemView.findViewById(R.id.iconCheckbox_trzebaKupic);
                deleteProductButton = itemView.findViewById(R.id.iconDelete_trzebaKupic);
                break;
            case R.id.layoutKupioneProdukty:
                name = itemView.findViewById(R.id.nazwaProduktu_kupione);
                unit = itemView.findViewById(R.id.jednostkiMiary_kupione);
                checkShoppingListButton = itemView.findViewById(R.id.iconUncheck_kupione);
                deleteProductButton = itemView.findViewById(R.id.iconDelete_kupione);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void bind(Product product, OnProductItemClickListener listener) {
        name.setText(product.getName());
        unit.setText(product.getUnit());
        switch(this.itemType) {
            case R.id.layoutWszystkieProdukty:
                addToShoppingListButton.setOnClickListener(v-> listener.onAddToShoppingListButtonClick(v, product));
                break;
            case R.id.layoutWlasneProdukty:
                addToShoppingListButton.setOnClickListener(v -> listener.onAddToShoppingListButtonClick(v, product));
                editProductButton.setOnClickListener(v -> listener.onEditProductButtonClick(v, product));
                break;
            case R.id.layoutTrzebaKupicProdukty:
            case R.id.layoutKupioneProdukty:
                checkShoppingListButton.setOnClickListener(v -> listener.onCheckShoppingListButtonClick(v, product));
                deleteProductButton.setOnClickListener(v -> listener.onDeleteProductButtonClick(v, product));
                break;
        }
    }
}
