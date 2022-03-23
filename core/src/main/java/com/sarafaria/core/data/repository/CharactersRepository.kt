package com.sarafaria.core.data.repository

import androidx.paging.PagingSource
import com.sarafaria.core.domain.model.Character

interface CharactersRepository {

    fun getCharacters(query: String): PagingSource<Int, Character>
}