package pl.plantoplate;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PlanToPlate extends Application {

    private static PlanToPlate instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if(instance == null){
            instance = this;
        }
    }

    public static PlanToPlate getInstance(){
        return instance;
    }

    public static boolean hasNetwork(){
        return instance.isNetworkConnected();
    }

    private boolean isNetworkConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
