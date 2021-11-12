package com.example.submissionjetpackpro.ui.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.core.domain.model.Movie
import com.example.core.helper.EspressoIdlingResource
import com.example.submissionjetpackpro.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance()
            .register(EspressoIdlingResource.getEspressoIdlingResourceForMainActivity())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance()
            .unregister(EspressoIdlingResource.getEspressoIdlingResourceForMainActivity())
    }

    private val dummyMovie: List<Movie> = arrayListOf(
        Movie(
            id = 438631,
            "Venom: Let There Be Carnage",
            "2021-09-30",
            "After finding a host body in investigative reporter Eddie Brock, the alien symbiote must face a new enemy, Carnage, the alter ego of serial killer Cletus Kasady.",
            "/rjkmN1dniUHVYAtwuV3Tji7FsDO.jpg"
        ),
    )

    private val dummyTv: List<Movie> = arrayListOf(
        Movie(
            id = 90462,
            "Chucky",
            "2021-10-12",
            "After a vintage Chucky doll turns up at a suburban yard sale, an idyllic American town is thrown into chaos as a series of horrifying murders begin to expose the town’s hypocrisies and secrets. Meanwhile, the arrival of enemies — and allies — from Chucky’s past threatens to expose the truth behind the killings, as well as the demon doll’s untold origins.",
            "/iF8ai2QLNiHV4anwY1TuSGZXqfN.jpg"
        ),
    )

    @Test
    fun loadDetailFilm() {
        onView(withId(R.id.rv_film))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_film)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.title_film))
            .check(matches(withText(dummyMovie[0].title)))
        onView(withId(R.id.detailFilm))
            .check(matches(withText(dummyMovie[0].releaseDate)))
        onView(withId(R.id.text_sinopsis))
            .check(matches(withText(dummyMovie[0].synopsis)))

        onView(withId(R.id.rv_casts)).check(matches(isDisplayed()))
    }

    @Test
    fun loadDetailTv() {
        onView(withId(R.id.navigation_tv)).perform(click())
        onView(withId(R.id.rv_tv))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_tv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.title_film))
            .check(matches(withText(dummyTv[0].title)))
        onView(withId(R.id.detailFilm))
            .check(matches(withText(dummyTv[0].releaseDate)))
        onView(withId(R.id.text_sinopsis))
            .check(matches(withText(dummyTv[0].synopsis)))

        onView(withId(R.id.rv_casts)).check(matches(isDisplayed()))
    }

    @Test
    fun loadFavoriteDetailFilm() {
        onView(withId(R.id.rv_film))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_film)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.fav_button)).perform(click())
        onView(isRoot()).perform(pressBack())

        onView(withId(R.id.navigation_favorite)).perform(click())
        onView(withId(R.id.rv_favorite)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.title_film))
            .check(matches(withText(dummyMovie[0].title)))
        onView(withId(R.id.detailFilm))
            .check(matches(withText(dummyMovie[0].releaseDate)))
        onView(withId(R.id.text_sinopsis))
            .check(matches(withText(dummyMovie[0].synopsis)))

        onView(withId(R.id.rv_casts)).check(matches(isDisplayed()))
    }

    @Test
    fun loadFavoriteDetailTv() {
        onView(withId(R.id.navigation_tv)).perform(click())
        onView(withId(R.id.rv_tv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.fav_button)).perform(click())
        onView(isRoot()).perform(pressBack())

        onView(withId(R.id.navigation_favorite)).perform(click())
        onView(withText("TV")).perform(click())
        onView(withId(R.id.rv_favorite)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.title_film))
            .check(matches(withText(dummyTv[0].title)))
        onView(withId(R.id.detailFilm))
            .check(matches(withText(dummyTv[0].releaseDate)))
        onView(withId(R.id.text_sinopsis))
            .check(matches(withText(dummyTv[0].synopsis)))

        onView(withId(R.id.rv_casts)).check(matches(isDisplayed()))
    }
}