package com.sarafaria.core

import androidx.paging.PagingConfig
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.pagingsource.PagingSourceFactory
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sarafaria.core.data.repository.CharactersRepository
import com.sarafaria.core.usecase.GetCharactersUseCase
import com.sarafaria.core.usecase.GetCharactersUseCaseImpl
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCharactersUseCaseImplTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var repository: CharactersRepository

    private lateinit var useCase: GetCharactersUseCase

    private val hero = CharacterFactory().create(CharacterFactory.Hero.ABomb)

    private val fakePagingSource = PagingSourceFactory().create(listOf(hero))

    @Before
    fun setUp() {
        useCase = GetCharactersUseCaseImpl(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate flo paging data creation when invoke from use case is called`() {
        runBlockingTest {
            whenever(repository.getCharacters("")).thenReturn(fakePagingSource)

            val result = useCase.invoke(GetCharactersUseCase.GetCharactersParams(
                "", PagingConfig(20))
            )

            verify(repository).getCharacters("")
            assertNotNull(result.first())
        }
    }
}