package dev.android.moviecatalog.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.android.moviecatalog.db.MovieDb
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideDb(context: Context): MovieDb {
        return MovieDb(context)
    }
}