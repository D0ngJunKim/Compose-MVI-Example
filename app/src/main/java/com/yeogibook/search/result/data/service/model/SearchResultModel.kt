package com.yeogibook.search.result.data.service.model

import com.yeogibook.search.result.data.service.datasource.SearchResultDataSource
import com.yeogibook.search.result.data.service.repository.SearchResultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object SearchResultModel {
    @Provides
    fun provideDataSource(): SearchResultDataSource {
        return SearchResultDataSource()
    }

    @Provides
    fun provideRepository(dataSource: SearchResultDataSource): SearchResultRepository {
        return SearchResultRepository(dataSource)
    }
}