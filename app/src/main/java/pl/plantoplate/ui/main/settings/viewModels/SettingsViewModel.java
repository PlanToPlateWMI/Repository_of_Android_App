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
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.UserInfo;
import pl.plantoplate.data.remote.repository.UserRepository;

public class SettingsViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable;
    private final UserRepository userRepository;
    private final SharedPreferences prefs;
    private final MutableLiveData<String> responseMessage;
    private final MutableLiveData<UserInfo> userInfo;
    private final MutableLiveData<Integer> userCount;

    public SettingsViewModel(@NonNull Application application) {
        super(application);

        prefs = application.getSharedPreferences("prefs", 0);
        userRepository = new UserRepository();
        compositeDisposable = new CompositeDisposable();
        responseMessage = new MutableLiveData<>();
        userInfo = new MutableLiveData<>();
        userCount = new MutableLiveData<>();
    }

    public MutableLiveData<String> getResponseMessage() {
        return responseMessage;
    }

    public MutableLiveData<UserInfo> getUserInfo() {
        return userInfo;
    }

    public MutableLiveData<Integer> getUserCount() {
        return userCount;
    }

    public void fetchUserCount() {
        String token = "Bearer " + prefs.getString("token", "");
        Disposable disposable = userRepository.getUsersInfo(token)
                .subscribe(requestUsersInfo -> userCount.setValue(requestUsersInfo.size()),
                            throwable -> responseMessage.postValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    public void fetchUserInfo() {
        String token = "Bearer " + prefs.getString("token", "");
        Disposable disposable = userRepository.getUserInfo(token)
                .subscribe(userInfo::setValue,
                        throwable -> responseMessage.setValue(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}