package com.example.dailyfoodplanner.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dailyfoodplanner.base.RxScheduleRule
import com.example.dailyfoodplanner.model.Notes
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit


class FirebaseRepositoryNotesTest {

//    @get:Rule
//    val taskExecutorRule = InstantTaskExecutorRule()
//
//    @Rule @JvmField
//    val rxScheduleRule = RxScheduleRule()
//
//
//    private lateinit var mFirebaseRepositoryNotes: FirebaseRepositoryNotes
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.initMocks(this)
//        mFirebaseRepositoryNotes = `mock`(FirebaseRepositoryNotes::class.java)
//    }
//
//    @Test
//    fun writeNotes_success() {
//        val note = Notes("notesId", "note")
//
//        val result = mFirebaseRepositoryNotes.writeNotes(note)
//
//        val testObserver = TestObserver<Boolean>()
//
//        result.subscribe(testObserver)
//        testObserver.assertComplete()
//        testObserver.assertNoErrors()
//
//        val testResult = testObserver.values()[0]
//
//        assertThat(testResult, `is`(true))
//    }
}