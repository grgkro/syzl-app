package de.stuttgart.syzl3000.network.model

data class UserDto(
        var id: String? = null,
        var gangs: List<String>? = null,
        var username: String? = null,
        var likedRecipes: List<String>? = null,
        var dislikedRecipes: List<String>? = null,
        var likedMovies: List<String>? = null,
        var dislikedMovies: List<String>? = null,
        var email: String? = null,
        var picture: String? = null,
)

