package com.dgnt.movienensemble.featureMovie.domain.usecase

import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class CanLoadMoreSearchPagesUseCaseTest {
    @InjectMockKs
    private lateinit var canLoadMoreSearchPagesUseCase: CanLoadMoreSearchPagesUseCase

    private val mockFirstPageSearchResult = SearchResult(
        movies = emptyList(),
        totalResults = 54,
        currentPage = 1,
        resultsPerPage = 10
    )

    private val mockAlmostLastPageSearchResult1 = SearchResult(
        movies = emptyList(),
        totalResults = 56, //To test rounding up
        currentPage = 5,
        resultsPerPage = 10
    )

    private val mockAlmostLastPageSearchResult2 = SearchResult(
        movies = emptyList(),
        totalResults = 51, //To test rounding up
        currentPage = 5,
        resultsPerPage = 10
    )
    private val mockLastPageSearchResult = SearchResult(
        movies = emptyList(),
        totalResults = 54,
        currentPage = 6,
        resultsPerPage = 10
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test load more - first page`() {
        Assert.assertTrue(canLoadMoreSearchPagesUseCase(mockFirstPageSearchResult))
    }

    @Test
    fun `test load more - almost last page`() {
        Assert.assertTrue(canLoadMoreSearchPagesUseCase(mockAlmostLastPageSearchResult1))
        Assert.assertTrue(canLoadMoreSearchPagesUseCase(mockAlmostLastPageSearchResult2))
    }

    @Test
    fun `test cannot load more - last page`() {
        Assert.assertFalse(canLoadMoreSearchPagesUseCase(mockLastPageSearchResult))
    }
}