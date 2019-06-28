package dev.android.moviecatalog.di.module

import android.app.Activity
import dagger.Module
import dagger.Provides
import dev.android.moviecatalog.api.MovieApiService
import dev.android.moviecatalog.di.scope.MovieScope
import dev.android.moviecatalog.ui.main.MainContract
import dev.android.moviecatalog.ui.main.MainPresenter

@Module
class ActivityModule(private var activity: Activity) {

    @Provides
    @MovieScope
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    @MovieScope
    fun providePresenter(apiService: MovieApiService): MainContract.Presenter {
        return MainPresenter(apiService)
    }
}