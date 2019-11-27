package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.GenreAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApi;
import cheema.hardeep.sahibdeep.brotherhood.database.UserInfoManager;
import cheema.hardeep.sahibdeep.brotherhood.models.Genre;
import cheema.hardeep.sahibdeep.brotherhood.models.GenreResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class GenreActivity extends AppCompatActivity {

    private static final String SAVE = "Save";
    private static final String ACTORS = "Actors";
    private static final float PERCENT_30 = 0.30f;
    private static final float PERCENT_0 = 0.00f;

    @BindView(R.id.genresRecyclerView)
    RecyclerView genresRecyclerView;

    @BindView(R.id.moveToNameBackground)
    View moveToNameBackground;

    @BindView(R.id.moveToActorOrSaveBackground)
    View moveToActorOrSaveBackground;

    @BindView(R.id.moveToActorsOrSave)
    TextView moveToActorOrSav;

    @BindView(R.id.genreProgressBar)
    ProgressBar genreProgressBar;

    @BindView(R.id.genreGuideLine)
    Guideline genreGuideline;

    @BindView(R.id.nameGroup)
    Group nameGroup;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    MovieApi movieApi;

    @Inject
    UserInfoManager userInfoManager;

    private GenreAdapter genreAdapter;

    public static Intent createIntent(Context context) {
        return new Intent(context, GenreActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        getSupportActionBar().hide();
        ((Brotherhood) getApplication()).getBrotherhoodComponent().inject(this);
        ButterKnife.bind(this);

        setupBottomButtonsAndGuideline();
        setClickListeners();
        setupRecyclerView();
        requestGenres();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compositeDisposable.clear();
    }

    private void setupBottomButtonsAndGuideline() {
        boolean isFirstLaunch = userInfoManager.isFirstLaunch();
        nameGroup.setVisibility(isFirstLaunch ? View.VISIBLE : View.GONE);
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
        compositeDisposable.add(
                movieApi.getGenre(EN_US)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> setIsProgressBarVisible(true))
                        .doOnTerminate(() -> setIsProgressBarVisible(false))
                        .subscribe(
                                this::handleGenreResponse,
                                throwable -> Log.e(GenreActivity.class.getSimpleName(), "onFailure: Error in getting the genres" + throwable)
                        )
        );
    }

    private void handleGenreResponse(GenreResponse genreResponse) {
        List<Genre> userGenres = userInfoManager.getUserGenres();
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
        userInfoManager.saveUserGenres(selectedGenreList);
        handleTransition();
    }

    private void handleTransition() {
        if (userInfoManager.isFirstLaunch()) {
            startActivity(ActorActivity.createIntent(this));
        } else {
            finish();
        }
    }
}
