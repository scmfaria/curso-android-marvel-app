package com.example.marvelapp.framework

import androidx.paging.PagingSource
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.paging.CharactersPagingSource
import com.sarafaria.core.data.repository.CharactersRemoteDataSource
import com.sarafaria.core.data.repository.CharactersRepository
import com.sarafaria.core.domain.model.Character
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharactersRemoteDataSource<DataWrapperResponse>
) : CharactersRepository {

    override fun getCharacters(query: String): PagingSource<Int, Character> {
        return CharactersPagingSource(remoteDataSource, query)
    }
}