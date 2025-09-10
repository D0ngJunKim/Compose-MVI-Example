package com.yeogibook.search.result.presentation.service.model

import com.yeogibook.search.result.presentation.service.SearchResultService
import com.yeogibook.search.result.presentation.service.repository.SearchResultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object SearchResultModel {
    @Provides
    fun provideRepository(apiService: SearchResultService): SearchResultRepository {
        return SearchResultRepository(apiService)
    }
}