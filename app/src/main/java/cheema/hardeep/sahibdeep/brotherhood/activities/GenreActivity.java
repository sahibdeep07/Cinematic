package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.GenreAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApiProvider;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;
import cheema.hardeep.sahibdeep.brotherhood.models.Genre;
import cheema.hardeep.sahibdeep.brotherhood.models.GenreResponse;
import cheema.hardeep.sahibdeep.brotherhood.models.GenreScreenType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class GenreActivity extends AppCompatActivity {

    private static final String KEY_GENRE_TYPE = "moveToName-type";
    private static final String SAVE = "Save";
    private static final String ACTORS = "Actors";
    private static final float PERCENT_30 = 0.30f;
    private static final float PERCENT_0 = 0.00f;


    RecyclerView recyclerView;
    View moveToName, moveToActorOrSave;
    TextView genreNextButtonText;
    GenreAdapter genreAdapter;
    ProgressBar progressBar;
    Guideline guideline;

    GenreScreenType genreScreenType;

    public static Intent createIntent(Context context, GenreScreenType genreScreenType) {
        Intent intent = new Intent(context, GenreActivity.class);
        intent.putExtra(KEY_GENRE_TYPE, genreScreenType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        getSupportActionBar().hide();

        findAndInitializeViews();
        handleGenreTypeIntent();
        setIsProgressBarVisible(true);
        setClickListeners();
        setupRecyclerView();
        requestGenres();
    }

    private void findAndInitializeViews() {
        moveToName = findViewById(R.id.moveToName);
        moveToActorOrSave = findViewById((R.id.moveToActorOrSave));
        recyclerView = findViewById(R.id.genreRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        guideline = findViewById(R.id.genreGuideLine);
        genreNextButtonText = findViewById(R.id.genreNextButtonText);
    }

    private void handleGenreTypeIntent() {
        Intent intent = getIntent();
        genreScreenType = (GenreScreenType) intent.getSerializableExtra(KEY_GENRE_TYPE);
        switch (genreScreenType) {
            case FIRST_SCREEN:
                guideline.setGuidelinePercent(PERCENT_30);
                genreNextButtonText.setText(ACTORS);
                break;
            case USER_SCREEN:
                guideline.setGuidelinePercent(PERCENT_0);
                genreNextButtonText.setText(SAVE);
                break;
        }
    }

    public void setIsProgressBarVisible(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    private void setClickListeners() {
        moveToName.setOnClickListener(v -> handleNameClick());
        moveToActorOrSave.setOnClickListener(v -> handleActorsAndSaveClick());
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
                setIsProgressBarVisible(false);
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
        List<Genre> userGenres = SharedPreferenceProvider.getUserGenres(this);
        for (Genre genre : genreResponse.getGenres()) {
            genre.setIcon();

            //Pre Select Genre if already in SharedPreferences
            if (!userGenres.isEmpty()) {
                for (Genre userGenre : userGenres) {
                    if (genre.getId().equals(userGenre.getId())) {
                        genre.setSelected(true);
                    }
                }
            }
        }
        genreAdapter.update(genreResponse.getGenres());
    }

    public void handleNameClick() {
        finish();
    }

    public void handleActorsAndSaveClick() {
        ArrayList<Genre> selectedGenres = getSelectedGenreList();
        saveSelectedGenreList(selectedGenres);
    }

    private ArrayList<Genre> getSelectedGenreList() {
        ArrayList<Genre> result = new ArrayList<>();
        for (Genre genre : genreAdapter.getUpdatedList()) {
            if (genre.isSelected()) {
                result.add(genre);
            }
        }
        return result;
    }

    private void saveSelectedGenreList(ArrayList<Genre> selectedGenreList) {
        SharedPreferenceProvider.saveUserGenres(this, selectedGenreList);
        handleTransition();
    }

    private void handleTransition() {
        switch (genreScreenType) {
            case FIRST_SCREEN:
                startActivity(ActorActivity.createIntent(this));
                break;
            case USER_SCREEN:
                finish();
                break;
        }
    }
}
