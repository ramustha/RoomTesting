package com.ramusthastudio.roomtesting.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import com.ramusthastudio.roomtesting.model.User;
import com.ramusthastudio.roomtesting.repository.DatabaseService;
import java.util.List;

public final class UserListViewModel extends AndroidViewModel {
  private final MediatorLiveData<List<User>> fObserverUsers;

  UserListViewModel(
      final Application aApplication,
      final DatabaseService aDatabaseService) {
    super(aApplication);

    fObserverUsers = new MediatorLiveData<>();
    fObserverUsers.setValue(null);

    LiveData<List<User>> users = aDatabaseService.getUsers();
    fObserverUsers.addSource(users, fObserverUsers::setValue);
  }

  public MediatorLiveData<List<User>> getUsers() { return fObserverUsers; }
}
