package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.GenreAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApiProvider;
import cheema.hardeep.sahibdeep.brotherhood.models.Genre;
import cheema.hardeep.sahibdeep.brotherhood.models.GenreResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class GenreActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    View genre,actors;
    GenreAdapter genreAdapter;

    public static Intent createIntent(Context context) {
        return new Intent(context, GenreActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        genre = findViewById(R.id.genreBackground);
        actors = findViewById((R.id.actorsBackground));
        genre.setOnClickListener(v -> handleGenreClick());
        actors.setOnClickListener(v -> handleActorsClick());
        recyclerView = findViewById(R.id.genreRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        genreAdapter = new GenreAdapter();
        recyclerView.setAdapter(genreAdapter);
        requestGenres();
    }

    private void requestGenres() {
        MovieApiProvider.getMovieApi().getGenre(EN_US).enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                GenreResponse genreResponse = response.body();
                handleGenreResponse(genreResponse);
            }

            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {

            }
        });
    }

    private void handleGenreResponse(GenreResponse genreResponse) {
        //Attach Icons
        for (Genre genre : genreResponse.getGenres()) { genre.setIcon(); }
        genreAdapter.update(genreResponse.getGenres());
    }

    public void handleGenreClick(){
        startActivity(NameActivity.createIntent(this));
        finish();
    }
    public void handleActorsClick(){
        startActivity(ActorActivity.createIntent(this));
        finish();
    }
}
