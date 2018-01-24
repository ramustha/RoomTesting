package com.ramusthastudio.roomtesting;

import com.ramusthastudio.roomtesting.repository.AppDatabase;
import com.ramusthastudio.roomtesting.repository.DatabaseService;

public class Application extends android.app.Application {
  private AppExecutors fAppExecutors;

  @Override
  public void onCreate() {
    super.onCreate();

    fAppExecutors = new AppExecutors();
  }

  public DatabaseService databaseService() {
    return DatabaseService.getInstance(AppDatabase.getInstance(this, fAppExecutors));
  }
}
