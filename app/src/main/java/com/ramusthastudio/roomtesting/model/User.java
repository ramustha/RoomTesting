package com.ramusthastudio.roomtesting.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {
  @PrimaryKey
  private int uid;

  @ColumnInfo(name = "first_name")
  private String firstName;

  @ColumnInfo(name = "last_name")
  private String lastName;

  @ColumnInfo(name = "role")
  private String role;

  public User() {
  }

  public User(final int aUid, final String aFirstName, final String aLastName) {
    uid = aUid;
    firstName = aFirstName;
    lastName = aLastName;
  }

  public int getUid() { return uid; }
  public String getFirstName() { return firstName; }
  public String getLastName() { return lastName; }
  public String getRole() { return role; }

  public void setUid(final int aUid) {
    uid = aUid;
  }

  public void setFirstName(final String aFirstName) {
    firstName = aFirstName;
  }

  public void setLastName(final String aLastName) {
    lastName = aLastName;
  }

  public void setRole(final String aRole) {
    role = aRole;
  }

  @Override
  public String toString() {
    return "User{" +
        "uid=" + uid +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", role='" + role + '\'' +
        '}';
  }
}
