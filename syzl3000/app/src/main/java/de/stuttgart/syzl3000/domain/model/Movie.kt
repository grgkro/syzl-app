package de.stuttgart.syzl3000.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
        val id: String? = null,
        val rating: Double? = null,
        val title: String? = null,
        val shortTitle: String? = null,
        val crew: String? = null,
        val genre: String? = null,
        val image: String? = null,
        val isInTheaters: Boolean? = null,
        val length: Int? = null,
        val plot: String? = null,
        val ratingCount: Int? = null,
        val year: Int? = null
) : Parcelable