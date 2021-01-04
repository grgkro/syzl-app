package de.stuttgart.syzl3000.repositories

import de.stuttgart.syzl3000.models.RecipeNew


interface RecipeRepositoryNew {

    suspend fun search(token: String, page: Int, query: String): List<RecipeNew>

    suspend fun get(token: String, id: Int): RecipeNew

}
