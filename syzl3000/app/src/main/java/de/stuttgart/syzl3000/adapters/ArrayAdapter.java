package de.stuttgart.syzl3000.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.models.Recipe;

//When the content for your layout is dynamic or not pre-determined, you can use a layout that subclasses AdapterView to populate the layout with views at runtime. A subclass of the AdapterView class uses an Adapter to bind data to its layout. The Adapter behaves as a middleman between the data source and the AdapterView layout—the Adapter retrieves the data (from a source such as an array or a database query) and converts each entry into a view that can be added into the AdapterView layout.
//ArrayAdapter:
//Use this adapter when your data source is an array. By default, ArrayAdapter creates a view for each array item by calling toString() on each item and placing the contents in a TextView.
public class ArrayAdapter extends android.widget.ArrayAdapter<Recipe> {

    Context context;
    private static final String TAG = "ArrayAdapter";

    public ArrayAdapter(Context context, int resourceId, List<Recipe> recipes) {
        super(context, resourceId, recipes);
    }

    //    To customize the appearance of each item you can override the toString() method for the objects in your array. Or, to create a view for each item that's something other than a TextView (for example, if you want an ImageView for each array item), extend the ArrayAdapter class and override getView() to return the type of view you want for each item.
    public View getView(int position, View convertView, ViewGroup parent) {
        Recipe recipe = getItem(position);
        if (convertView == null) {
//           Instantiates a layout XML file into its corresponding View objects.
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_recipe, parent, false);
        }


        ImageView image = convertView.findViewById(R.id.single_recipe_image);

//       holder.i = (ImageView) convertView.findViewById(R.id.icon);
        TextView title = convertView.findViewById(R.id.recipe_title);
        TextView social_score = convertView.findViewById(R.id.recipe_social_score);
        TextView ingredients_title = convertView.findViewById(R.id.ingredients_title);

        Glide.with(getContext()).load(recipe.getImage_url()).into(image);
        title.setText(recipe.getTitle());
        ingredients_title.setText("Ingredients");
        social_score.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

        return convertView;
    }

}
