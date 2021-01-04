package de.stuttgart.syzl3000.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import de.stuttgart.syzl3000.network.RecipeService
import de.stuttgart.syzl3000.network.model.RecipeDtoMapper
import de.stuttgart.syzl3000.repositories.RecipeRepositoryNew
import de.stuttgart.syzl3000.repositories.RecipeRepository_Impl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
            recipeService: RecipeService,
            recipeMapper: RecipeDtoMapper,
    ): RecipeRepositoryNew{
        return RecipeRepository_Impl(
            recipeService = recipeService,
            mapper = recipeMapper
        )
    }
}

