package com.ramusthastudio.roomtesting.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import com.ramusthastudio.roomtesting.model.User;
import com.ramusthastudio.roomtesting.repository.DatabaseService;

public final class UserDetailViewModel extends AndroidViewModel {
  private final DatabaseService fDatabaseService;

  UserDetailViewModel(
      final Application aApplication,
      final DatabaseService aDatabaseService) {
    super(aApplication);

    fDatabaseService = aDatabaseService;
  }

  public LiveData<User> loadAllById(final int aUid) {
    return fDatabaseService.loadAllById(aUid);
  }
}
