package com.example.marvelapp.framework.di

import com.sarafaria.core.usecase.GetCharactersUseCase
import com.sarafaria.core.usecase.GetCharactersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindCharactersUseCase(useCase: GetCharactersUseCaseImpl): GetCharactersUseCase
}