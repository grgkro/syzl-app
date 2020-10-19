package de.stuttgart.syzl3000.requests.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.stuttgart.syzl3000.models.Recipe;

public class RecipeResponse {
    @SerializedName("recipe")
    @Expose
    private Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipe=" + recipe +
                '}';
    }
}
