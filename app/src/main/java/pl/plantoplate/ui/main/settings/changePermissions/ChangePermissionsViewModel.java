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

        userRepository.getUsersInfo(token, new ResponseCallback<ArrayList<UserInfo>>() {
            @Override
            public void onSuccess(ArrayList<UserInfo> response) {
                usersInfo.setValue(filterUsers(response, prefs.getString("email", "")));
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

    public void changePermissions(UserInfo userInfo) {
        String token = "Bearer " + prefs.getString("token", "");

        userRepository.changePermissions(token, userInfo, new ResponseCallback<ArrayList<UserInfo>>() {
            @Override
            public void onSuccess(ArrayList<UserInfo> response) {
                usersInfo.setValue(filterUsers(response, prefs.getString("email", "")));
                String role = "";
                if (userInfo.getRole().equals("USER")) {
                    role = "Użytkownik";
                } else if (userInfo.getRole().equals("ADMIN")) {
                    role = "Administrator";
                }
                success.setValue("Zmieniono uprawnienia użytkownika " + userInfo.getUsername() + " na " + role);
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

    public ArrayList<UserInfo> filterUsers(ArrayList<UserInfo> response, String email){
        // delete current user from list
        for (int i = 0; i < response.size(); i++) {
            if (response.get(i).getEmail().equals(email)) {
                response.remove(i);
                break;
            }
        }
        return response;
    }

}
