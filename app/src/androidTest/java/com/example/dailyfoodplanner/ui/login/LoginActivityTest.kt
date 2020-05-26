package com.example.dailyfoodplanner.ui.login


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dailyfoodplanner.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(LoginActivity::class.java)
    }

    @Test
    fun emailFiledIsMandatory() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.etPasswordLogin)).perform(typeText("password"), closeSoftKeyboard())

        onView(withId(R.id.btnLogin)).perform(click())

        onView(withText("Email is mandatory")).check(matches(isDisplayed()))
    }

    @Test
    fun passwordFiledIsMandatory() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.etEmailLogin)).perform(typeText("test@test.com"), closeSoftKeyboard())

        onView(withId(R.id.btnLogin)).perform(click())

        onView(withText("Password is mandatory")).check(matches(isDisplayed()))
    }

    @Test
    fun loginSuccessful() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.etEmailLogin)).perform(typeText("test@test.com"), closeSoftKeyboard())

        onView(withId(R.id.etPasswordLogin)).perform(typeText("password"), closeSoftKeyboard())

        onView(withId(R.id.btnLogin)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.etDate)).check(matches(isDisplayed()))
    }
}
