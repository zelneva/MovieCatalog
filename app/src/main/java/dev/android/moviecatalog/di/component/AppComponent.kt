package dev.android.moviecatalog.di.component

import android.content.Context
import dagger.Component
import dev.android.moviecatalog.App
import dev.android.moviecatalog.api.MovieApiService
import dev.android.moviecatalog.db.MovieDb
import dev.android.moviecatalog.di.module.ApiModule
import dev.android.moviecatalog.di.module.AppModule
import dev.android.moviecatalog.di.module.ContextModule
import dev.android.moviecatalog.di.module.DbModule
import javax.inject.Singleton

@Component(modules = [AppModule::class, ApiModule::class, ContextModule::class, DbModule::class])
@Singleton
interface AppComponent {
    val api: MovieApiService
    val context: Context
    val movieDb: MovieDb

    fun inject(app: App)
}
