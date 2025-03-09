package com.dgnt.movienensemble.featureMovie.presentation

import com.dgnt.movienensemble.R
import com.dgnt.movienensemble.core.util.Resource
import com.dgnt.movienensemble.featureMovie.domain.model.Movie
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import com.dgnt.movienensemble.featureMovie.domain.usecase.CanLoadMoreSearchResultsUseCase
import com.dgnt.movienensemble.featureMovie.domain.usecase.SearchMovieUseCase
import com.dgnt.movienensemble.featureMovie.domain.usecase.ValidateSearchQueryUseCase
import io.mockk.MockKAnnotations
import io.mockk.called
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK(relaxed = true)
    private lateinit var searchMovieUseCase: SearchMovieUseCase

    @MockK(relaxed = true)
    private lateinit var canLoadMoreSearchResultsUseCase: CanLoadMoreSearchResultsUseCase

    @MockK(relaxed = true)
    private lateinit var validateSearchQueryUseCase: ValidateSearchQueryUseCase

    @InjectMockKs
    private lateinit var movieListVM: MovieListViewModel

    private val mockSearchResult = SearchResult(
        movies = listOf(
            Movie(
                title = "Star Wars",
                year = "2004",
                poster = "https://m.media-amazon.com/images/M/MV5BNWEwOTI0MmUtMGNmNy00ODViLTlkZDQtZTg1YmQ3MDgyNTUzXkEyXkFqcGc@._V1_SX300.jpg",
                imdbID = "123",
                type = "Movie"
            ),
            Movie(
                title = "Star Wars 2",
                year = "2005",
                poster = "https://m.media-amazon.com/images/M/MV5BMTkxNGFlNDktZmJkNC00MDdhLTg0MTEtZjZiYWI3MGE5NWIwXkEyXkFqcGc@._V1_SX300.jpg",
                imdbID = "123",
                type = "Movie"
            )
        ),
        totalResults = 4,
        currentPage = 1
    )

    private val mockSearchResult2 = SearchResult(
        movies = listOf(
            Movie(
                title = "Star Trek",
                year = "2014",
                poster = "https://m.media-amazon.com/images/M/MV5BNWEwOTI0MmUtMGNmNy00ODViLTlkZDQtZTg1YmQ3MDgyNTUzXkEyXkFqcGc@._V1_SX300.jpg",
                imdbID = "123",
                type = "Movie"
            ),
            Movie(
                title = "Star Trek 2",
                year = "2015",
                poster = "https://m.media-amazon.com/images/M/MV5BMTkxNGFlNDktZmJkNC00MDdhLTg0MTEtZjZiYWI3MGE5NWIwXkEyXkFqcGc@._V1_SX300.jpg",
                imdbID = "123",
                type = "Movie"
            )
        ),
        totalResults = 4,
        currentPage = 2
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `test state - initial`() = runTest {
        Assert.assertEquals(MovieListState.Empty(""), movieListVM.state.value)
    }

    @Test
    fun `test search - search is called`() = runTest {
        every { validateSearchQueryUseCase("") } returns true

        movieListVM.onAction(MovieListAction.Search(""))
        advanceUntilIdle()
        verify(exactly = 1) {
            searchMovieUseCase("", 1)
        }
    }

    @Test
    fun `test search - search is not called`() = runTest {
        every { validateSearchQueryUseCase("") } returns false

        movieListVM.onAction(MovieListAction.Search(""))
        advanceUntilIdle()
        verify {
            searchMovieUseCase wasNot called
        }
    }

    @Test
    fun `test search - assert loading state`() = runTest {
        every { validateSearchQueryUseCase("Something") } returns true
        every { searchMovieUseCase("Something", 1) } returns flow {
            emit(Resource.Loading())
        }

        movieListVM.onAction(MovieListAction.Search("Something"))
        advanceUntilIdle()

        Assert.assertEquals(MovieListState.Loading("Something"), movieListVM.state.value)

    }

    @Test
    fun `test search - assert http error state`() = runTest {
        every { validateSearchQueryUseCase("Something") } returns true
        every { searchMovieUseCase("Something", 1) } returns flow {
            emit(Resource.Error.HttpError())
        }

        movieListVM.onAction(MovieListAction.Search("Something"))
        advanceUntilIdle()

        Assert.assertEquals(MovieListState.Empty("Something", R.string.serverError), movieListVM.state.value)

    }

    @Test
    fun `test search - assert io error state`() = runTest {
        every { validateSearchQueryUseCase("Something") } returns true
        every { searchMovieUseCase("Something", 1) } returns flow {
            emit(Resource.Error.IOError())
        }

        movieListVM.onAction(MovieListAction.Search("Something"))
        advanceUntilIdle()

        Assert.assertEquals(MovieListState.Empty("Something", R.string.genericError), movieListVM.state.value)

    }

    @Test
    fun `test search - assert search result`() = runTest {
        every { validateSearchQueryUseCase("Something") } returns true
        every { searchMovieUseCase("Something", 1) } returns flow {
            emit(Resource.Success(mockSearchResult))
        }

        movieListVM.onAction(MovieListAction.Search("Something"))
        advanceUntilIdle()

        Assert.assertEquals(MovieListState.Result("Something", mockSearchResult), movieListVM.state.value)

    }

    @Test
    fun `test search and load more - assert search result with loading more`() = runTest {
        every { validateSearchQueryUseCase("Something") } returns true
        every { canLoadMoreSearchResultsUseCase(mockSearchResult) } returns true
        every { searchMovieUseCase("Something", 1) } returns flow {
            emit(Resource.Success(mockSearchResult))
        }
        every { searchMovieUseCase("Something", 2) } returns flow {
            emit(Resource.Loading(mockSearchResult))
        }

        movieListVM.onAction(MovieListAction.Search("Something"))
        advanceUntilIdle()
        movieListVM.onAction(MovieListAction.LoadMore)

        Assert.assertEquals(MovieListState.Result("Something", mockSearchResult, true), movieListVM.state.value)

    }

    @Test
    fun `test search and load more - assert when load more is not possible that search is not called`() = runTest {
        every { validateSearchQueryUseCase("Something") } returns true
        every { canLoadMoreSearchResultsUseCase(mockSearchResult) } returns false
        every { searchMovieUseCase("Something", 1) } returns flow {
            emit(Resource.Success(mockSearchResult))
        }

        movieListVM.onAction(MovieListAction.Search("Something"))
        advanceUntilIdle()
        clearMocks(validateSearchQueryUseCase, searchMovieUseCase)
        movieListVM.onAction(MovieListAction.LoadMore)

        verify(exactly = 0) {
            validateSearchQueryUseCase("Something")
        }
        verify(exactly = 0) {
            searchMovieUseCase("Something", 1)
        }
    }

    @Test
    fun `test search and load more - assert 2 search results added together`() = runTest {
        every { validateSearchQueryUseCase("Something") } returns true
        every { canLoadMoreSearchResultsUseCase(mockSearchResult) } returns true
        every { searchMovieUseCase("Something", 1) } returns flow {
            emit(Resource.Success(mockSearchResult))
        }
        every { searchMovieUseCase("Something", 2) } returns flow {
            emit(Resource.Success(mockSearchResult2))
        }

        movieListVM.onAction(MovieListAction.Search("Something"))
        advanceUntilIdle()
        movieListVM.onAction(MovieListAction.LoadMore)

        Assert.assertEquals(
            MovieListState.Result(
                "Something",
                SearchResult(
                    movies = mockSearchResult.movies + mockSearchResult2.movies,
                    totalResults = mockSearchResult2.totalResults,
                    currentPage = mockSearchResult2.currentPage

                )
            ), movieListVM.state.value
        )

    }


}