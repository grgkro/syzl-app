package de.stuttgart.syzl3000.network.repsonses

import com.google.gson.annotations.SerializedName
import de.stuttgart.syzl3000.network.model.UserDto

data class UserSearchResponse(
        @SerializedName("User")
        var user: UserDto
)