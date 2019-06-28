package dev.android.moviecatalog.models

import com.google.gson.annotations.SerializedName

class MovieResponse(

    @SerializedName("total_results")
    var totalResult: Int,

    @SerializedName("results")
    var movies: List<Movie>
)
