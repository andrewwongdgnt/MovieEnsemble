package com.dgnt.movienensemble.featureMovie.domain.usecase

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class ValidateSearchQueryUseCaseTest {
    @InjectMockKs
    private lateinit var validateSearchQueryUseCase: ValidateSearchQueryUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test valid - non blank string`() {
        Assert.assertTrue(validateSearchQueryUseCase("something"))
    }

    @Test
    fun `test invalid - blank string`() {
        Assert.assertFalse(validateSearchQueryUseCase(" "))
    }

    @Test
    fun `test invalid - empty string`() {
        Assert.assertFalse(validateSearchQueryUseCase(""))
    }
}