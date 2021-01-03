package de.stuttgart.syzl3000.network.model

import com.google.gson.annotations.SerializedName

class MovieNetworkEntity(

//        @SerializedName("id")
        var id: String? = null,
        var rating: Double? = null,
        var title: String? = null,
        var shortTitle: String? = null,
        var crew: String? = null,
        var genre: String? = null,
        var image: String? = null,
        var isInTheaters: Boolean? = null,
        var length: Int? = null,
        var plot: String? = null,
        var ratingCount: Int? = null,
        var year: Int? = null
) {

}