package de.stuttgart.syzl3000.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val id: String? = null,
        val gangs: List<String>? = null,
        val username: String? = null,
        val likedRecipes: List<String>? = null,
        val dislikedRecipes: List<String>? = null,
        val likedMovies: List<String>? = null,
        val dislikedMovies: List<String>? = null,
        val email: String? = null,
        val picture: String? = null,
) : Parcelable