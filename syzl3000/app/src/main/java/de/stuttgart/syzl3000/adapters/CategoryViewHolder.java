package de.stuttgart.syzl3000.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import de.stuttgart.syzl3000.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener
{
    CircleImageView categoryImage;
    TextView categoryTitle;
    OnRecipeListener listener;

    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
        super(itemView);
        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);
        listener = onRecipeListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listener.onCategoryClick(categoryTitle.getText().toString());
    }
}
