package com.topzap.android.bakingtime;

import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import android.support.test.espresso.contrib.RecyclerViewActions;

import com.topzap.android.bakingtime.activities.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.anything;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityMenuScreenTest {

  private static final int ITEM_TO_CLICK = 0;

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

  @Test
  public void clickRecyclerViewItem_OpensRecipeActivity() throws Exception {
    // Context of the app under test.

    onView(withId(R.id.rv_main_activity))
        .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_TO_CLICK, click()));

    // If successful the text view for the ingredients should be present
    onView(withId(R.id.tv_ingredients_list))
        .check(exists());
  }

  public static ViewAssertion exists() {
    return matches(anything());
  }
}
