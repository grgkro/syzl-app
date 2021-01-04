package de.stuttgart.syzl3000.network.model


import de.stuttgart.syzl3000.domain.util.DomainMapper
import de.stuttgart.syzl3000.models.RecipeNew

class RecipeDtoMapper : DomainMapper<RecipeDto, RecipeNew> {

    override fun mapToDomainModel(model: RecipeDto): RecipeNew {
        return RecipeNew(
                id = model.pk,
                title = model.title,
                featuredImage = model.featuredImage,
                rating = model.rating,
                publisher = model.publisher,
                sourceUrl = model.sourceUrl,
                description = model.description,
                cookingInstructions = model.cookingInstructions,
                ingredients = model.ingredients.orEmpty(),
                dateAdded = model.dateAdded,
                dateUpdated = model.dateUpdated,
        )
    }

    override fun mapFromDomainModel(domainModel: RecipeNew): RecipeDto {
        return RecipeDto(
                pk = domainModel.id,
                title = domainModel.title,
                featuredImage = domainModel.featuredImage,
                rating = domainModel.rating,
                publisher = domainModel.publisher,
                sourceUrl = domainModel.sourceUrl,
                description = domainModel.description,
                cookingInstructions = domainModel.cookingInstructions,
                ingredients = domainModel.ingredients,
                dateAdded = domainModel.dateAdded,
                dateUpdated = domainModel.dateUpdated,
        )
    }

    fun toDomainList(initial: List<RecipeDto>): List<RecipeNew>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<RecipeNew>): List<RecipeDto>{
        return initial.map { mapFromDomainModel(it) }
    }


}