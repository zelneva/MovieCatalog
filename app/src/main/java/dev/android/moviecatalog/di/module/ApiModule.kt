package dev.android.moviecatalog.di.module

import dagger.Module
import dagger.Provides
import dev.android.moviecatalog.api.MovieApiService
import dev.android.moviecatalog.api.RetrofitClient
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideApiService(): MovieApiService {
        return RetrofitClient.getClient()
    }
}