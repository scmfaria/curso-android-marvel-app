package com.example.marvelapp.framework.di

import com.example.marvelapp.framework.CharactersRepositoryImpl
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.remote.RetrofitCharactersDataSource
import com.sarafaria.core.data.repository.CharactersRemoteDataSource
import com.sarafaria.core.data.repository.CharactersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // usa-se o Singleton somente se esse modulo for ser usado em mais de um caso de uso (mais de uma tela)
interface RepositoryModule {

    @Binds
    fun bindCharacterRepository(
        repository: CharactersRepositoryImpl
    ): CharactersRepository

    @Binds
    fun bindRemoteDataSource(
        dataSource: RetrofitCharactersDataSource
    ): CharactersRemoteDataSource<DataWrapperResponse>
}