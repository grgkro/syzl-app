package de.stuttgart.syzl3000.network.repsonses

import com.google.gson.annotations.SerializedName
import de.stuttgart.syzl3000.network.model.RecipeDto

data class RecipeSearchResponseNew(

        @SerializedName("count")
        var count: Int,

        @SerializedName("results")
        var recipes: List<RecipeDto>,
)