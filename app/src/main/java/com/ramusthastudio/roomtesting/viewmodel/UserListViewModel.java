package com.ramusthastudio.roomtesting.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import com.ramusthastudio.roomtesting.model.User;
import java.util.List;

public final class UserListViewModel extends AndroidViewModel {
  // MediatorLiveData can observe other LiveData objects and react on their emissions.
  private final MediatorLiveData<List<User>> fObserverUsers;

  public UserListViewModel(final Application application) {
    super(application);

    fObserverUsers = new MediatorLiveData<>();
    fObserverUsers.setValue(null);

    LiveData<List<User>> users = ((com.ramusthastudio.roomtesting.Application) application)
        .databaseService().getUsers();

    // observe the changes of the products from the database and forward them
    fObserverUsers.addSource(users, fObserverUsers::setValue);
  }

  public MediatorLiveData<List<User>> getUsers() { return fObserverUsers; }
}
