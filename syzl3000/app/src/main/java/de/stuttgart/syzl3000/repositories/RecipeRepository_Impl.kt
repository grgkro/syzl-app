package de.stuttgart.syzl3000.repositories

import de.stuttgart.syzl3000.models.RecipeNew
import de.stuttgart.syzl3000.network.RecipeService
import de.stuttgart.syzl3000.network.model.RecipeDtoMapper

class RecipeRepository_Impl (
        private val recipeService: RecipeService,
        private val mapper: RecipeDtoMapper,
): RecipeRepositoryNew {

    override suspend fun search(token: String, page: Int, query: String): List<RecipeNew> {
        return mapper.toDomainList(recipeService.search(token = token, page = page, query = query).recipes)
    }

    override suspend fun get(token: String, id: Int): RecipeNew {
        return mapper.mapToDomainModel(recipeService.get(token = token, id))
    }

}