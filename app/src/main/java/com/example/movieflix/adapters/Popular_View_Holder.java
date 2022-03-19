package com.example.movieflix.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieflix.R;

public class Popular_View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnMovieListener onMovieListener;
    ImageView imageView_pop;
    RatingBar ratingBar_pop;
    public Popular_View_Holder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;
        imageView_pop = itemView.findViewById(R.id.movie_img_popular);
        ratingBar_pop = itemView.findViewById(R.id.rating_bar_pop);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
