package com.ramusthastudio.roomtesting.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import com.ramusthastudio.roomtesting.model.User;
import com.ramusthastudio.roomtesting.repository.DatabaseService;

public final class UserDetailViewModel extends AndroidViewModel {
  private final LiveData<User> fObserverUser;

  private UserDetailViewModel(@NonNull Application aApplication,
      final DatabaseService aDatabaseService, final int aUid) {
    super(aApplication);
    fObserverUser = aDatabaseService.loadAllById(aUid);
  }

  public static Factory createFactory(@NonNull Application aApplication, final int aUid) {
    return new Factory(aApplication, aUid);
  }

  public LiveData<User> getObserverUser() { return fObserverUser; }

  /**
   * A creator is used to inject the user UID into the ViewModel
   * <p>
   * This creator is to showcase how to inject dependencies into ViewModels. It's not
   * actually necessary in this case, as the user UID can be passed in a public method.
   */

  private static final class Factory extends ViewModelProvider.NewInstanceFactory {
    private final Application fApplication;
    private final int fUid;
    private final DatabaseService fDatabaseService;

    private Factory(@NonNull final Application aApplication, final int aUid) {
      fApplication = aApplication;
      fUid = aUid;
      fDatabaseService = ((com.ramusthastudio.roomtesting.Application) fApplication).databaseService();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
      return (T) new UserDetailViewModel(fApplication, fDatabaseService, fUid);
    }
  }
}
