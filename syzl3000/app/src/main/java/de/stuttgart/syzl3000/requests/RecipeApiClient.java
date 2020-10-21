package de.stuttgart.syzl3000.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import de.stuttgart.syzl3000.AppExecutors;
import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.requests.response.RecipeSearchResponse;
import de.stuttgart.syzl3000.util.Constants;
import retrofit2.Call;
import retrofit2.Response;

import static de.stuttgart.syzl3000.util.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;

    public static RecipeApiClient getInstance() {
         if (instance == null ) {
             instance = new RecipeApiClient();
         }
         return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    public void searchRecipesApi(String query, int pageNumber) {
        // we want a new, fresh instance of mRetrieveRecipesRunnable
        if (mRetrieveRecipesRunnable != null) {
            mRetrieveRecipesRunnable = null;
        }
        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query, pageNumber);
        // before we used mRetrieveRecipesRunnable here, we used a new Runnable inside the submit() part.
        // why Future? Future nimmt den Wert einer asynchronen Task an. Man kann in der zwischenzeit andere aufgaben ausführen und dann mit handler.get() den Wert abfragen (falls er bis dahin da ist.)
        // the AppExecutors is needed to execute 3 Runnables after each other with a defined time inbetween.
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipesRunnable);

        //this task / thread is running 3 seconds after the first task
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let the user know that the request is timedout

                // stop the request
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveRecipesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // this is the actual API call, so it needs to run in the background
            try {
                Response response = getRecipes(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if(response.code() == 200) {
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                    // if we retrieve the first page, send that data to the LiveData
                    if(pageNumber == 1) {
                        mRecipes.postValue(list);
                    } else {
                        // if we retrieve further recipes, we dont want to replace the first 30, but append them to them.
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(currentRecipes);
                    }
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error );
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }

        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber) {
            return ServiceGenerator.getRecipeApi().searchRecipe(
                    query,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling the search request.");
            cancelRequest = true;
        }
    }

}
