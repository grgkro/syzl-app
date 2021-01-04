package de.stuttgart.syzl3000.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * See Recipe example: https://food2fork.ca/
 */
@Parcelize
data class RecipeNew (
    val id: Int? = null,
    val title: String? = null,
    val publisher: String? = null,
    val featuredImage: String? = null,
    val rating: Int? = 0,
    val sourceUrl: String? = null,
    val description: String? = null,
    val cookingInstructions: String? = null,
    val ingredients: List<String> = listOf(),
    val dateAdded: String? = null,
    val dateUpdated: String? = null,
) : Parcelable