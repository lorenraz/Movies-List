package com.example.movieslist

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movieslist.database.MovieDatabase
import com.example.movieslist.repository.MoviesRepository
import com.example.movieslist.utils.Constants
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class MoviesRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: MoviesRepository
    private lateinit var mockDatabase: MovieDatabase
    private lateinit var mockContext: Context
    private lateinit var mockSharedPreferences: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var mockFirebaseDatabase: FirebaseDatabase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDatabase = mock(MovieDatabase::class.java)
        mockContext = mock(Context::class.java)
        mockSharedPreferences = mock(SharedPreferences::class.java)
        mockEditor = mock(SharedPreferences.Editor::class.java)
        mockFirebaseDatabase = mock(FirebaseDatabase::class.java)

        `when`(mockContext.getSharedPreferences(Constants.MOVIES_PREF, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor)

        repository = MoviesRepository(mockDatabase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getItems when names is not empty`() = runTest {
        val testNames = listOf("Name1", "Name2")
        repository.setNames(testNames)

        val liveData = repository.getItems(mockContext)

        Assert.assertEquals(testNames, liveData.value)
    }

    @Test
    fun `test getItems when names is empty and exists in SharedPreferences`() = runTest {
        val testNamesString = "Name1,Name2"
        val testNames = testNamesString.split(",")
        `when`(mockSharedPreferences.contains(Constants.PREF_NAMES_KEY)).thenReturn(true)
        `when`(mockSharedPreferences.getString(Constants.PREF_NAMES_KEY, "")).thenReturn(testNamesString)

        val liveData = repository.getItems(mockContext)

        Assert.assertEquals(testNames, liveData.value)
    }
}