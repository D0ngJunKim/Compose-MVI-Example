package com.yeogibook.search.presentation.service.model

import com.yeogibook.search.presentation.service.datasource.SearchBookDataSource
import com.yeogibook.search.presentation.service.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object SearchModel {
    @Provides
    fun provideDataSource(): SearchBookDataSource {
        return SearchBookDataSource()
    }

    @Provides
    fun provideRepository(dataSource: SearchBookDataSource): SearchRepository {
        return SearchRepository(dataSource)
    }
}