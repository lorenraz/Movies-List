package com.example.movieslist

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movieslist.database.MovieDao
import com.example.movieslist.database.MovieDatabase
import com.example.movieslist.viewmodel.MoviesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MoviesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        // Mock SharedPreferences and Editor
        `when`(mockContext.getSharedPreferences("moviesPref", Context.MODE_PRIVATE))
            .thenReturn(mockSharedPreferences)
        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)

        viewModel = MoviesViewModel()
    }

    @Test
    fun `test getItems when names is not empty`() = runTest {
        val mockNames = listOf("Movie1", "Movie2")
        viewModel.setNames(mockNames)

        viewModel.getItems(mockContext)

        assertEquals(mockNames, viewModel.namesLiveData.value)
    }

    @Test
    fun `test getItems when names is empty and exists in SharedPreferences`() = runTest {
        `when`(mockSharedPreferences.contains("names")).thenReturn(true)
        `when`(mockSharedPreferences.getString("names", "")).thenReturn("Movie1,Movie2,Movie3")

        viewModel.getItems(mockContext)

        assertEquals(listOf("Movie1","Movie2","Movie3"), viewModel.namesLiveData.value)
    }
}