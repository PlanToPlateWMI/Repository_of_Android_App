package pl.plantoplate.ui.main.shoppingList.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ShoppingListViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> bougthProductsCount;
    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        bougthProductsCount = new MutableLiveData<>();
    }

    public void setBougthProductsCount(int count) {
        bougthProductsCount.setValue(count);
    }

    public MutableLiveData<Integer> getBougthProductsCount() {
        return bougthProductsCount;
    }

}
