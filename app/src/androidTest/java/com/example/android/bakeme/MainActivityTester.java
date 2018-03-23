package com.example.android.bakeme;

import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import com.example.android.bakeme.ui.MainActivity;

import org.junit.runner.RunWith;

/**
 * This test demos a user clicking on an item in the {@link MainActivity}.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTester {

    public void clickRvItem_OpenDetailAcitivity() {
        onView(withId(R.id.recipe_overview_rv)).perform(actionOnItemAtPosition(0, click()));

        //onView(withId(R.id.detail_fragment_container1)).check(isDisplayed(R.layout.fragment_overview));


    }
}
