package dev.android.moviecatalog.ui.main

import dev.android.moviecatalog.models.Movie
import dev.android.moviecatalog.ui.base.BaseContract

class MainContract {

    interface View : BaseContract.View {
        fun showList(list: List<Movie>)
        fun showError()
        fun showNotFound(query: String)
        fun showAlert()
        fun showProgress(show: Boolean)
        fun showProgressHorizontal(show: Boolean)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadData()
        fun loadDataByQuery(query: String)
    }
}