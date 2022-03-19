package com.example.movieflix.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieflix.AppExecutors;
import com.example.movieflix.models.MovieModel;
import com.example.movieflix.repositories.MovieRepository;
import com.example.movieflix.response.MovieSearchResponse;
import com.example.movieflix.utils.Credentials;
import com.example.movieflix.utils.MovieApi;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

//this class will work as a bridge with the retrofit data and live data
public class MovieApiClient {
    //This is a live data for search
    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    //making Global runnable request
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    //Livedata for popular movies
    private MutableLiveData<List<MovieModel>> mMoviesPop;
    //popular runnable
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePop;




    public static MovieApiClient getInstance(){
        if(instance == null){
            instance = new MovieApiClient();
        }
        return instance;
    }
    //to store live data
    private MovieApiClient(){
        mMovies = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
    }


    //Get movies live data
    public LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }

    //Popular movies liveData
    public LiveData<List<MovieModel>> getMoviesPop(){
        return mMoviesPop;
    }

    // 1 - This method going to call through the classes
    public void searchMoviesApi(String query, int pageNumber){


        if (retrieveMoviesRunnable != null){
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);



        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);



        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // to cancel retrofit call
                myHandler.cancel(true);
            }
        },3000, TimeUnit.MILLISECONDS);
    }

    public void searchMoviesPop(int pageNumber){


        if (retrieveMoviesRunnablePop != null){
            retrieveMoviesRunnablePop = null;
        }

        retrieveMoviesRunnablePop = new RetrieveMoviesRunnablePop(pageNumber);



        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnablePop);



        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // to cancel retrofit call
                myHandler2.cancel(true);
            }
        },1000, TimeUnit.MILLISECONDS);
    }



    //Retrieving data from Rest APi by runnable class
    // 2 types of queries; search and movie id
    private class RetrieveMoviesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;


        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting the response object from the internet using retrofit
            try{
                Response response = getMovies(query,pageNumber).execute();

                if (cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if(pageNumber == 1){
                        //Sending data to live data
                        // postValue used for background thread
                        //setValue not for background thread
                        mMovies.postValue(list);
                    }else{
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                }else{
                    String error = response.errorBody().string();
                    Log.v("Tag","Error" +error);
                    mMovies.postValue(null);
                }

            } catch(IOException e){
                e.printStackTrace();
                mMovies.postValue(null);
            }

        }
        //Search method/query
        private Call<MovieSearchResponse>getMovies(String query, int pageNumber){
            return Servicey.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );
        }
        private void cancelRequest(){
            Log.v("Tag","Cancelling Search Request");
            cancelRequest = true;
        }
    }


    //Retrieving data from Rest APi by runnable popular class
    // 1 type of data pageNumber
    private class RetrieveMoviesRunnablePop implements Runnable{

        private int pageNumber;
        boolean cancelRequest;


        public RetrieveMoviesRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting the response object from the internet using retrofit
            try{
                Response response2 = getPop(pageNumber).execute();

                if (cancelRequest){
                    return;
                }
                if(response2.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if(pageNumber == 1){
                        //Sending data to live data
                        // postValue used for background thread
                        //setValue not for background thread
                        mMoviesPop.postValue(list);
                    }else{
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
                    }
                }else{
                    String error = response2.errorBody().string();
                    Log.v("Tag","Error" +error);
                    mMoviesPop.postValue(null);
                }

            } catch(IOException e){
                e.printStackTrace();
                mMoviesPop.postValue(null);
            }

        }
        //Search method/query
        private Call<MovieSearchResponse>getPop(int pageNumber){
            return Servicey.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );
        }
        private void cancelRequest(){
            Log.v("Tag","Cancelling Search Request");
            cancelRequest = true;
        }
    }

}
