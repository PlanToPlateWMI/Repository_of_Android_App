/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.plantoplate.ui.main.settings.viewModels;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.models.UserInfo;
import pl.plantoplate.repository.remote.user.UserRepository;

public class SettingsViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    private SharedPreferences prefs;

    private MutableLiveData<String> success;
    private MutableLiveData<String> error;
    private MutableLiveData<UserInfo> userInfo;

    public SettingsViewModel(@NonNull Application application) {
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

    public MutableLiveData<UserInfo> getUserInfo() {
        if (userInfo == null) {
            userInfo = new MutableLiveData<>();
            fetchUserInfo();
        }
        return userInfo;
    }

    private void fetchUserInfo() {
        String token = "Bearer " + prefs.getString("token", "");

        userRepository.getUserInfo(token, new ResponseCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                userInfo.setValue(response);
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
