package com.ramusthastudio.roomtesting.ui;

import android.arch.core.executor.testing.CountingTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import com.ramusthastudio.roomtesting.AppExecutors;
import com.ramusthastudio.roomtesting.MainActivity;
import com.ramusthastudio.roomtesting.R;
import com.ramusthastudio.roomtesting.repository.AppDatabase;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public final class MainActivityTest {
  @Rule
  public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

  @Rule
  public CountingTaskExecutorRule mCountingTaskExecutorRule = new CountingTaskExecutorRule();

  public MainActivityTest() {
    // delete the database
    InstrumentationRegistry.getTargetContext().deleteDatabase(AppDatabase.DATABASE_NAME);
  }

  @Before
  public void disableRecyclerViewAnimations() {
    // Disable RecyclerView animations
    EspressoTestUtil.disableAnimations(activityTestRule);
  }

  @Before
  public void waitForDbCreation() throws Throwable {
    final CountDownLatch latch = new CountDownLatch(1);
    final LiveData<Boolean> databaseCreated = AppDatabase.getInstance(
        InstrumentationRegistry.getTargetContext(), new AppExecutors()).getDatabaseCreated();
    activityTestRule.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        databaseCreated.observeForever(new Observer<Boolean>() {
          @Override
          public void onChanged(@Nullable Boolean aBoolean) {
            if (Boolean.TRUE.equals(aBoolean)) {
              databaseCreated.removeObserver(this);
              latch.countDown();
            }
          }
        });
      }
    });
    MatcherAssert.assertThat("database should've initialized",
        latch.await(1, TimeUnit.MINUTES), CoreMatchers.is(true));
  }

  @Test
  public void clickOnFirstItem_opensComments() throws Throwable {
    drain();
    // When clicking on the first product
    onView(ViewMatchers.withId(R.id.products_list))
        .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    drain();
    // Then the detail screen
    onView(ViewMatchers.withId(R.id.item_first_name))
        .check(matches(withText("first0")));
  }

  private void drain() throws TimeoutException, InterruptedException {
    mCountingTaskExecutorRule.drainTasks(1, TimeUnit.MINUTES);
  }
}
