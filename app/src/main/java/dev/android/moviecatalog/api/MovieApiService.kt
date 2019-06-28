package dev.android.moviecatalog.api

import dev.android.moviecatalog.models.MovieResponse
import dev.android.moviecatalog.utils.Constants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("discover/movie?")
    fun getListMovie(
        @Query("api_key") api: String = Constants.API_KEY,
        @Query("language") lang: String = "ru-RUS"
    ): Observable<MovieResponse>


    @GET("search/movie?")
    fun getListMovieBySearch(
        @Query("api_key") api: String = Constants.API_KEY,
        @Query("language") lang: String = "ru-RUS",
        @Query("query") query: String
    ): Observable<MovieResponse>
}