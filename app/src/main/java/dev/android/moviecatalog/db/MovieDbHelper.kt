package dev.android.moviecatalog.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class MovieDbHelper(context: Context) : ManagedSQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        private const val DATABASE_NAME = "movie"
        private const val TABLE_NAME = "favorite"

        private var instance: MovieDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): MovieDbHelper {
            if (instance == null) {
                instance = MovieDbHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(database: SQLiteDatabase) {

        database.createTable(
            TABLE_NAME,
            true,
            "movie_id" to INTEGER + NOT_NULL
        )
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.dropTable(TABLE_NAME, ifExists = true)
    }

}

val Context.database: MovieDbHelper
    get() = MovieDbHelper.getInstance(applicationContext)