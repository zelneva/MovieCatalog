package dev.android.moviecatalog.db

import android.content.Context

class MovieDb(var context: Context) {

    fun loadFavorite(): ArrayList<Int>? {
        return MovieDbRepository(context).findAll()
    }


    fun deleteFavorite(movieId: Int, context: Context) {
        MovieDbRepository(context).deleteFavorite(movieId)
    }


    fun addFavorite(movieId: Int, context: Context) {
        MovieDbRepository(context).create(movieId)
    }
}
