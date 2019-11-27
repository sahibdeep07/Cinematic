package cheema.hardeep.sahibdeep.brotherhood.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.MovieAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApi;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;
import cheema.hardeep.sahibdeep.brotherhood.models.Actor;
import cheema.hardeep.sahibdeep.brotherhood.models.CallerType;
import cheema.hardeep.sahibdeep.brotherhood.models.Cast;
import cheema.hardeep.sahibdeep.brotherhood.models.Genre;
import cheema.hardeep.sahibdeep.brotherhood.models.Movie;
import cheema.hardeep.sahibdeep.brotherhood.models.MovieDetail;
import cheema.hardeep.sahibdeep.brotherhood.models.TopRated;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class RecommendedFragment extends Fragment {

    @BindView(R.id.progressBar)
    ProgressBar recommendedProgressBar;

    @BindView(R.id.yourGenreRV)
    RecyclerView genresRecyclerView;

    @BindView(R.id.yourActorsRV)
    RecyclerView actorsRecyclerView;

    @BindView(R.id.yourFavouriteRV)
    RecyclerView favouritesRecyclerView;

    @BindView(R.id.homeScrollView)
    ScrollView homeScrollView;

    @BindView(R.id.noFavoritesMessage)
    TextView noFavoriteMessage;

    private static final String NO_GENRE = "No Genres Selected Showing all Results";
    private static final String NO_ACTOR = "No Actors Selected Showing all Results";

    private MovieAdapter genreAdapter = new MovieAdapter(CallerType.RECOMMENDED);
    private MovieAdapter actorAdapter = new MovieAdapter(CallerType.RECOMMENDED);
    private MovieAdapter favoriteAdapter = new MovieAdapter(CallerType.RECOMMENDED);

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    MovieApi movieApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Brotherhood) getActivity().getApplication()).getBrotherhoodComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended, container, false);
        ButterKnife.bind(this, view);

        setRecyclerView(genresRecyclerView, genreAdapter);
        setRecyclerView(actorsRecyclerView, actorAdapter);
        setRecyclerView(favouritesRecyclerView, favoriteAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestTopRatedMovies();
        requestFavorites();
    }

    private void requestFavorites() {
        List<Movie> favoriteMovies = convertMovieDetailToMovie(SharedPreferenceProvider.getUserFavorites(getContext()));
        if(favoriteMovies.isEmpty()) {
            favouritesRecyclerView.setVisibility(View.GONE);
            noFavoriteMessage.setVisibility(View.VISIBLE);
        } else {
            noFavoriteMessage.setVisibility(View.GONE);
            favouritesRecyclerView.setVisibility(View.VISIBLE);
            favoriteAdapter.updateDataSet(favoriteMovies);
        }
    }

    private void setIsProgressBarVisible(boolean visible) {
        recommendedProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        homeScrollView.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    private void requestTopRatedMovies() {
        compositeDisposable.add(
                movieApi.getTopRated(EN_US)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> setIsProgressBarVisible(true))
                        .subscribe(
                                this::handleTopRatedResponse,
                                throwable -> Log.e(RecommendedFragment.class.getSimpleName(), "onFailure: Error in getting the Top Rated" + throwable)
                        )
        );
    }

    private void handleTopRatedResponse(TopRated topRated) {
        List<Movie> topRatedList = topRated.getResults();
        filterMoviesBasedOnUserActors(topRatedList, SharedPreferenceProvider.getUserActors(getContext()));
        filterMoviesBasedOnUserGenres(topRatedList, SharedPreferenceProvider.getUserGenres(getContext()));
    }

    private void setRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void filterMoviesBasedOnUserActors(List<Movie> topRatedMovies, List<Actor> userActors) {
        compositeDisposable.add(
                Observable.fromIterable(topRatedMovies)
                        .flatMap(movie -> movieApi.getMovieCastDetails(movie.getId()), Movie::setCastDetails)
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> setIsProgressBarVisible(true))
                        .doFinally(() -> setIsProgressBarVisible(false))
                        .subscribe(
                                movies -> setMoviesToActorRecyclerView(movies, userActors),
                                throwable -> {
                                    Log.e(RecommendedFragment.class.getSimpleName(), "onFailure: Error Fetch Actors" + throwable);
                                    actorAdapter.updateDataSet(topRatedMovies);
                                })
        );
    }

    private void filterMoviesBasedOnUserGenres(List<Movie> topRatedMovies, List<Genre> genreList) {
        HashSet<Movie> genreMovieHash = new HashSet<>();
        for (Movie movie : topRatedMovies) {
            for (long genreID : movie.getGenreIds()) {
                for (Genre genre : genreList) {
                    if (genre.getId().equals(genreID)) {
                        genreMovieHash.add(movie);
                    }
                }
            }
        }
        Toast.makeText(getContext(), NO_GENRE, Toast.LENGTH_SHORT).show();
        genreAdapter.updateDataSet(genreMovieHash.isEmpty()? topRatedMovies: new ArrayList<>(genreMovieHash));
    }

    private void setMoviesToActorRecyclerView(List<Movie> topRatedMovies, List<Actor> userActors) {
        HashSet<Movie> actorsMovieHash = new HashSet<>();
        for (Movie movie : topRatedMovies) {
            for (Actor userActor : userActors) {
                for (Cast cast : movie.getCastDetails().getCast()) {
                    if (userActor.getName().toLowerCase().equals(cast.getName().toLowerCase())) {
                        actorsMovieHash.add(movie);
                    }
                }
            }
        }
        Toast.makeText(getContext(), NO_ACTOR, Toast.LENGTH_SHORT).show();
        actorAdapter.updateDataSet(actorsMovieHash.isEmpty() ? topRatedMovies: new ArrayList<>(actorsMovieHash));
    }

    private List<Movie> convertMovieDetailToMovie(List<MovieDetail> movieDetails) {
        List<Movie> movies = new ArrayList<>();
        for (MovieDetail movieDetail : movieDetails) {
            movies.add(movieDetail.convertToMovie());
        }
        return movies;
    }
}
