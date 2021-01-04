package de.stuttgart.syzl3000.presentation.ui.recipeList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.stuttgart.syzl3000.models.RecipeNew
import de.stuttgart.syzl3000.repositories.RecipeRepositoryNew
import kotlinx.coroutines.launch
import javax.inject.Named

class RecipeListViewModel
@ViewModelInject
constructor(
        private val repository: RecipeRepositoryNew,
        private @Named("auth_token") val token: String,
): ViewModel(){

    val recipes: MutableState<List<RecipeNew>> = mutableStateOf(listOf())

    init {
        viewModelScope.launch {
            val result = repository.search(
                    token = token,
                    page = 1,
                    query = "chicken"
            )
            recipes.value = result
        }
    }
}
















