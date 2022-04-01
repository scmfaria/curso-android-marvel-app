package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.network.response.toCharacterModel
import com.sarafaria.core.data.repository.CharactersRemoteDataSource
import com.sarafaria.core.domain.model.Character
import java.lang.Exception

class CharactersPagingSource(
    private val remoteDataSource: CharactersRemoteDataSource<DataWrapperResponse>,
    private val query: String
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            // a primeira vez que cair nessa função load, a variavel keyy estara null, por isso essa pequena validação
            val offset = params.key ?: 0
            val queries = hashMapOf("offset" to offset.toString())

            if(query.isNotEmpty()) {
                queries["nameStartsWith"] = query
            }

            val response = remoteDataSource.fetchCharacters(queries)

            val responseOffset = response.data.offset // valor do offset atual (retornado pela api)
            val totalCharacters = response.data.total // total de itens que existe (retornado pela api)

            // é esse objeto LoadResult<Int, Character> que essa função retorna
            LoadResult.Page(
                data = response.data.results.map { it.toCharacterModel() },
                prevKey = null,
                nextKey = if (responseOffset < totalCharacters) { // eu so irei obter mais paginas se o meu offset atual for menor que o total de itens
                    responseOffset + LIMIT
                } else null // quando retornamos null em nextKey, ele para de fazer requisições na api
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    // é chamado quando chamamos o SwipeToRefresh
    // ou quando o usuario mata a atividade do app e depois o usuario volta novamente, ele guarda a posição que o usuario estava anteriormente
    // ele retorna e recupera a posição que ele estava (parte do adapter)
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        // anchorPosition é a posição do adapter em que ele parou, que é recuperado a partir do estado retornado
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(LIMIT) ?: anchorPage?.nextKey?.minus(LIMIT)
        }
    }

    companion object {
        private const val LIMIT = 20
    }
}