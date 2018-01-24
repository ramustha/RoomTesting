package com.ramusthastudio.roomtesting.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import com.ramusthastudio.roomtesting.AppExecutors;
import com.ramusthastudio.roomtesting.model.User;
import com.ramusthastudio.roomtesting.repository.dao.LiveDataUserDao;
import com.ramusthastudio.roomtesting.repository.dao.RxUserDao;
import com.ramusthastudio.roomtesting.repository.dao.UserDao;
import java.util.List;

@Database(entities = {User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
  @VisibleForTesting
  public static final String DATABASE_NAME = "userdb";
  private static AppDatabase sInstance;
  private final MutableLiveData<Boolean> fIsDatabaseCreated = new MutableLiveData<>();

  public abstract UserDao userDao();
  public abstract RxUserDao rxUserDao();
  public abstract LiveDataUserDao liveDataUserDao();

  public static AppDatabase getInstance(final Context aContext, final AppExecutors aExecutors) {
    if (sInstance == null) {
      synchronized (AppDatabase.class) {
        // sInstance = Room.databaseBuilder(aContext, AppDatabase.class, DATABASE_NAME).build();\
        sInstance = buildDatabase(aContext.getApplicationContext(), aExecutors);
        sInstance.updateDatabaseCreated(aContext.getApplicationContext());
      }
    }

    return sInstance;
  }

  public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
      database.execSQL("ALTER TABLE 'user' ADD 'role' TEXT");
    }
  };

  /**
   * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
   */
  private void updateDatabaseCreated(final Context context) {
    if (context.getDatabasePath(DATABASE_NAME).exists()) {
      setDatabaseCreated();
    }
  }

  private void setDatabaseCreated() {
    fIsDatabaseCreated.postValue(true);
  }

  public LiveData<Boolean> getDatabaseCreated() {
    return fIsDatabaseCreated;
  }

  private static AppDatabase buildDatabase(final Context aContext, final AppExecutors aExecutors) {
    return Room.databaseBuilder(aContext, AppDatabase.class, DATABASE_NAME)
        .addMigrations(MIGRATION_1_2)
        .addCallback(new Callback() {
          @Override public void onCreate(@NonNull final SupportSQLiteDatabase db) {
            super.onCreate(db);
            aExecutors.diskIO().execute(() -> {
              // Generate the data for pre-population
              AppDatabase database = AppDatabase.getInstance(aContext, aExecutors);
              List<User> products = DataGenerator.generateUsers(1000);
              final LiveDataUserDao userDao = database.liveDataUserDao();
              userDao.insertAll(products);

              // notify that the database was created and it's ready to be used
              database.setDatabaseCreated();
            });
          }
        })
        .build();
  }
}
