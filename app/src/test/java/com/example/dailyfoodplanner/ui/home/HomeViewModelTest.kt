package com.example.dailyfoodplanner.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dailyfoodplanner.base.RxScheduleRule
import com.example.dailyfoodplanner.data.FirebaseRepositoryDailyPlaner
import com.example.dailyfoodplanner.data.FirebaseRepositoryRecipes
import com.example.dailyfoodplanner.model.DailyPlaner
import com.example.dailyfoodplanner.model.Recipes
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

class HomeViewModelTest{

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxScheduleRule = RxScheduleRule()

    @Mock
    lateinit var firebaseRepositoryRecipes: FirebaseRepositoryRecipes

    @Mock
    lateinit var firebaseRepositoryDailyPlaner: FirebaseRepositoryDailyPlaner

    @InjectMocks
    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getSingleDailyPlan_success() {
        val dailyPlan = DailyPlaner("id", "date", "time", "recipe",
            "time", "recipe", "time", "recipe",
            "time", "recipe", "time", "recipe")

        val dailyPlanTest = Observable.just(dailyPlan)

        `when`(firebaseRepositoryDailyPlaner.getSingleDailyPlan("id")).thenReturn(dailyPlanTest)

        homeViewModel.getSingleDailyPlan("id")

        assertThat(homeViewModel.dailyPlanLiveData.value, `is`(dailyPlan))
    }

    @Test
    fun getSingleDailyPlan_failure() {
        val dailyPlanTest = Observable.error<DailyPlaner>(Throwable())

        `when`(firebaseRepositoryDailyPlaner.getSingleDailyPlan("id")).thenReturn(dailyPlanTest)

        homeViewModel.getSingleDailyPlan("id")

        assertNull(homeViewModel.dailyPlanLiveData.value)
    }

    @Test
    fun getAllRecipes_success() {
        val recipe = Recipes("id", "title", "description", listOf("ingredients"))
        val recipeList = arrayListOf<Recipes>()
        recipeList.add(recipe)

        val recipeTest = Observable.just(recipeList.toList())

        `when`(firebaseRepositoryRecipes.getAllRecipes()).thenReturn(recipeTest)

        homeViewModel.getAllRecipes()

        assertEquals(recipeList, homeViewModel.recipeLiveData.value)
        assertEquals(false, homeViewModel.recipeLoading.value)
    }

    @Test
    fun getAllRecipes_failure() {
        val recipeTest = Observable.error<List<Recipes>>(Throwable())

        `when`(firebaseRepositoryRecipes.getAllRecipes()).thenReturn(recipeTest)

        homeViewModel.getAllRecipes()

        assertNull(homeViewModel.recipeLiveData.value)
        assertEquals(false, homeViewModel.recipeLoading.value)
    }

    @Test
    fun editDailyPlan_success() {
        val dailyPlan = DailyPlaner("id", "date", "time", "recipe",
            "time", "recipe", "time", "recipe",
            "time", "recipe", "time", "recipe")

        `when`(firebaseRepositoryDailyPlaner.editDailyPlan(dailyPlan)).thenReturn(Observable.just(true))

        val result = homeViewModel.editDailyPlan(dailyPlan)

        val testObserver = TestObserver<Boolean>()

        result.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue(true)
    }

    @Test
    fun addDailyPlaner_dailyPlanExists_falseReturned() {
        val dailyPlan = DailyPlaner("id", "date", "time", "recipe",
            "time", "recipe", "time", "recipe",
            "time", "recipe", "time", "recipe")
        val dailyPlanList = arrayListOf<DailyPlaner>()
        dailyPlanList.add(dailyPlan)

        val dailyPlanTest = Observable.just(dailyPlanList.toList())

        `when`(firebaseRepositoryDailyPlaner.getAllDailyPlans()).thenReturn(dailyPlanTest)

        val result = homeViewModel.addDailyPlaner(dailyPlan)

        val testObserver = TestObserver<Pair<Boolean, DailyPlaner?>>()
        result.subscribe(testObserver)

        testObserver.assertValue(Pair(false, dailyPlan))
        testObserver.assertNoErrors()
    }

    @Test
    fun addDailyPlaner_success() {
        val dailyPlan = DailyPlaner("id", "date", "time", "recipe",
            "time", "recipe", "time", "recipe",
            "time", "recipe", "time", "recipe")

        val dailyPlan1 = DailyPlaner("id1", "date1", "time1", "recipe1",
            "time1", "recipe1", "time1", "recipe1",
            "time1", "recipe1", "time1", "recipe1")
        val dailyPlanList = arrayListOf<DailyPlaner>()
        dailyPlanList.add(dailyPlan1)

        val dailyPlanTest = Observable.just(dailyPlanList.toList())

        //get all daily plans
        `when`(firebaseRepositoryDailyPlaner.getAllDailyPlans()).thenReturn(dailyPlanTest)
        //add daily plan
        `when`(firebaseRepositoryDailyPlaner.addDailyPlaner(dailyPlan)).thenReturn(Observable.just(
            Pair(true, dailyPlan)
        ))

        val result = homeViewModel.addDailyPlaner(dailyPlan)

        val testObserver = TestObserver<Pair<Boolean, DailyPlaner?>>()
        result.subscribe(testObserver)

        testObserver.assertValue(Pair(true, dailyPlan))
        testObserver.assertNoErrors()
    }

    @Test
    fun addDailyPlaner_failure() {
        val dailyPlan = DailyPlaner("id", "date", "time", "recipe",
            "time", "recipe", "time", "recipe",
            "time", "recipe", "time", "recipe")
        val dailyTest = Observable.error<Pair<Boolean, DailyPlaner?>>(Throwable())

        `when`(firebaseRepositoryDailyPlaner.addDailyPlaner(dailyPlan)).thenReturn(dailyTest)

        val result = homeViewModel.addDailyPlaner(dailyPlan)

        val testObserver = TestObserver<Pair<Boolean, DailyPlaner?>>()
        result.subscribe(testObserver)

        testObserver.assertNoValues()

    }
}