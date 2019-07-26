package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.GenreAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApiProvider;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;
import cheema.hardeep.sahibdeep.brotherhood.models.Genre;
import cheema.hardeep.sahibdeep.brotherhood.models.GenreResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class GenreActivity extends AppCompatActivity {
    private static final String TITLE_BAR_NAME = "Select Genre";

    RecyclerView recyclerView;
    View genre,actors;
    GenreAdapter genreAdapter;
    ProgressBar progressBar;

    public static Intent createIntent(Context context) {
        return new Intent(context, GenreActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        setTitle(TITLE_BAR_NAME);

        findAndInitializeViews();
        setProgressBar();
        setClickListeners();
        setupRecyclerView();
        requestGenres();
    }

    private void findAndInitializeViews() {
        genre = findViewById(R.id.genreBackground);
        actors = findViewById((R.id.actorsBackground));
        recyclerView = findViewById(R.id.genreRecyclerView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void setClickListeners() {
        genre.setOnClickListener(v -> handleNameClick());
        actors.setOnClickListener(v -> handleActorsClick());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        genreAdapter = new GenreAdapter();
        recyclerView.setAdapter(genreAdapter);
    }

    private void requestGenres() {
        MovieApiProvider.getMovieApi().getGenre(EN_US).enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                GenreResponse genreResponse = response.body();
                handleGenreResponse(genreResponse);
            }

            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                Log.e("genreRequest", "onFailure: Error in getting the genres");
                Toast.makeText(GenreActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleGenreResponse(GenreResponse genreResponse) {
        for (Genre genre : genreResponse.getGenres()) { genre.setIcon(); }
        genreAdapter.update(genreResponse.getGenres());
    }

    public void handleNameClick(){
        finish();
    }

    public void handleActorsClick(){
        ArrayList<String> sharedPreferenceGenreList = new ArrayList<>();
        for(int i=0; i<genreAdapter.getUpdatedList().size(); i++) {
            Genre genre = genreAdapter.getUpdatedList().get(i);
            if(genre.isSelected()){
                sharedPreferenceGenreList.add(genre.getName());
            }
        }
        if(!sharedPreferenceGenreList.isEmpty()) {
            SharedPreferenceProvider.saveUserGenres(this, sharedPreferenceGenreList);
            startActivity(ActorActivity.createIntent(this));
            finish();
        }
        else{
            Toast.makeText(this, "Please select a genre", Toast.LENGTH_SHORT).show();
        }
    }
}
