package com.ramusthastudio.roomtesting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.ramusthastudio.roomtesting.model.User;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Add user list fragment if this is first creation
    if (savedInstanceState == null) {
      RecyclerListFragment fragment = new RecyclerListFragment();

      getSupportFragmentManager().beginTransaction()
          .add(R.id.fragment_container, fragment, RecyclerListFragment.TAG).commit();
    }
  }

  /** Shows the product detail fragment */
  public void show(User aUser) {
    UserDetailFragment detailFragment = UserDetailFragment.forUser(aUser.getUid());

    getSupportFragmentManager()
        .beginTransaction()
        .addToBackStack("user")
        .replace(R.id.fragment_container, detailFragment, "UserDetailFragment").commit();
  }
}
