package de.stuttgart.syzl3000;

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

import de.stuttgart.syzl3000.adapters.OnCircleListener;
import de.stuttgart.syzl3000.adapters.OnCircleListener;
import de.stuttgart.syzl3000.adapters.CircleRecyclerAdapter;
import de.stuttgart.syzl3000.models.Circle;
import de.stuttgart.syzl3000.util.Testing;
import de.stuttgart.syzl3000.util.VerticalSpacingItemDecorator;

public class CircleListActivity extends BaseActivity implements OnCircleListener {

    private static final String TAG = "CircleListActivity";

//    private CircleListViewModel mCircleListViewModel;
    private RecyclerView mRecyclerView;
    private CircleRecyclerAdapter mCircleRecyclerAdapter;
    private SearchView mSearchView;
    private RecipeListActivity mRecipyListActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_list);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        mRecyclerView = findViewById(R.id.circle_list);  // in activity_circle_list -> it's where we actually display the single circle Views
        mSearchView = findViewById(R.id.search_view);

//        mCircleListViewModel = new ViewModelProvider(this).get(CircleListViewModel.class);

        initRecyclerView();
//        subscribeObservers();
//        initSearchView();
        displayCircles();

//        if(!mCircleListViewModel.isViewingCircles()) {
//            // display the search categories
//            displaySearchCategories();
//        }
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
    }

//    private void subscribeObservers() {
//        // the LiveData (a.k.a. the circles) comes from the Repository, goes to the ViewModel and arrives / gets observered here in the Activity: (Client: DB / API ->) Repo -> ViewModel -> Activity
////        mCircleListViewModel.getCircles().observe(this, new Observer<List<Circle>>() {
//
//            @Override
//            public void onChanged(@Nullable List<Circle> circles) {
//                if (circles != null) {
//                    if (mCircleListViewModel.isViewingCircles()) {
//                        Testing.printCircles(circles, "circles test");
//                        mCircleListViewModel.setIsPerformingQuery(false);  // the query is complete now
//                        mCircleRecyclerAdapter.setCircles(circles);
//                    }
//                }
//            }
//        });
//    }

    private void initRecyclerView() {
        mCircleRecyclerAdapter = new CircleRecyclerAdapter(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);   // this will add the little spacing between the cards in the category list overview View (30px)
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mCircleRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!mRecyclerView.canScrollVertically(1)) {
                    // search the next page
//                    mCircleListViewModel.searchNextPage();
                }
            }
        });
    }

//    private void searchCirclesApi(String query, int pageNumber) {
//        mCircleListViewModel.searchCirclesApi(query, pageNumber);
//    }

    private void initSearchView() {
        // the search view needs to be excanged to one that searches for circles, not recipes...
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

//                mCircleRecyclerAdapter.displayLoading();
//                mCircleListViewModel.searchCirclesApi(query, 1);
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



    void displayCircles() {
//        mCircleListViewModel.setIsViewingCircles(false);
      if (mCircleRecyclerAdapter != null) {
          mCircleRecyclerAdapter.displayCircles();
      } else {
          initRecyclerView();
          mCircleRecyclerAdapter.displayCircles();
      }

    }

    @Override
    public void onBackPressed() {
//        if (mCircleListViewModel.onBackPressed()) {
//            super.onBackPressed();
//        } else {
//            displaySearchCategories();
//        }
        Log.d(TAG, "onBackPressed: not implemented yet" );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // ??
        switch (item.getItemId()) {
            case R.id.action_categories:
                mRecipyListActivity.displaySearchCategories();
                return super.onOptionsItemSelected(item);
            case R.id.action_circles:
                displayCircles();
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

    @Override
    public void onCircleClick(String circle) {
        Log.d(TAG, "onCircleClick: " + circle);
    }
}