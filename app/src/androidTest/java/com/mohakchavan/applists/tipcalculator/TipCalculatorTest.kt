package com.mohakchavan.applists.tipcalculator


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.mohakchavan.applists.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TipCalculatorTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(TipCalculator::class.java)

    @Test
    fun mainActivityTest() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.cost_of_service_edit_text),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cost_of_service),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("50"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.cost_of_service_edit_text), withText("50"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cost_of_service),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(pressImeActionButton())

        val materialButton = onView(
            allOf(
                withId(R.id.calculate_button), withText("Calculate"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        onView(withId(R.id.tip_result))
            .check(matches(withText(containsString("10.00"))))

    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
