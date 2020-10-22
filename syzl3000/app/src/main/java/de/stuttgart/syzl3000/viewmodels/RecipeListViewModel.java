package de.stuttgart.syzl3000.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.repositories.RecipeRepository;

// getting, holding and displaying the recipes list
// there is no LiveData kept in the ViewModel itself, it returns the LiveData straight from the Repository
public class RecipeListViewModel extends ViewModel {

    private static final String TAG = "RecipeListViewModel";

    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsPerformingQuery = false;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber){
        mIsViewingRecipes = true;
        mIsPerformingQuery = true;
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }

    public boolean isViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes){
        mIsViewingRecipes = isViewingRecipes;
    }

    public void setIsPerformingQuery(boolean isPerformingQuery){
        mIsPerformingQuery = isPerformingQuery;
    }

    public boolean onBackPressed(){
        if(mIsPerformingQuery){
            Log.d(TAG, "onBackPressed: canceling the request");
            mRecipeRepository.cancelRequest();
        }
        if(mIsViewingRecipes){
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }

    public void searchNextPage(){
        Log.d(TAG, "searchNextPage: called.");
        if(!mIsPerformingQuery
                && mIsViewingRecipes){
            mRecipeRepository.searchNextPage();
        }
    }
}



















