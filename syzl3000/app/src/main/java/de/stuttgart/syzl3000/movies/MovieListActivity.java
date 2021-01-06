package de.stuttgart.syzl3000.movies;

import android.os.Bundle;

import de.stuttgart.syzl3000.BaseActivity;
import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.adapters.OnRecipeListener;
import de.stuttgart.syzl3000.adapters.movies.OnMovieListener;
import de.stuttgart.syzl3000.network.UserService;
import de.stuttgart.syzl3000.util.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListActivity extends BaseActivity implements OnMovieListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

         
    }

    @Override
    public void onMovieClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }
}
