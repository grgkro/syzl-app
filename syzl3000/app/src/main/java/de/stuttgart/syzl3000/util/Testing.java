package de.stuttgart.syzl3000.util;

import android.util.Log;

import java.util.List;

import de.stuttgart.syzl3000.models.Recipe;

public class Testing {
    public static void printRecipes(List<Recipe> list, String tag) {
        for (Recipe recipe: list) {
            Log.d(tag, "onChanged: " + recipe.getTitle());
        }
    }
}
