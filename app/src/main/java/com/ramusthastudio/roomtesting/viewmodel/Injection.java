package com.ramusthastudio.roomtesting.viewmodel;

import android.app.Application;
import com.ramusthastudio.roomtesting.repository.DatabaseService;

public final class Injection {
  private Injection() {}

  public static ViewModelFactory provideViewModelFactory(Application aApplication) {
    final DatabaseService database = ((com.ramusthastudio.roomtesting.Application) aApplication)
        .databaseService();
    return new ViewModelFactory(aApplication, database);
  }
}
