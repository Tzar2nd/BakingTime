package com.topzap.android.bakingtime;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import com.topzap.android.bakingtime.activities.MainActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SummaryActivityIntentTest {

  private final static String RECIPE_NAME = "Cheesecake - Servings: 8";
  private IdlingResource mIdlingResource;
  private final static int SCROLL_POSITION = 3;
  private final static int RECIPE_STEP_LIST_ITEM = 12;
  private final static String STEP_NAME = "12: Final cooling and set.";

  @Rule
  public IntentsTestRule<MainActivity> mActivityTestRule = new IntentsTestRule<>(
      MainActivity.class);

  @Before
  public void registerIdlingResource() {
    mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
    IdlingRegistry.getInstance().register(mIdlingResource);
  }

  @Test
  public void testRecipeNameIsAtPosition() {

    // Scroll to the second item (item 1)
    onView(withId(R.id.rv_main_activity))
        .perform(RecyclerViewActions
            .scrollToPosition(SCROLL_POSITION));

    // Check that 'Brownies' recipe is in the second position as expected
    onView(withText(RECIPE_NAME))
        .check(matches(isDisplayed()));
  }

  @Test
  public void testMainToListToStepNavigation() {

    // Click fourth item (cheesecake)
    onView(withId(R.id.rv_main_activity))
        .perform(RecyclerViewActions
            .actionOnItemAtPosition(SCROLL_POSITION, click()));

    // Check if ingredients text view is displayed
    onView(withId(R.id.tv_ingredients_list))
        .check(matches(isDisplayed()));

    // Check that the step name "12: Final cooling and set." is the one chosen
    onData(anything())
        .inAdapterView(withId(R.id.lv_recipe_steps))
        .atPosition(RECIPE_STEP_LIST_ITEM)
        .check(matches(withText(STEP_NAME)));

    // Click this item
    onData(anything()).inAdapterView(withId(R.id.lv_recipe_steps)).atPosition(RECIPE_STEP_LIST_ITEM).perform(click());

    // Check if the player is visible and the correct step is showing. If so the test is a success.
    onView(withId(R.id.tv_recipe_step_description)).check(matches(isDisplayed()));
  }

  @After
  public void unregisterIdlingResource() {
    if (mIdlingResource != null) {
      IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
  }

}
