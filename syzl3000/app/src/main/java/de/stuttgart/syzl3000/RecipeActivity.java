package de.stuttgart.syzl3000;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";
    
    // UI components
    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;   // into this we will put the ingredients programmatically
    private ScrollView mScrollView;    // this one I need, to set it to visible when the recipe is retrieved / loaded.

    private RecipeViewModel mRecipeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);

        mRecipeViewModel = new ViewModelProvider(this ).get(RecipeViewModel.class);

        showProgressBar(true);
        subscribeObservers();
        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
            // when an intent is retrieved, I want to search by tht recipeId and get the ingredients
            mRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
        }
    }

    private void subscribeObservers() {
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe != null) {
                    if (recipe.getRecipe_id().equals(mRecipeViewModel.getRecipeId())) {
                        setRecipeProperties(recipe);
                    }
                }
            }
        });
    }

    private  void setRecipeProperties (Recipe recipe) {
        if (recipe != null) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.getImage_url())
                    .into(mRecipeImage);

            mRecipeTitle.setText(recipe.getTitle());
            mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

            //remove all views that were previously in there
            mRecipeIngredientsContainer.removeAllViews();
            for (String ingredient: recipe.getIngredients()) {
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);  // 15 px
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                mRecipeIngredientsContainer.addView(textView);
            }
        }
        showParent();
        showProgressBar(false);
    }

    private void showParent() {
        mScrollView.setVisibility(View.VISIBLE);
    }
}
