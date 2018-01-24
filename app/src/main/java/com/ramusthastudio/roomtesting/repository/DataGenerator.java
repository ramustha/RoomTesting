package com.ramusthastudio.roomtesting.repository;

import com.ramusthastudio.roomtesting.model.User;
import java.util.ArrayList;
import java.util.List;

public final class DataGenerator {
  private DataGenerator() {}

  public static List<User> generateUsers(final int aSize) {
    List<User> users = new ArrayList<>();
    for (int i = 0; i < aSize; i++) {
      User user = new User();
      user.setUid(i);
      user.setFirstName("first" + i);
      user.setLastName("last" + i);
      users.add(user);
    }
    return users;
  }
}
