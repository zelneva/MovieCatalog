package dev.android.moviecatalog.ui.main

import dev.android.moviecatalog.api.MovieApiService
import dev.android.moviecatalog.ui.main.MainContract.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainPresenter(var api: MovieApiService) : Presenter {

    private lateinit var view: MainContract.View
    private val subscriptions = CompositeDisposable()

    override fun loadData() {
            val subscribe = api.getListMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { view.showProgress(true)
                        if (it.totalResult != 0) {
                            view.showList(it.movies)
                        } else
                            view.showAlert()
                    },
                    { view.showError() },
                    { view.showProgress(false)
                    view.showProgressHorizontal(false)}
                )

            subscriptions.add(subscribe)
    }

    override fun loadDataByQuery(searchText: String) {
        val subscribe = api.getListMovieBySearch(query = searchText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { view.showProgressHorizontal(true)
                    if (it.totalResult != 0)
                        view.showList(it.movies)
                    else
                        view.showNotFound(searchText)
                },
                { view.showError() },
                { view.showProgressHorizontal(false)}
            )

        subscriptions.add(subscribe)
    }


    override fun unsubscribe() {
        subscriptions.clear()
    }


    override fun attach(view: MainContract.View) {
        this.view = view
    }
}
