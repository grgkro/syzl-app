package de.stuttgart.syzl3000;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.URL;

import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";
    
    // UI components
    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;   // into this we will put the ingredients programmatically
    private ScrollView mScrollView;    // this one I need, to set it to visible when the recipe is retrieved / loaded.
private Recipe mRecipe;
private RelativeLayout mLayout;

    private RecipeViewModel mRecipeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mLayout = findViewById(R.id.recipe_layout);
        mLayout.setOnClickListener(layoutClickListener);

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

    private View.OnClickListener layoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onTouchEvent: --------------------------------------------");
            if (mRecipe != null) {
                openGoogleTab(mRecipe.getSource_url());
            }
        }
    };

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
                        mRecipe = recipe;  // we need the recipe as a class parameter, so that the layout.onClick Event can use it.
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
                    displayErrorScreen("Error retrieving data. Check network connection...");
                }
            }
        });
    }

    private void displayErrorScreen(String errorMessage) {
        mRecipeTitle.setText("Error retrieving recipe...");
        mRecipeRank.setText("");
        TextView textView = new TextView(this);
        if (!errorMessage.equals("")) {
            textView.setText(errorMessage);
        } else {
            textView.setText("Error");
        }
        textView.setTextSize(15);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        mRecipeIngredientsContainer.addView(textView);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.ic_launcher_background)
                .into(mRecipeImage);

        showParent();
        showProgressBar(false);
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

    private void openGoogleTab(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void showParent() {
        mScrollView.setVisibility(View.VISIBLE);
    }
}
