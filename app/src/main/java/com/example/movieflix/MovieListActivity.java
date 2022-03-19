package com.example.movieflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.movieflix.adapters.MovieRecyclerView;
import com.example.movieflix.adapters.OnMovieListener;
import com.example.movieflix.models.MovieModel;
import com.example.movieflix.request.Servicey;
import com.example.movieflix.response.MovieSearchResponse;
import com.example.movieflix.utils.Credentials;
import com.example.movieflix.utils.MovieApi;
import com.example.movieflix.viewmodels.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    //RecyclerView Widget
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;


    //ViewModel
    private MovieListViewModel movieListViewModel;


    //Popular movie
    boolean isPopular = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //SearchView
        SetupSearchView();

        recyclerView = findViewById(R.id.recyclerView);

        //instantiating viewmodel here
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        ConfigureRecyclerView();
        //Calling the observers
        ObserveAnyChange();
        //searchMovieApi("fast",1);
        ObservePopularMovies();
        //Getting data for popular movies
        movieListViewModel.searchMoviePop(1);

    }

    private void ObservePopularMovies() {
        movieListViewModel.getPop().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                //To observe any data change
                if (movieModels != null){
                    for (MovieModel movieModel: movieModels){
                        //getting the data in the log
                        //Log.v("Tag","onChanged: " +movieModel.getTitle());
                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }


            }
        });







    }


    //Observing any data change
    private void ObserveAnyChange(){

        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                //To observe any data change
                if (movieModels != null){
                    for (MovieModel movieModel: movieModels){
                        //getting the data in the log
                        //Log.v("Tag","onChanged: " +movieModel.getTitle());

                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }


            }
        });
    }

    /* 4 - Calling method in mainactivity
    private void searchMovieApi(String query, int pageNumber){
        movieListViewModel.searchMovieApi(query,pageNumber);
    }
     */

    // 5 -Initalise recyclerview and adding data to it
    private void ConfigureRecyclerView(){

        movieRecyclerAdapter = new MovieRecyclerView(this);
        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Recycler view pagination
        //Loading next page api response
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)){
                    // scrolling vertically by displaying the results
                    movieListViewModel.searchNextPage();
                }
            }
        });





    }

    @Override
    public void onMovieClick(int position) {
        //Toast.makeText(this, "Position: "+position , Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie",movieRecyclerAdapter.getSelectedMovie(position));
        startActivity(intent);




    }

    @Override
    public void onCategoryClick(String category) {

    }

    //Getting data from searchview, query and API to to get the result
    private void SetupSearchView() {
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        // The search string obtained from searchview
                        query,
                        1
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        // if user clicks on search movie popular becomes false
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPopular = false;
            }
        });

    }


    /* For earlier testing purpose
    private void GetRetrofitResponse() {
        //getting retrofit response from the server
        MovieApi movieApi = Servicey.getMovieApi();

        Call<MovieSearchResponse> responseCall = movieApi
                .searchMovie(
                        Credentials.API_KEY,
                        "Jack Reacher", 1);

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {

                if (response.code()==200){
                    Log.v("Tag","The response: " +response.body().toString());

                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());

                    for (MovieModel movie: movies){
                        Log.v("Tag","The movie title: " +movie.getTitle());
                    }
                }
                else{
                    try {
                        Log.v("Tag","Error: "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

            }
        });


    }

    /*
    Search with ID
    https://api.themoviedb.org/3/movie/550?api_key=7e197fed41ae6c61f69c639e9ce85aa4
     */
    /*
    private void GetRetrofitResponseAccordingToID(){
        MovieApi movieApi = Servicey.getMovieApi();
        Call<MovieModel> responseCall = movieApi.getMovie(550,
                Credentials.API_KEY);

        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code()==200){
                    MovieModel movie = response.body();
                    Log.v("Tag","The response: " +movie.getTitle());
                }
                else{
                    try{
                        Log.v("Tag","Error: " +response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }
    */



}