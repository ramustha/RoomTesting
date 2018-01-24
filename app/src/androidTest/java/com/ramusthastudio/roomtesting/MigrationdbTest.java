package com.ramusthastudio.roomtesting;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.migration.Migration;
import android.arch.persistence.room.testing.MigrationTestHelper;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.ramusthastudio.roomtesting.repository.AppDatabase;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.ramusthastudio.roomtesting.repository.AppDatabase.MIGRATION_1_2;

@RunWith(AndroidJUnit4.class)
public class MigrationdbTest {
  @Rule
  public MigrationTestHelper helper;

  public MigrationdbTest() {
    helper = new MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase.class.getCanonicalName(),
        new FrameworkSQLiteOpenHelperFactory()
    );
  }

  @Test
  public void migrate1To2() throws IOException {
    SupportSQLiteDatabase db = helper.createDatabase(AppDatabase.DATABASE_NAME, 1);

    // db has schema version 1. insert some data using SQL queries.
    // You cannot use DAO classes because they expect the latest schema.
    db.execSQL("CREATE TABLE IF NOT EXISTS 'User' ('id' INTEGER, 'first_name' TEXT, 'last_name' TEXT, PRIMARY KEY('id'))");

    // Prepare for the next version.
    db.close();

    // Re-open the database with version 2 and provide
    // MIGRATION_1_2 as the migration process.
    db = helper.runMigrationsAndValidate(AppDatabase.DATABASE_NAME, 2, true, MIGRATION_1_2);

    // MigrationTestHelper automatically verifies the schema changes,
    // but you need to validate that the data was migrated properly.
    db.close();
  }
}
