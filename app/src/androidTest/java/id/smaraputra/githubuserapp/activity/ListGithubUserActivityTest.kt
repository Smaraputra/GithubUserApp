package id.smaraputra.githubuserapp.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import id.smaraputra.githubuserapp.R
import org.junit.Before
import org.junit.Test

class ListGithubUserActivityTest{
    private val usernameFiller = "Smaraputra"
    private val usernameRandomFiller = "==<<"
    @Before
    fun setup(){
        ActivityScenario.launch(ListGithubUserActivity::class.java)
    }

    @Test
    fun assertRelevantUser() {
        onView(withId(R.id.searchUser)).perform(typeText(usernameFiller), closeSoftKeyboard())
        Thread.sleep(1000)
        onView(withId(R.id.rv_list_user_card)).check(matches(isDisplayed()))
        onView(withId(R.id.username_user)).check(matches(withText(usernameFiller)))
        onView(withId(R.id.rv_list_user_card)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.usernameDetail)).check(matches(isDisplayed()))
        onView(withId(R.id.usernameDetail)).check(matches(withText(usernameFiller)))
    }

    @Test
    fun assertUserNotFound() {
        onView(withId(R.id.searchUser)).perform(typeText(usernameRandomFiller), closeSoftKeyboard())
        Thread.sleep(1000)
        onView(withId(R.id.statusSearch)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_list_user_card)).check(doesNotExist())
    }
}