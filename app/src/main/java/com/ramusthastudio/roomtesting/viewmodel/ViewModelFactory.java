package com.ramusthastudio.roomtesting.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import com.ramusthastudio.roomtesting.repository.DatabaseService;

public class ViewModelFactory implements ViewModelProvider.Factory {
  private final Application fApplication;
  private final DatabaseService fDatabaseService;

  public ViewModelFactory(
      final Application aApplication,
      final DatabaseService aDatabaseService) {

    fApplication = aApplication;
    fDatabaseService = aDatabaseService;
  }

  @SuppressWarnings("unchecked")
  @NonNull @Override
  public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
    if (modelClass.isAssignableFrom(UserListViewModel.class)) {
      return (T) new UserListViewModel(fApplication, fDatabaseService);
    } else if (modelClass.isAssignableFrom(UserDetailViewModel.class)) {
      return (T) new UserDetailViewModel(fApplication, fDatabaseService);
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
