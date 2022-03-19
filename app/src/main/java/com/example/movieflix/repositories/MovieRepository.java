package com.example.movieflix.repositories;

import android.graphics.Movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieflix.models.MovieModel;
import com.example.movieflix.request.MovieApiClient;

import java.util.List;

public class MovieRepository {


    private static MovieRepository instance;

    //This class is acting as repositories
    private MovieApiClient movieApiClient;

    private String mQuery;
    private int mPageNumber;



    public static MovieRepository getInstance() {
        if(instance==null){
            instance = new MovieRepository();
        }
        return instance;
    }
    private MovieRepository(){
       movieApiClient = MovieApiClient.getInstance();
    }


    public LiveData<List<MovieModel>> getMovies(){
        return movieApiClient.getMovies();
    }


    //Popular movie
    public LiveData<List<MovieModel>> getPop(){
        return movieApiClient.getMoviesPop();
    }


    // 2- calling the method in repo
    public void searchMovieApi(String query, int pageNumber){
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesApi(query, pageNumber);
    }

    //2 - calling the popular movie in repo
    public void searchMoviePop(int pageNumber){
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesPop(pageNumber);
    }



    public void searchNextPage(){
        searchMovieApi(mQuery,mPageNumber+1);
    }


}
