package com.sarafaria.core.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sarafaria.core.data.repository.CharactersRepository
import com.sarafaria.core.domain.model.Character
import com.sarafaria.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val charactersRepository: CharactersRepository
) : PagingUseCase<GetCharactersUseCase.GetCharactersParams, Character>() {

    override fun createFlowObservable(params: GetCharactersParams): Flow<PagingData<Character>> {
        return Pager(config = params.pagingConfig) { // espera passar uma função como parametro
            charactersRepository.getCharacters(params.query)
        }.flow
    }

    data class GetCharactersParams(val query: String, val pagingConfig: PagingConfig)
}