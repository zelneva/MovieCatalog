package dev.android.moviecatalog

import android.app.Application
import dev.android.moviecatalog.di.component.AppComponent
import dev.android.moviecatalog.di.component.DaggerAppComponent
import dev.android.moviecatalog.di.module.ApiModule
import dev.android.moviecatalog.di.module.AppModule
import dev.android.moviecatalog.di.module.ContextModule
import dev.android.moviecatalog.di.module.DbModule

class App : Application() {

    companion object {
        lateinit var instance: App private set
    }

    private lateinit var component: AppComponent


    override fun onCreate() {
        super.onCreate()
        instance = this
        setup()
    }


    private fun setup(){
        component = DaggerAppComponent
            .builder()
            .apiModule(ApiModule())
            .contextModule(ContextModule(this.applicationContext))
            .dbModule(DbModule())
            .build()
        component.inject(this)
    }

    fun getApplicationComponent(): AppComponent {
        return component
    }
}
