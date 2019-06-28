package dev.android.moviecatalog.ui.base

class BaseContract {

    interface Presenter<in T> {
        fun attach(view: T)
        fun unsubscribe()
    }

    interface View {
    }
}