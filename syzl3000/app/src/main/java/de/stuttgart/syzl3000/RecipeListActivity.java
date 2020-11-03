package de.stuttgart.syzl3000;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import de.stuttgart.syzl3000.adapters.OnRecipeListener;
import de.stuttgart.syzl3000.adapters.RecipeRecyclerAdapter;
import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.util.Testing;
import de.stuttgart.syzl3000.util.VerticalSpacingItemDecorator;
import de.stuttgart.syzl3000.viewmodels.RecipeListViewModel;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";

    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mRecipeRecyclerAdapter;
    private SearchView mSearchView;
    private CircleListActivity mCircleListActivity;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecyclerView = findViewById(R.id.recipe_list);
        mSearchView = findViewById(R.id.search_view);
        context = this;

        mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        initRecyclerView();
        subscribeObservers();
        initSearchView();


        if(!mRecipeListViewModel.isViewingRecipes()) {
            // display the search categories
            displaySearchCategories();
        }
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
    }

    private void subscribeObservers() {
        // the LiveData (a.k.a. the recipes) comes from the Repository, goes to the ViewModel and arrives / gets observered here in the Activity: (Client: DB / API ->) Repo -> ViewModel -> Activity
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {

            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    if (mRecipeListViewModel.isViewingRecipes()) {
                        Testing.printRecipes(recipes, "recipes test");

//                        mRecipeRecyclerAdapter.setRecipes(recipes);
                        Intent intent = new Intent(context, SwipeActivity.class);
                        if (mRecipeRecyclerAdapter.getSelectedRecipe(0) != null) {
//                            intent.putExtra("recipe", mRecipeRecyclerAdapter.getSelectedRecipe(0));
                        } else {
                            Recipe newRecipe = new Recipe("Title", "URL", "id", "source", "publi", "idd", 100, "image", new String[]{"dfsdf"});
                            intent.putExtra("recipe", newRecipe);
                        }
//                        mRecipeListViewModel.setIsPerformingQuery(false);  // the query is complete now
                        startActivity(intent);
                    }
                }
            }
        });

        mRecipeListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Log.d(TAG, "onChanged: is exhausted");
                    mRecipeRecyclerAdapter.setQueryExhausted();
                }

            }
        });
    }

    private void initRecyclerView() {
        mRecipeRecyclerAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);   // this will add the little spacing between the cards in the category list overview View (30px)
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mRecipeRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!mRecyclerView.canScrollVertically(1)) {
                    // search the next page
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });
    }

    private void searchRecipesApi(String query, int pageNumber) {
        mRecipeListViewModel.searchRecipesApi(query, pageNumber);
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mRecipeRecyclerAdapter.displayLoading();
                mRecipeListViewModel.searchRecipesApi(query, 1);
                mSearchView.clearFocus();      // this takes the Focus from the search field when the query was submitted -> otherwise the back press button would first remove the focus from the searchView and then only on the second prss go back.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // we are not interested in changes, just on submitions...
                return false;
            }
        });
    }
    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, SwipeActivity.class);
//        intent.putExtra("recipe", mRecipeRecyclerAdapter.getSelectedRecipe(position));
//        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        mRecipeRecyclerAdapter.displayLoading();
        mRecipeListViewModel.searchRecipesApi(category, 1);
        mSearchView.clearFocus();
    }

    void displaySearchCategories() {
        mRecipeListViewModel.setIsViewingRecipes(false);
        mRecipeRecyclerAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {
        if (mRecipeListViewModel.onBackPressed()) {
            super.onBackPressed();
        } else {
            displaySearchCategories();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_categories:
                displaySearchCategories();
                return super.onOptionsItemSelected(item);
            case R.id.action_circles:
                Intent intent = new Intent(this, CircleListActivity.class);
                startActivity(intent);

                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}