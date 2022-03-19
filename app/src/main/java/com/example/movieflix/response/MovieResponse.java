package com.example.movieflix.response;

import com.example.movieflix.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// This class is for single movie request by the user.
public class MovieResponse {
    // To find the movie object
    @SerializedName("results")
    @Expose()
    private MovieModel movie;
    public MovieModel getMovie(){
        return movie;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }
}
