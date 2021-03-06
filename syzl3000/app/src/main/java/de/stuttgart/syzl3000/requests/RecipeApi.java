package de.stuttgart.syzl3000.requests;

import de.stuttgart.syzl3000.requests.response.RecipeResponse;
import de.stuttgart.syzl3000.requests.response.RecipeSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// we use retrofit to make http requests -> retrofit always requires such a API interface, where we specify the queries.
public interface RecipeApi {

    // SEARCH
    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe (
            @Query("q") String query,
            @Query("page") String page
    );

    // GET Recipe Request
    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("rId") String recipe_id
    );
}
