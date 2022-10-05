package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import com.example.marvelapp.factory.response.DataWrapperResponseFactory
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.sarafaria.core.data.repository.CharactersRemoteDataSource
import com.sarafaria.core.domain.model.Character
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharactersPagingSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var dataSource: CharactersRemoteDataSource<DataWrapperResponse>

    private val dataWrapperResponseFactory = DataWrapperResponseFactory()

    private val characterFactory = CharacterFactory()

    private lateinit var pagingSource: CharactersPagingSource

    @Before
    fun setUp() {
        pagingSource = CharactersPagingSource(dataSource, "")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should a return success load result when load is called`() = runBlockingTest {
        // arrange
        whenever(dataSource.fetchCharacters(any())).thenReturn(
            dataWrapperResponseFactory.create()
        )

        // act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                null, loadSize = 2, placeholdersEnabled = false
            )
        )

        // assert
        val expected = listOf(characterFactory.create(CharacterFactory.Hero.ThreeDMan))

        assertEquals(
            PagingSource.LoadResult.Page(
                data = expected,
                prevKey = null,
                nextKey = 20
            ),
            result
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should return a error load result when load is called`() = runBlockingTest {
        // arrange
        val exception = RuntimeException()
        whenever(dataSource.fetchCharacters(any())).thenThrow(exception)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        // assert
        assertEquals(PagingSource.LoadResult.Error<Int, Character>(exception), result)
    }

}

