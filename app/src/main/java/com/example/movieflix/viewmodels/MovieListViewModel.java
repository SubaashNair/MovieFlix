package com.example.movieflix.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieflix.models.MovieModel;
import com.example.movieflix.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {
    /*
    this class is used for viewmodel
    MutableLiveData is a subclass*/

    //Creating an instance of repo
    private MovieRepository movieRepository;


    //constructor
    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }



    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }

    //Popular movie
    public LiveData<List<MovieModel>> getPop(){
        return movieRepository.getPop();
    }


    // 3 - Calling method in viewModel
    public void searchMovieApi(String query, int pageNumber){
        movieRepository.searchMovieApi(query, pageNumber);
    }

    //Searching for popular movies
    public void searchMoviePop(int pageNumber){
        movieRepository.searchMoviePop(pageNumber);
    }

    public void searchNextPage(){
        movieRepository.searchNextPage();
    }



}
