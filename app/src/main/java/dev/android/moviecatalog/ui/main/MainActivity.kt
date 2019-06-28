package dev.android.moviecatalog.ui.main

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import dev.android.moviecatalog.App
import dev.android.moviecatalog.R
import dev.android.moviecatalog.db.MovieDb
import dev.android.moviecatalog.di.component.DaggerActivityComponent
import dev.android.moviecatalog.di.module.ActivityModule
import dev.android.moviecatalog.models.Movie
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainContract.View {

    private val POSITION = "POSITION"
    private val MOVIE_LIST = "MOVIE LIST"

    @Inject
    lateinit var presenter: MainContract.Presenter

    @Inject
    lateinit var movieDb: MovieDb

    private lateinit var progressBar: ProgressBar
    private lateinit var progressHorizontal: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchText: EditText
    private lateinit var adapter: MainAdapter

    private lateinit var movieListLayout: SwipeRefreshLayout
    private lateinit var notFoundLayout: LinearLayout
    private lateinit var alertLayout: LinearLayout

    private var positionIndex: Int = -1
    private var movieList = arrayListOf<Movie>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependency()
        initView()

        if (savedInstanceState != null) {
            positionIndex = savedInstanceState.getInt(POSITION)
            movieList = savedInstanceState.getParcelableArrayList<Parcelable>(MOVIE_LIST) as ArrayList<Movie>
            if (positionIndex != -1) {
                (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(positionIndex)
            }
        }

        initActionBar()
        attachPresenter()
        refresh()
    }


    private fun initActionBar() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar)
        val toolbar = supportActionBar?.customView?.parent as Toolbar?
        toolbar?.setPadding(0,0,0,0)
        toolbar?.setContentInsetsAbsolute(0,0)
        searchText = findViewById(R.id.search_text)
        searchText.setOnEditorActionListener(searchActionListener)
    }


    private val searchActionListener = TextView.OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (v.text.isNotEmpty()) {
                showProgressHorizontal(true)
                loadDataByQuery(v.text.toString())
            } else {
                showProgressHorizontal(true)
                loadData()
            }
            hideKeyboard()
            true
        } else {
            false
        }
    }


    private fun hideKeyboard() {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }


    private fun attachPresenter() {
        if (lastCustomNonConfigurationInstance != null) {
            presenter = lastCustomNonConfigurationInstance as MainContract.Presenter
            presenter.attach(this)
            show()
        } else {
            presenter.attach(this)
            loadData()
        }
    }


    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent
            .builder()
            .activityModule(ActivityModule(this))
            .appComponent(App.instance.getApplicationComponent())
            .build()

        activityComponent.inject(this)
    }


    private fun initView() {
        movieListLayout = findViewById(R.id.movie_list)
        notFoundLayout = findViewById(R.id.not_found)
        alertLayout = findViewById(R.id.alert_layout)

        progressBar = findViewById(R.id.progress_bar)
        progressHorizontal = findViewById(R.id.progress_horizontal)
        recyclerView = findViewById(R.id.rv_movie)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun loadData() {
        presenter.loadData()
    }


    private fun loadDataByQuery(searchText: String) {
        presenter.loadDataByQuery(searchText)
    }


    private fun show() {
        if (movieList.isNotEmpty()) {
            setVisibleLayout(VisibleLayout.MOVIE_LIST)
            adapter = MainAdapter(movieList, this, movieDb)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }


    override fun showList(list: List<Movie>) {
        movieList.clear()
        movieList.addAll(list)
        show()
    }


    override fun showError() {
        Snackbar
            .make(main_view, "Проверьте соединение с интернетом и попробуйте еще раз", Snackbar.LENGTH_LONG)
            .show()
    }


    override fun showNotFound(query: String) {
        setVisibleLayout(VisibleLayout.NOT_FOUND)
        val notFound = findViewById<TextView>(R.id.not_found_txt)
        notFound.text = "По запросу “$query” ничего не найдено"
    }


    override fun showAlert() {
        setVisibleLayout(VisibleLayout.ALERT)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            loadData()
        }
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }


    override fun showProgressHorizontal(show: Boolean) {
        if (show) {
            progressHorizontal.visibility = View.VISIBLE
        } else {
            progressHorizontal.visibility = View.INVISIBLE
        }
    }


    private fun refresh() {
        movieListLayout.setColorSchemeColors(resources.getColor(R.color.electric_blue))
        movieListLayout.setProgressViewOffset(true, -200, -200)
        movieListLayout.setOnRefreshListener(swipeListener)
    }


    private val swipeListener = SwipeRefreshLayout.OnRefreshListener {
        if (searchText.text.toString().isEmpty()) {
            showProgressHorizontal(true)
            loadData()
        } else {
            showProgressHorizontal(true)
            loadDataByQuery(searchText.text.toString())
        }
        movieListLayout.isRefreshing = false
    }


    private fun setVisibleLayout(layout: VisibleLayout) {
        movieListLayout.visibility = View.GONE
        notFoundLayout.visibility = View.GONE
        alertLayout.visibility = View.GONE

        when (layout) {
            VisibleLayout.MOVIE_LIST -> movieListLayout.visibility = View.VISIBLE
            VisibleLayout.NOT_FOUND -> notFoundLayout.visibility = View.VISIBLE
            VisibleLayout.ALERT -> alertLayout.visibility = View.VISIBLE
        }
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        positionIndex = (recyclerView.layoutManager!! as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        outState!!.putInt(POSITION, positionIndex)
        outState.putParcelableArrayList(MOVIE_LIST, movieList as (ArrayList<out Parcelable>))
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }


    override fun onRetainCustomNonConfigurationInstance(): MainContract.Presenter {
        return presenter
    }

    enum class VisibleLayout {
        MOVIE_LIST,
        NOT_FOUND,
        ALERT
    }
}
