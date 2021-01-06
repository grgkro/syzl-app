package de.stuttgart.syzl3000;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;

import java.util.List;

import de.stuttgart.syzl3000.BaseActivity;
import de.stuttgart.syzl3000.CircleListActivity;
import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.SwipeActivity;
import de.stuttgart.syzl3000.adapters.RecipeRecyclerAdapter;
import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.movies.MovieListActivity;
import de.stuttgart.syzl3000.util.Testing;
import de.stuttgart.syzl3000.util.VerticalSpacingItemDecorator;
import de.stuttgart.syzl3000.viewmodels.RecipeListViewModel;

public class SelectTopCategoryActivity extends BaseActivity {

    private static final String TAG = "SelectTopCategoryActiv";

    private AppCompatImageView mRecipesImage, mMoviesImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_category);
        mMoviesImage = findViewById(R.id.select_movies_image);
        mRecipesImage = findViewById(R.id.select_recipes_image);

        mRecipesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTopCategoryActivity.this, RecipeListActivity.class);
                startActivity(intent);
            }
        });

        mMoviesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: movies not implemented yet");
                Intent intent = new Intent(SelectTopCategoryActivity.this, MovieListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "onBackPressed: back pressed, not implemented yet");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_categories:
                Log.d(TAG, "onOptionsItemSelected: Not implemented yet -> makes no sense here anyway, delete");
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
        Log.d(TAG, "onCreateOptionsMenu: need new menu for this, not recipe menu");
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}