package de.stuttgart.syzl3000.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.requests.RecipeApiClient;

import static android.content.ContentValues.TAG;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient mRecipeApiClient;
    private String mQuery;
    private int mPageNumber;

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }
    private RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();

    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeApiClient.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mRecipeApiClient.searchRecipesApi(query, pageNumber);
    }

    public void searchNextPage() {
        Log.d(TAG, "searchNextPage: " + 1);
        searchRecipesApi(mQuery, mPageNumber + 1);
    }
    public void cancelRequest() {
        mRecipeApiClient.cancelRequest();
    }
}
