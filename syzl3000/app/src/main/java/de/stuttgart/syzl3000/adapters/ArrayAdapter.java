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


    public ArrayAdapter(Context context, int resourceId, List<Recipe> recipes) {
        super(context, resourceId, recipes);
    }

//    To customize the appearance of each item you can override the toString() method for the objects in your array. Or, to create a view for each item that's something other than a TextView (for example, if you want an ImageView for each array item), extend the ArrayAdapter class and override getView() to return the type of view you want for each item.
   public View getView(int position, View convertView, ViewGroup parent) {
       RecyclerView.ViewHolder holder = null;
        Recipe recipe = getItem(position);
       if (convertView == null) {
//           Instantiates a layout XML file into its corresponding View objects.
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_recipe, parent, false);
       }

       ImageView image = convertView.findViewById(R.id.single_recipe_image);
       
//       holder.i = (ImageView) convertView.findViewById(R.id.icon);
       TextView title = (TextView) convertView.findViewById(R.id.recipe_title);
       TextView social_score = (TextView) convertView.findViewById(R.id.recipe_social_score);
       TextView ingredients_title = (TextView) convertView.findViewById(R.id.ingredients_title);

//       image.setImageURI(Uri.parse(recipe.getImage_url()));
//       image.setImageResource(Uri.parse("https://www.imagesource.com/wp-content/uploads/2019/06/Rio.jpg"));
       image.setImageResource(R.mipmap.ic_launcher);
//       RequestOptions options = new RequestOptions()
//               .centerCrop()
//               .error(R.drawable.ic_launcher_background);
//
//       Glide.with(((CircleViewHolder) holder).itemView)
//               .setDefaultRequestOptions(options)
//               .load("https://www.imagesource.com/wp-content/uploads/2019/06/Rio.jpg")
//               .into(((CircleViewHolder) holder).circleImage);
       title.setText("TEST");
       ingredients_title.setText("Ingedients lkdfs");
               social_score.setText("200");
//       title.setText(recipe.getTitle());

       return convertView;
   }
}
