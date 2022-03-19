package com.example.movieflix.utils;

import com.example.movieflix.models.MovieModel;
import com.example.movieflix.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    //search for movies
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );

    /*
    https://api.themoviedb.org/3/movie/popular
    ?api_key=7e197fed41ae6c61f69c639e9ce85aa4&page=1
    Popular movie search
     */
    @GET("3/movie/popular")
    Call<MovieSearchResponse> getPopular(
            @Query("api_key") String key,
            @Query("page") int page
    );











    /*
    https://api.themoviedb.org/3/movie/550?api_key=7e197fed41ae6c61f69c639e9ce85aa4
    movie_id 550 is for fight club
     */

    @GET("3/movie/{movie_id}?")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );


}
