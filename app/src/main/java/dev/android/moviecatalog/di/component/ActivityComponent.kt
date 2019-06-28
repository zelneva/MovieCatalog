package dev.android.moviecatalog.di.component

import dagger.Component
import dev.android.moviecatalog.di.module.ActivityModule
import dev.android.moviecatalog.di.scope.MovieScope
import dev.android.moviecatalog.ui.main.MainActivity


@Component(dependencies = [AppComponent::class],modules = [ActivityModule::class])
@MovieScope
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)
}
