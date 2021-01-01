package de.stuttgart.syzl3000;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.stuttgart.syzl3000.adapters.OnCircleListener;
import de.stuttgart.syzl3000.adapters.CircleRecyclerAdapter;
import de.stuttgart.syzl3000.authentication.SignUpActivity;
import de.stuttgart.syzl3000.util.RequestQueueSingleton;
import de.stuttgart.syzl3000.util.VerticalSpacingItemDecorator;

import static de.stuttgart.syzl3000.util.Constants.BRUDDAAL_BASE_URL;

public class CircleListActivity extends BaseActivity implements OnCircleListener {

    private static final String TAG = "CircleListActivity";

    //    private CircleListViewModel mCircleListViewModel;
    private RecyclerView mRecyclerView;
    private CircleRecyclerAdapter mCircleRecyclerAdapter;
    private SearchView mSearchView;
    private RecipeListActivity mRecipyListActivity;
    private EncryptedPreferences encryptedPreferences;
    private String email;
    private String password;
    private String idToken;
    private String sub;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_list);

        getCredentialsFromSharedPreferences();

        // Get the Intent that started this activity and extract the string
        category = getIntent().getStringExtra("category");
        Log.d(TAG, "The intent: " + category);

        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i(TAG, "User attributes = " + attributes.toString());
                    sub = attributes.get(0).getValue();
                    Log.i(TAG, "User sub = " + sub);
                    getGangs();
                },
                error -> Log.e("AuthDemo", "Failed to fetch user attributes.", error)
        );

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
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    private void getGangs() {
        Log.i(TAG, "Going to GET the Gangs of this user.");
        if (category.equals("Recipes")) {
//            RequestQueue queue = RequestQueueSingleton.getInstance(this.getApplicationContext()).
//                    getRequestQueue();

            String url = BRUDDAAL_BASE_URL + "users/" + sub + "/gangs";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "Response: " + response.toString());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Log.d(TAG, "Error retrieving gangs" + error);
                        }
                    }) {    //this is the part, that adds the header to the request
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", idToken);
                    params.put("content-type", "application/json");
                    return params;
                }
            };
            RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
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

    private void getCredentialsFromSharedPreferences() {
        encryptedPreferences = new EncryptedPreferences.Builder(this).withEncryptionPassword("MyTestPassword").build();
        email = encryptedPreferences.getString("email", null);
        password = encryptedPreferences.getString("pw", null);
        idToken = encryptedPreferences.getString("idToken", null);
    }

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
        if (category.equals("Recipes")) {
            startRecipeListActivity();
        } else if (category.equals("Movies")) {
            Log.d(TAG, "onBackPressed: Not implemented yet for Movies");
        }
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
            case R.id.action_signOut:
                signOut();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void signOut() {
        Amplify.Auth.signOut(
                () -> {
                    Log.i("AuthQuickstart", "Signed out successfully");
                    startAuthenticationActivity();
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    public void startAuthenticationActivity() {
        Log.i(TAG, "Starting Authentication Activity");
        Intent i = new Intent(CircleListActivity.this, SignUpActivity.class);
        CircleListActivity.this.startActivity(i);
    }

    public void startRecipeListActivity() {
        Log.i(TAG, "Starting RecipeList Activity");
        Intent i = new Intent(CircleListActivity.this, RecipeListActivity.class);
        CircleListActivity.this.startActivity(i);
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