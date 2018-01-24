package com.ramusthastudio.roomtesting;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.ramusthastudio.roomtesting.model.User;
import com.ramusthastudio.roomtesting.repository.AppDatabase;
import com.ramusthastudio.roomtesting.repository.DataGenerator;
import com.ramusthastudio.roomtesting.repository.dao.LiveDataUserDao;
import com.ramusthastudio.roomtesting.repository.dao.RxUserDao;
import com.ramusthastudio.roomtesting.repository.dao.UserDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.ramusthastudio.roomtesting.LiveDataTestUtil.getValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  private AppDatabase appDatabase;
  private UserDao userDao;
  private LiveDataUserDao liveDataUserDao;
  private RxUserDao rxUserDao;

  @Before
  public void initDb() throws Exception {
    Context context = InstrumentationRegistry.getContext();
    appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
        // allowing main thread queries, just for testing
        .allowMainThreadQueries()
        .build();

    userDao = appDatabase.userDao();
    liveDataUserDao = appDatabase.liveDataUserDao();
    rxUserDao = appDatabase.rxUserDao();
  }

  @After
  public void tearDown() throws Exception {
    appDatabase.close();
  }

  @Test
  public void writeUserAndReadTest() throws Exception {
    final User testUser = new User(1, "first1", "last1");

    userDao.insertAll(DataGenerator.generateUsers(1000));

    final User user1 = userDao.findByName("first1", "last1");
    assertThat(user1.getFirstName(), equalTo(testUser.getFirstName()));

    assertThat(userDao.count(), equalTo(1000));
  }

  @Test
  public void writeUserAndReadLiveDataTest() throws Exception {
    final User testUser = new User(1, "first1", "last1");

    liveDataUserDao.insertAll(DataGenerator.generateUsers(1000));
    final int count = getValue(liveDataUserDao.count());
    assertThat(count, equalTo(1000));

    final User user1 = getValue(liveDataUserDao.findByName("first1", "last1"));
    assertThat(user1.getFirstName(), equalTo(testUser.getFirstName()));
  }

  @Test
  public void deleteUserTest() throws Exception {
    userDao.insertAll(DataGenerator.generateUsers(1000));

    userDao.delete(new User(1, "first1", "last1"));
    final User user1 = userDao.findByName("first1", "last1");

    assertThat(user1, nullValue());
    assertThat(userDao.count(), equalTo(999));
  }

  // @Test
  // public void writeUserAndReadRxTest() throws Exception {
  //   final User testUser = new User(1, "first1", "last1");
  //
  //   rxUserDao.insertAll(usersDummy());
  //   final int count = getValue(rxUserDao.count());
  //   assertThat(count, equalTo(100));
  //
  //   final User user1 = getValue(rxUserDao.findByName("first1", "last1"));
  //   assertThat(user1.getFirstName(), equalTo(testUser.getFirstName()));
  // }
}
