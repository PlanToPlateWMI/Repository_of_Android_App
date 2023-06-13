package pl.plantoplate.ui.main.settings.changePermissions;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.models.UserInfo;
import pl.plantoplate.repository.remote.user.UserRepository;

public class ChangePermissionsViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    private SharedPreferences prefs;

    private MutableLiveData<String> success;
    private MutableLiveData<String> error;
    private MutableLiveData<ArrayList<UserInfo>> usersInfo;

    public ChangePermissionsViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences("prefs", 0);
        userRepository = new UserRepository();
    }

    public MutableLiveData<String> getSuccess() {
        if (success == null) {
            success = new MutableLiveData<>();
        }
        return success;
    }

    public MutableLiveData<String> getError() {
        if (error == null) {
            error = new MutableLiveData<>();
        }
        return error;
    }

    public MutableLiveData<ArrayList<UserInfo>> getUsersInfo() {
        if (usersInfo == null) {
            usersInfo = new MutableLiveData<>();
            fetchUsersInfo();
        }
        return usersInfo;
    }

    private void fetchUsersInfo() {
        String token = "Bearer " + prefs.getString("token", "");
        String email = prefs.getString("email", "");
        System.out.println("email: " + email);

        userRepository.getUsersInfo(token, new ResponseCallback<ArrayList<UserInfo>>() {
            @Override
            public void onSuccess(ArrayList<UserInfo> response) {
                // delete current user from list
                for (int i = 0; i < response.size(); i++) {
                    if (response.get(i).getEmail().equals(email)) {
                        response.remove(i);
                        System.out.println("removed");
                        break;
                    }
                }
                usersInfo.setValue(response);
            }

            @Override
            public void onError(String errorMessage) {
                error.setValue(errorMessage);
            }

            @Override
            public void onFailure(String failureMessage) {
                error.setValue(failureMessage);
            }
        });
    }

}
