package de.stuttgart.syzl3000.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeById(String recipeId) {
        mRecipeRepository.searchRecipeById(recipeId);
    }
}
