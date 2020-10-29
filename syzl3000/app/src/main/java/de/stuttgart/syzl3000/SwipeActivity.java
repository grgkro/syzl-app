package de.stuttgart.syzl3000;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import de.stuttgart.syzl3000.adapters.ArrayAdapter;
import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.util.Testing;
import de.stuttgart.syzl3000.viewmodels.RecipeListViewModel;
import de.stuttgart.syzl3000.viewmodels.RecipeViewModel;

public class SwipeActivity extends BaseActivity  {

    private static final String TAG = "SwipeActivity";

    private RecipeListViewModel mRecipeListViewModel;
    private RecipeViewModel mRecipeViewModel;
//    private TextView mRecipeTitle, mRecipeRank;
//    private LinearLayout mRecipeIngredientsContainer;   // into this we will put the ingredients programmatically

    private boolean recepieListAlreadyReceived;
    private Recipe mRecipe;
    private List<Recipe> mRecipes;

    private ArrayAdapter arrayAdapter;
    private int i;

    ListView listView;
    List<Recipe> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        mRecipeViewModel = new ViewModelProvider(this ).get(RecipeViewModel.class);
        mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        subscribeObservers();
        subscribeObserversForSingleRecipe();
        getIncomingIntent();

        rowItems = new ArrayList<>();
        rowItems.add(new Recipe("String title23232ghjghjghj", "String publisher_url", "String recipe_id", "", "String publisher", "String _id", 100, "https://www.imagesource.com/wp-content/uploads/2019/06/Rio.jpg", new String[]{"ingredients"}));

        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.activity_recipe, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.swipe_frame);

        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                if (mRecipes.size() > 0) {
                    Recipe recipeToRemove = mRecipes.remove(0);
                    arrayAdapter.remove(recipeToRemove);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Recipe recipe = (Recipe) dataObject;
                String title = recipe.getTitle();
                Log.d(TAG, "onLeftCardExit: " + title);
                
                makeToast(SwipeActivity.this, "Dismissed!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Recipe recipe = (Recipe) dataObject;
                String title = recipe.getTitle();
                makeToast(SwipeActivity.this, "You Liked " + title + "!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
//                al.add("XML ".concat(String.valueOf(i)));
//                arrayAdapter.notifyDataSetChanged();
//                Log.d("LIST", "notified");
//                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(SwipeActivity.this, "Clicked!");
            }
        });


    }

    private void subscribeObservers() {
        // the LiveData (a.k.a. the recipes) comes from the Repository, goes to the ViewModel and arrives / gets observered here in the Activity: (Client: DB / API ->) Repo -> ViewModel -> Activity
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {

            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                makeToast(SwipeActivity.this, "Recipeeeeeeeeeeees observed: " + recipes.get(0).getTitle());
                if (recipes != null) {
                    Testing.printRecipes(recipes, "recipes test");
                    mRecipes = recipes;
                    recepieListAlreadyReceived = true;
                    rowItems = recipes;
                    arrayAdapter.clear();
                    arrayAdapter.addAll(mRecipes);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void subscribeObserversForSingleRecipe() {
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                makeToast(SwipeActivity.this, "Recipe observed: " + recipe.getTitle());
                if (recipe != null) {
                    if (recipe.getRecipe_id().equals(mRecipeViewModel.getRecipeId())) {
//                        setRecipeProperties(recipe);
                        mRecipe = recipe;  // we need the recipe as a class parameter, so that the layout.onClick Event can use it.
                        if (recepieListAlreadyReceived) {
                            rowItems.add(recipe);
                            arrayAdapter.add(recipe);
                        } else {
                            rowItems.clear();
                            rowItems.add(recipe);
                            arrayAdapter.clear();
                            arrayAdapter.add(recipe);
                        }
                        arrayAdapter.notifyDataSetChanged();
                        // we need to change mDidRetrieveRecipe to true, so that we don't show the timeOut-ErrorView after 3 sec.
                        mRecipeViewModel.setDidRetrieveRecipe(true);
                    }
                }
            }
        });
        mRecipeViewModel.isRecipeRequestTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean && !mRecipeViewModel.getDidRetrieveRecipe()) {
                    Log.d(TAG, "onChanged: timed out...");
//                    displayErrorScreen("Error retrieving data. Check network connection...");
                }
            }
        });
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
            // when an intent is retrieved, I want to search by tht recipeId and get the ingredients
            mRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
        }
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }


}