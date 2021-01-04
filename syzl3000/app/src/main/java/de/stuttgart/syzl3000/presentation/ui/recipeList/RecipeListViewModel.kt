package de.stuttgart.syzl3000.presentation.ui.recipeList

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import de.stuttgart.syzl3000.repositories.RecipeRepositoryNew
import javax.inject.Named

class RecipeListViewModel
@ViewModelInject
constructor(
        private val repository: RecipeRepositoryNew,
        private @Named("auth_token") val token: String,
): ViewModel(){

    fun getRepo() = repository

    fun getAuthToken() = token

}
















