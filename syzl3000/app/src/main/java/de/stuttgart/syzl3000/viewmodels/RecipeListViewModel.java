package de.stuttgart.syzl3000.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.repositories.RecipeRepository;

// getting, holding and displaying the recipes list
// there is no LiveData kept in the ViewModel itself, it returns the LiveData straight from the Repository
public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    // should I display the categories?
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;

    public RecipeListViewModel() {
       mRecipeRepository = RecipeRepository.getInstance();
        mIsPerformingQuery = false;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber) {
        mIsViewingRecipes = true;        // this is not yet true, the query was just started, but we already set it to true.
        mIsPerformingQuery = true;
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }

    public boolean isViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsPerformingQuery(Boolean isPerformingQuery) {
        mIsPerformingQuery = mIsPerformingQuery;
    }

    public boolean isPerformingQuery() {
        return mIsPerformingQuery;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes) {
        mIsViewingRecipes = isViewingRecipes;
    }

    public boolean onBackPressed() {
        if (isPerformingQuery()) {
            //cancel the query and go back to categories
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }
        if (mIsViewingRecipes) {
            mIsViewingRecipes = false;
            return false;
        }
            return true;

    }


}
