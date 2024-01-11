package pl.plantoplate.ui.main.shopping_list.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * This class is used to notify the view that the list of products has changed.
 */
public class ShoppingListViewModel extends AndroidViewModel {

    /**
     * The list of products
     */
    private MutableLiveData<Integer> bougthProductsCount;
    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        bougthProductsCount = new MutableLiveData<>();
    }

    /**
     * Sets the bougth products count.
     * @param count the list type
     */
    public void setBougthProductsCount(int count) {
        bougthProductsCount.setValue(count);
    }

    /**
     * @return the list type
     */
    public MutableLiveData<Integer> getBougthProductsCount() {
        return bougthProductsCount;
    }

}
