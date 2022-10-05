package com.example.marvelapp.presentation.characters

import androidx.paging.PagingData
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.sarafaria.core.usecase.GetCharactersUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class CharactersViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var useCase: GetCharactersUseCase

    private lateinit var viewModel: CharactersViewModel

    private val charactersFactory = CharacterFactory()

    private val pagingDataCharacters = PagingData.from(
        listOf(
            charactersFactory.create(CharacterFactory.Hero.ThreeDMan),
            charactersFactory.create(CharacterFactory.Hero.ABomb)
        )
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        viewModel = CharactersViewModel(useCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate the paging data object values when calling charactersPagingData`() = runBlockingTest {
        whenever(useCase.invoke(any())).thenReturn(
            flowOf(pagingDataCharacters)
        )

        val result = viewModel.charactersPagingData("")

        assertEquals(result.count(), 1)
    }

    @ExperimentalCoroutinesApi
    @Test(expected = RuntimeException::class)
    fun `should throw an exception when the calling to the use case return an exception`() =
        runBlockingTest {
            whenever(useCase.invoke(any())).thenThrow(RuntimeException())

            viewModel.charactersPagingData("")
        }

}