package com.example.marvelapp.di

import com.example.marvelapp.BuildConfig
import com.example.marvelapp.framework.di.qualifiers.BaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BaseUrlTestModule {

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = "http://localhost:8080/"
}