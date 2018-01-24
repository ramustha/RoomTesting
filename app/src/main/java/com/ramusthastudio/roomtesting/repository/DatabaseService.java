package com.ramusthastudio.roomtesting.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import com.ramusthastudio.roomtesting.model.User;
import com.ramusthastudio.roomtesting.repository.dao.LiveDataUserDao;
import com.ramusthastudio.roomtesting.repository.dao.RxUserDao;
import com.ramusthastudio.roomtesting.repository.dao.UserDao;
import java.util.List;

public final class DatabaseService {
  private static DatabaseService sInstance;
  private final AppDatabase fAppDatabase;
  private final MediatorLiveData<List<User>> fObserverUsers;

  private DatabaseService(final AppDatabase aAppDatabase) {
    fAppDatabase = aAppDatabase;
    fObserverUsers = new MediatorLiveData<>();

    fObserverUsers.addSource(fAppDatabase.liveDataUserDao().getAll(),
        users -> {
          if (aAppDatabase.getDatabaseCreated().getValue() != null) {
            fObserverUsers.postValue(users);
          }
        });
  }

  public static DatabaseService getInstance(final AppDatabase database) {
    if (sInstance == null) {
      synchronized (DatabaseService.class) {
        if (sInstance == null) {
          sInstance = new DatabaseService(database);
        }
      }
    }

    return sInstance;
  }

  public MediatorLiveData<List<User>> getUsers() { return fObserverUsers; }
  public UserDao userDao() { return fAppDatabase.userDao(); }
  public RxUserDao rxUserDao() { return fAppDatabase.rxUserDao(); }
  public LiveDataUserDao liveDataUserDao() { return fAppDatabase.liveDataUserDao(); }

  public LiveData<User> loadAllById(int aUid) {
    return liveDataUserDao().loadAllById(aUid);
  }

  public void close() { fAppDatabase.close(); }

}
