package com.example.dailyfoodplanner.ui.notes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dailyfoodplanner.base.RxScheduleRule
import com.example.dailyfoodplanner.data.FirebaseRepositoryNotes
import com.example.dailyfoodplanner.model.Notes
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class NotesViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxScheduleRule = RxScheduleRule()

    @Mock
    lateinit var firebaseRepositoryNotes: FirebaseRepositoryNotes

    @InjectMocks
    lateinit var notesViewModel: NotesViewModel

    private lateinit var notesTest : Observable<List<Notes>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getAllNotes_success() {
        val note = Notes("noteId", "note")
        val notesList = arrayListOf<Notes>()
        notesList.add(note)

        notesTest = Observable.just(notesList.toList())

        Mockito.`when`(firebaseRepositoryNotes.getAllNotes()).thenReturn(notesTest)

        notesViewModel.getAllNotes()

        assertNotNull(notesViewModel.notesLiveData.value)
        assertEquals(note, notesViewModel.notesLiveData.value?.get(0))
        assertEquals(false, notesViewModel.notesLoading.value)
        assertEquals(false, notesViewModel.notesError.value)
    }

    @Test
    fun getAllNotes_failure() {
        notesTest = Observable.error(Throwable())

        Mockito.`when`(firebaseRepositoryNotes.getAllNotes()).thenReturn(notesTest)

        notesViewModel.getAllNotes()

        assertNull(notesViewModel.notesLiveData.value)
        assertEquals(true, notesViewModel.notesError.value)
        assertEquals(false, notesViewModel.notesLoading.value)
    }

    @Test
    fun addNote_success() {
        val note = Notes("noteId", "note")

        Mockito.`when`(firebaseRepositoryNotes.addNotes(note)).thenReturn(Observable.just(true))

        val result = notesViewModel.addNote(note)

        val testObserver = TestObserver<Boolean>()

        result.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertNoErrors()

        testObserver.assertValue(true)

//        val testResult = testObserver.values()[0]
//        assertThat(testResult, `is`(true))
    }

    @Test
    fun addNote_failure() {
        val note = Notes("noteId", "note")
//        val noteTest = Observable.error<Boolean>(Throwable())

        Mockito.`when`(firebaseRepositoryNotes.addNotes(note)).thenReturn(Observable.just(false))

        val result = notesViewModel.addNote(note)

        val testObserver = TestObserver<Boolean>()

        result.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertValue(false)
//        testObserver.assertComplete()
//        testObserver.assertError(Throwable())

//        val testResult = testObserver.values()[0]
//
//        assertThat(testResult, `is`(false))
    }
}