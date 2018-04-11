package com.example.android.bakeme;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakeme.ui.DetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasEntry;

/**
 * This test demos a user clicking on an item in the {@link DetailActivity}.
 */
@RunWith(AndroidJUnit4.class)
public class DetailActivityTester {

    String shortDescription = "Recipe Introduction";
    String description = "Recipe Introduction";


    private RecipeIdlingResource idlingResource;

    @Rule
    private ActivityTestRule<DetailActivity> activityTestRule
            = new ActivityTestRule<>(DetailActivity.class);


    @Before
    public void registerIdlingResource() {

        idlingResource = activityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void idlingResourceTest() {
        //assertThat(activityTestRule.getActivityResult(),
                //hasEntry(onView(withId(R.id.recipe_text_tv)).atPosition(0)).matches(withText(shortDescription)));

// hasEntry: public static <K,V> Matcher<java.util.Map<? extends K,
// ? extends V>> hasEntry(Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher)
// Creates a matcher for Maps matching when the examined Map contains at least one entry whose key
// satisfies the specified keyMatcher and whose value satisfies the specified valueMatcher.
// For example: assertThat(myMap, hasEntry(equalTo("bar"), equalTo("foo")))
// Parameters: keyMatcher - the key matcher that, in combination with the valueMatcher, must be
// satisfied by at least one entry valueMatcher - the value matcher that, in combination with the
// keyMatcher, must be satisfied by at least one entry
        //http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/collection/IsMapContaining.html

        //onData(anything()).inAdapterView(withId(R.id.recipe_text_tv)).atPosition(0)
                //.perform(click()); //Click on first step of the recipe

        // check first step if right text is displayed.
        //onView(withId(R.id.recipe_step_tv)).check(matches(withText(description))));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource);

    }
}
