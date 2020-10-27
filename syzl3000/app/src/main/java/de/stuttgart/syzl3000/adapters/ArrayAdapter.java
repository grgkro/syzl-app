package de.stuttgart.syzl3000.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.models.Recipe;

public class ArrayAdapter extends android.widget.ArrayAdapter {

    Context context;

    public ArrayAdapter(Context context, int resourceId, List<Recipe> recipes) {
super(context, resourceId, recipes);
    }

   public View getView(int position, View convertView, ViewGroup parent) {
        Recipe recipe = (Recipe) getItem(position);
       if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_recipe, parent, false);
       }

       ImageView image = (ImageView) convertView.findViewById(R.id.recipe_image);
       TextView title = (TextView) convertView.findViewById(R.id.recipe_title);

       image.setImageURI(Uri.parse(recipe.getImage_url()));
       title.setText(recipe.getTitle());

       return convertView;
   }
}
