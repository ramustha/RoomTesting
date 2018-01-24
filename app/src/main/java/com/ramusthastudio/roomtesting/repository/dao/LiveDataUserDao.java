package com.ramusthastudio.roomtesting.repository.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.ramusthastudio.roomtesting.model.User;
import java.util.List;

@Dao
public interface LiveDataUserDao {
  @Query("SELECT count(*) FROM user")
  LiveData<Integer> count();

  @Query("SELECT * FROM user")
  LiveData<List<User>> getAll();

  @Query("SELECT * FROM user WHERE uid IN (:userIds)")
  LiveData<List<User>> loadAllByIds(int[] userIds);

  @Query("SELECT * FROM user WHERE uid = :userIds")
  LiveData<User> loadAllById(int userIds);

  @Query("SELECT * FROM user WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
  LiveData<User> findByName(String first, String last);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(User... users);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<User> users);

  @Delete
  void delete(User user);
}
