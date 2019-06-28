package dev.android.moviecatalog.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import dev.android.moviecatalog.App
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    fun provideApp(): Application {
        return app
    }
}
