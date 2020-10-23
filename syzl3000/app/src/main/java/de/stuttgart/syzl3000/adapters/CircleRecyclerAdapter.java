package de.stuttgart.syzl3000.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.stuttgart.syzl3000.R;
import de.stuttgart.syzl3000.models.Circle;
import de.stuttgart.syzl3000.models.Recipe;
import de.stuttgart.syzl3000.util.Constants;

// the Adapter sends the data to the view. We want to use this adapter for different views (Recipe List View, Single Recipe View, Loading Screen, No Recipes Found View, etc.), so we use the generic ViewType RecyclerView.ViewHolder
public class CircleRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    private static final int RECIPE_TYPE = 1;


    private List<Circle> mCircles;
    private OnCircleListener mOnCircleListener;

    public CircleRecyclerAdapter(OnCircleListener OnCircleListener) {
        mOnCircleListener = OnCircleListener;
        mCircles = new ArrayList<>();
        Circle testCircle1 = new Circle("Girlfriend", new ArrayList<>(Arrays.asList("Jessica")), null);
        Circle testCircle2 = new Circle("Johannes + Klaus", new ArrayList<>(Arrays.asList("Johannes", "Klaus")), "https://www.wonderplugin.com/wp-content/uploads/2013/12/Evening_1024.jpg");
        mCircles.add(testCircle1);
        mCircles.add(testCircle2);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Each ViewHolder holds one View / Circle
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_circle_list_item, viewGroup, false);
        return new CircleViewHolder(view, mOnCircleListener);
    }

    @Override
    // binds the data to the ViewHolder
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            // set the image
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background);

            Glide.with(((CircleViewHolder) viewHolder).itemView)
                    .setDefaultRequestOptions(options)
                    .load(mCircles.get(i).getImage_url())
                    .into(((CircleViewHolder) viewHolder).circleImage);

            ((CircleViewHolder) viewHolder).circleTitle.setText(mCircles.get(i).getTitle());
//            ((CircleViewHolder) viewHolder).publisher.setText(mCircles.get(i).getUsers());

    }

    @Override
    public int getItemCount() {
        return mCircles.size();
    }

    public void setCircles(List<Circle> circles){
        mCircles = circles;
        notifyDataSetChanged();
    }

    public Circle getSelectedCircle (int position) {
        if (mCircles != null) {
            if (mCircles.size() > 0) {
                return mCircles.get(position);
            }
        }
        return null;
    }

    public void displayCircles(){
        mCircles = new ArrayList<>();
        Circle testCircle1 = new Circle("Girlfriend", new ArrayList<>(Arrays.asList("Jessica")), null);
        Circle testCircle2 = new Circle("Johannes + Klaus", new ArrayList<>(Arrays.asList("Johannes", "Klaus")), "https://www.wonderplugin.com/wp-content/uploads/2013/12/Evening_1024.jpg");
        mCircles.add(testCircle1);
        mCircles.add(testCircle2);
        notifyDataSetChanged();
    }
}

























