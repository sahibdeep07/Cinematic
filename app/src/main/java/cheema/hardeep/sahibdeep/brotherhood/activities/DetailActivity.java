package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.CastAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.LocationService;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApi;
import cheema.hardeep.sahibdeep.brotherhood.database.UserInfoManager;
import cheema.hardeep.sahibdeep.brotherhood.models.CastDetail;
import cheema.hardeep.sahibdeep.brotherhood.models.MovieDetail;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.ERROR_MOVIE_DETAIL;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.ERROR_MOVIE_DETAIL_CAST;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.GENERAL_AUDIENCE;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.GOOGLE_MAP_MOVIE_THEATRE_URL;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.HOURS;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.MINUTES;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.RATED_R;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIXTY;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_342;

public class DetailActivity extends AppCompatActivity {

    private static final String KEY_MOVIE_ID = "movie-id";
    private static final String KEY_SHOW_BOOKING = "show-booking";
    private static final int ANIMATION_DURATION = 3000;

    @BindView(R.id.detailProgressBar)
    ProgressBar detailProgressBar;

    @BindView(R.id.imagePosterView)
    KenBurnsView imagePosterView;

    @BindView(R.id.backButton)
    ImageView backButton;

    @BindView(R.id.movieName)
    TextView movieName;

    @BindView(R.id.favoriteIcon)
    ImageView favoriteIcon;

    @BindView(R.id.releaseDateText)
    TextView releaseDateText;

    @BindView(R.id.genreText)
    TextView genreText;

    @BindView(R.id.durationText)
    TextView durationText;

    @BindView(R.id.viewingRatingText)
    TextView viewingRatingText;

    @BindView(R.id.summaryText)
    TextView summaryText;

    @BindView(R.id.castRecyclerView)
    RecyclerView castRecyclerView;

    @BindView(R.id.getTicketsBackground)
    View getTicketsBackground;

    @BindView(R.id.getTicketGroup)
    Group getTicketGroup;

    @BindView(R.id.theatreBackground)
    View theatreBackground;

    @BindView(R.id.theatreGroup)
    Group theatreGroup;

    @Inject
    MovieApi movieApi;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    LocationService locationService;

    @Inject
    UserInfoManager userInfoManager;

    private MovieDetail movieDetail;

    public static Intent createIntent(Context context, Long movieId, boolean showBooking) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_MOVIE_ID, movieId);
        intent.putExtra(KEY_SHOW_BOOKING, showBooking);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        ((Brotherhood) getApplication()).getBrotherhoodComponent().inject(this);

        RandomTransitionGenerator generator = new RandomTransitionGenerator(ANIMATION_DURATION, new LinearInterpolator());
        imagePosterView.setTransitionGenerator(generator);

        requestMovieDetails();
        attachClickListeners();

        requestLocation();
    }

    private void requestLocation() {
        if (getIntent().getBooleanExtra(KEY_SHOW_BOOKING, false)) {
            locationService.requestSingleLocation(new LocationService.LocationProvider() {
                @Override
                public void onLocation(Location location) {
                    enableLocationFeature(true);
                }

                @Override
                public void onLocationFailure() {
                    enableLocationFeature(false);
                }
            });
        } else {
            enableLocationFeature(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        compositeDisposable.clear();
    }

    private void requestMovieDetails() {
        long movieId = getIntent().getLongExtra(KEY_MOVIE_ID, -1);
        if (movieId == -1) finish();

        compositeDisposable.add(movieApi.getMovieDetails(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> detailProgressBar.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> detailProgressBar.setVisibility(View.GONE))
                .subscribe(
                        this::handleMovieDetailResponse,
                        throwable -> Log.e(MovieDetail.class.getSimpleName(), ERROR_MOVIE_DETAIL + throwable)
                )
        );

        compositeDisposable.add(
                movieApi.getMovieCastDetails(movieId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> detailProgressBar.setVisibility(View.VISIBLE))
                        .doOnTerminate(() -> detailProgressBar.setVisibility(View.GONE))
                        .subscribe(
                                this::handleMovieCastResponse,
                                throwable -> Log.e(MovieDetail.class.getSimpleName(), ERROR_MOVIE_DETAIL_CAST + throwable)
                        )
        );
    }

    private void enableLocationFeature(boolean enable) {
        getTicketGroup.setVisibility(enable ? View.VISIBLE : View.GONE);
        theatreGroup.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    private void attachClickListeners() {
        backButton.setOnClickListener(v -> finish());

        getTicketsBackground.setOnClickListener(v -> startActivity(BookTicketActivity.createIntent(this, movieDetail.getTitle())));

        theatreBackground.setOnClickListener(v -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(String.format(
                            GOOGLE_MAP_MOVIE_THEATRE_URL,
                            locationService.getCurrentLocation().getLatitude(),
                            locationService.getCurrentLocation().getLongitude())
                    )
            );
            startActivity(intent);
        });

        favoriteIcon.setOnClickListener(v -> {
            if (favoriteIcon.isSelected()) {
                userInfoManager.removeUserFavorite(movieDetail);
                updateFavoriteIcon(false);
            } else {
                userInfoManager.addUserFavorite(movieDetail);
                updateFavoriteIcon(true);
            }
        });
    }

    public void updateFavoriteIcon(boolean isSelected) {
        favoriteIcon.setSelected(isSelected);
        favoriteIcon.setImageDrawable(
                favoriteIcon
                        .getContext()
                        .getResources()
                        .getDrawable(isSelected ? R.drawable.icon_star_selected : R.drawable.icon_star_unselected)
        );
    }

    private void handleMovieDetailResponse(MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
        setUpView();
        Glide.with(this).load(Utilities.createImageUrl(movieDetail.getPosterPath(), SIZE_342)).into(imagePosterView);
        movieName.setText(movieDetail.getTitle());
        releaseDateText.setText(Utilities.convertDate(movieDetail.getReleaseDate()));
        durationText.setText(convertToHoursAndMinutes(movieDetail.getRuntime().intValue()));
        genreText.setText(Utilities.createGenreString(movieDetail.getGenres()));
        viewingRatingText.setText(movieDetail.getAdult() ? RATED_R : GENERAL_AUDIENCE);
        summaryText.setText(movieDetail.getOverview());
    }

    private void setUpView() {
        for (MovieDetail userFavorite : userInfoManager.getUserFavorites()) {
            if (movieDetail != null && userFavorite.getId().equals(movieDetail.getId())) {
                updateFavoriteIcon(true);
                return;
            }
        }
        updateFavoriteIcon(false);
    }

    private void handleMovieCastResponse(CastDetail castDetail) {
        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        castRecyclerView.setAdapter(new CastAdapter(castDetail.getCast()));
    }

    private static String convertToHoursAndMinutes(int time) {
        int hours = time / SIXTY;
        int minutes = time % SIXTY;
        return hours + HOURS + minutes + MINUTES;
    }
}
