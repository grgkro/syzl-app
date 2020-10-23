package de.stuttgart.syzl3000.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.requests.RecipeApiClient;

import static android.content.ContentValues.TAG;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient mRecipeApiClient;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    // MediatorLiveData is a mediator that lets you change LiveData before it is returned
    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }
    private RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
initMediators();
    }

    private void initMediators() {
        LiveData<List<Recipe>> recipeListApiSource = mRecipeApiClient.getRecipes();
        // mediator is kind of an inbetween section and source is where this inbetween section is getting its data from
        mRecipes.addSource(recipeListApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                // this method will trigger, if the source (= mRecipeApiClient.getRecipes()) changes
                // observer is very similar to a mediator
                if (recipes != null) {
                    // I still want to add the recipes
                    mRecipes.setValue(recipes);
                    doneQuery(recipes);
                }
                else {
                    // search database cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery (List<Recipe> list) {
        if (list != null) {
            if (list.size() % 30 != 0) {
                mIsQueryExhausted.setValue(true);
            }
        } else {
            mIsQueryExhausted.setValue(true);
        }
    }

    public LiveData<Boolean> isQueryExhausted() {
        return mIsQueryExhausted;
    }

    // Getters for the LiveData
    public LiveData<List<Recipe>> getRecipes() {
        // we return the Mediator instead of the LiveData, because we want to make a change to it before.
        return mRecipes;
//        return mRecipeApiClient.getRecipes();
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipeApiClient.getRecipe();
    }

    public LiveData<Boolean> isRecipeRequestTimedOut() {
        return mRecipeApiClient.isRecipeRequestTimedOut();
    }

    public void searchRecipeById (String recipeId) {
        mRecipeApiClient.searchRecipeById(recipeId);
    }

    public void searchRecipesApi(String query, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
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
