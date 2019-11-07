package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
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

import javax.inject.Inject;

import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.GenreAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApi;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;
import cheema.hardeep.sahibdeep.brotherhood.models.Genre;
import cheema.hardeep.sahibdeep.brotherhood.models.GenreResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class GenreActivity extends AppCompatActivity {

    private static final String SAVE = "Save";
    private static final String ACTORS = "Actors";
    private static final float PERCENT_30 = 0.30f;
    private static final float PERCENT_0 = 0.00f;

    RecyclerView genresRecyclerView;
    View moveToNameBackground, moveToActorOrSaveBackground;
    TextView moveToActorOrSav;
    GenreAdapter genreAdapter;
    ProgressBar genreProgressBar;
    Guideline genreGuideline;
    Group nameGroup;

    @Inject
    MovieApi movieApi;

    public static Intent createIntent(Context context) {
        return new Intent(context, GenreActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        getSupportActionBar().hide();
        ((Brotherhood) getApplication()).getBrotherhoodComponent().inject(this);

        findAndInitializeViews();
        setupBottomButtonsAndGuideline();
        setIsProgressBarVisible(true);
        setClickListeners();
        setupRecyclerView();
        requestGenres();
    }

    private void findAndInitializeViews() {
        moveToNameBackground = findViewById(R.id.moveToNameBackground);
        moveToActorOrSaveBackground = findViewById((R.id.moveToActorOrSaveBackground));
        genresRecyclerView = findViewById(R.id.genresRecyclerView);
        genreProgressBar = findViewById(R.id.genreProgressBar);
        genreGuideline = findViewById(R.id.genreGuideLine);
        moveToActorOrSav = findViewById(R.id.moveToActorsOrSave);
        nameGroup = findViewById(R.id.nameGroup);
    }

    private void setupBottomButtonsAndGuideline() {
        boolean isFirstLaunch = SharedPreferenceProvider.isFirstLaunch(this);
        nameGroup.setVisibility(isFirstLaunch ? View.VISIBLE: View.GONE);
        genreGuideline.setGuidelinePercent(isFirstLaunch ? PERCENT_30 : PERCENT_0);
        moveToActorOrSav.setText(isFirstLaunch ? ACTORS : SAVE);
    }

    public void setIsProgressBarVisible(boolean visible) {
        genreProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        genresRecyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    private void setClickListeners() {
        moveToNameBackground.setOnClickListener(v -> handleNameClick());
        moveToActorOrSaveBackground.setOnClickListener(v -> handleActorsAndSaveClick());
    }

    private void setupRecyclerView() {
        genresRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        genreAdapter = new GenreAdapter(true);
        genresRecyclerView.setAdapter(genreAdapter);
    }

    private void requestGenres() {
        movieApi.getGenre(EN_US).enqueue(new Callback<GenreResponse>() {
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
        if(SharedPreferenceProvider.isFirstLaunch(this)) {
            startActivity(ActorActivity.createIntent(this));
        } else {
            finish();
        }
    }
}
