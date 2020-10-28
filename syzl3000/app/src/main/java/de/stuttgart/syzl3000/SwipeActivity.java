package de.stuttgart.syzl3000;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import de.stuttgart.syzl3000.adapters.ArrayAdapter;
import de.stuttgart.syzl3000.models.Recipe;

public class SwipeActivity extends Activity {

    private static final String TAG = "SwipeActivity";

    private ArrayAdapter arrayAdapter;
    private int i;

    ListView listView;
    List<Recipe> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        rowItems = new ArrayList<>();
        rowItems.add(new Recipe("String title2", "String publisher_url", "String recipe_id", "String source_url", "String publisher", "String _id", 100, "String image_url", new String[]{"ingredients"}));
        rowItems.add(new Recipe("String title3", "String publisher_url", "String recipe_id", "String source_url", "String publisher", "String _id", 100, "String image_url", new String[]{"ingredients"}));
        rowItems.add(new Recipe("String title23", "String publisher_url", "String recipe_id", "String source_url", "String publisher", "String _id", 100, "String image_url", new String[]{"ingredients"}));
        rowItems.add(new Recipe("String title23232", "String publisher_url", "String recipe_id", "String source_url", "String publisher", "String _id", 100, "String image_url", new String[]{"ingredients"}));
        rowItems.add(new Recipe("String title23232", "String publisher_url", "String recipe_id", "String source_url", "String publisher", "String _id", 100, "https://res.cloudinary.com/dk4ocuiwa/image/upload/v1575163942/RecipesApi/353269d44b.jpg", new String[]{"ingredients"}));

        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.activity_recipe, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.swipe_frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Recipe recipe = (Recipe) dataObject;
                String title = recipe.getTitle();
                Log.d(TAG, "onLeftCardExit: " + title);
                
                makeToast(SwipeActivity.this, "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(SwipeActivity.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
//                al.add("XML ".concat(String.valueOf(i)));
//                arrayAdapter.notifyDataSetChanged();
//                Log.d("LIST", "notified");
//                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(SwipeActivity.this, "Clicked!");
            }
        });

    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

}