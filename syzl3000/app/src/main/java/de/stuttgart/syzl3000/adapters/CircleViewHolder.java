package de.stuttgart.syzl3000.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import de.stuttgart.syzl3000.R;

public class CircleViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener
{
    CircleImageView circleImage;
    TextView circleTitle;
    OnCircleListener listener;

    public CircleViewHolder(@NonNull View itemView, OnCircleListener onCircleListener) {
        super(itemView);
        circleImage = itemView.findViewById(R.id.circle_image);
        circleTitle = itemView.findViewById(R.id.circle_title);
        listener = onCircleListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listener.onCircleClick(circleTitle.getText().toString());
    }
}
