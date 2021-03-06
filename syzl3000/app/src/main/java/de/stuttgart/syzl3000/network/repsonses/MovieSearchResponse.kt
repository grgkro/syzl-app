package de.stuttgart.syzl3000.network.repsonses

import com.google.gson.annotations.SerializedName
import de.stuttgart.syzl3000.network.model.MovieDto

data class MovieSearchResponse (
        @SerializedName("count")
        var count: Int,
@SerializedName("results")
        var movies: List<MovieDto>
        ) {
}