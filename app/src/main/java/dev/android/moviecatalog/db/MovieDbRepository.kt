package dev.android.moviecatalog.db

import android.content.Context
import org.jetbrains.anko.db.*

class MovieDbRepository(val context: Context) {

    companion object {
        const val TABLE_NAME = "favorite"
    }


    fun findAll(): ArrayList<Int>? = context.database.use {
        val objects = ArrayList<Int>()

        select(TABLE_NAME, "movie_id")
            .parseList(object : MapRowParser<List<Int>> {
                override fun parseRow(columns: Map<String, Any?>): List<Int> {
                    val movieId = columns["movie_id"].toString().toInt()
                    if (movieId != null) {
                        objects.add(movieId)
                    }
                    return objects
                }
            })
        objects
    }


    fun create(movieId: Int) = context.database.use {
        insert(TABLE_NAME,
            "movie_id" to movieId)
    }


    fun deleteFavorite(movieId: Int) = context.database.use {
        delete(TABLE_NAME, "movie_id = {movie_id}", "movie_id" to movieId)
    }
}
