package de.stuttgart.syzl3000.network.repsonses

import com.google.gson.annotations.SerializedName
import de.stuttgart.syzl3000.network.model.MovieNetworkEntity
import de.stuttgart.syzl3000.network.model.UserNetworkEntity

class UserSearchResponse(
        @SerializedName("User")
        var user: UserNetworkEntity
)