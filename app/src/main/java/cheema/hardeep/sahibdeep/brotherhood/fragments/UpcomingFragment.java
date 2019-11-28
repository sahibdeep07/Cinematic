package cheema.hardeep.sahibdeep.brotherhood.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.MovieAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApi;
import cheema.hardeep.sahibdeep.brotherhood.models.CallerType;
import cheema.hardeep.sahibdeep.brotherhood.models.Movie;
import cheema.hardeep.sahibdeep.brotherhood.models.Upcoming;
import cheema.hardeep.sahibdeep.brotherhood.models.UpcomingData;
import cheema.hardeep.sahibdeep.brotherhood.utils.PaginationListener;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class UpcomingFragment extends Fragment {

    private static final int COLUMNS_COUNT = 2;

    @BindView(R.id.upcomingRecyclerView)
    RecyclerView upcomingRecyclerView;

    @BindView(R.id.upcomingProgressBar)
    ProgressBar upcomingProgressBar;

    @Inject
    MovieApi movieApi;

    @Inject
    CompositeDisposable compositeDisposable;

    private MovieAdapter movieAdapter;
    private int currentPage = 3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Brotherhood) getActivity().getApplication()).getBrotherhoodComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), COLUMNS_COUNT);
        upcomingRecyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(CallerType.UPCOMING);
        upcomingRecyclerView.setAdapter(movieAdapter);
        setUpPagination(gridLayoutManager);
        return view;
    }

    private void setUpPagination(GridLayoutManager gridLayoutManager) {
        upcomingRecyclerView.addOnScrollListener(new PaginationListener(gridLayoutManager) {

            @Override
            protected void loadMoreItems() {
                currentPage++;
                requestUpcomingWithPagination();
            }

        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestUpcomingMovies();
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.clear();
    }

    private void setIsProgressBarVisible(boolean visible) {
        upcomingProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        upcomingRecyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    private void requestUpcomingMovies() {
        compositeDisposable.add(
                Observable.zip(
                        movieApi.getUpcomingMovies(EN_US, 1),
                        movieApi.getUpcomingMovies(EN_US, 2),
                        movieApi.getUpcomingMovies(EN_US, 3),
                        (upcoming, upcoming2, upcoming3) -> Arrays.asList(upcoming, upcoming2, upcoming3))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> setIsProgressBarVisible(true))
                        .doOnTerminate(() -> setIsProgressBarVisible(false))
                        .subscribe(
                                this::handleUpcomingMovieData,
                                throwable -> Log.e(UpcomingFragment.class.getSimpleName(), throwable.getMessage())
                        )
        );
    }

    private void requestUpcomingWithPagination() {
        compositeDisposable.add(
                        movieApi.getUpcomingMovies(EN_US, currentPage).toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> setIsProgressBarVisible(true))
                        .doOnTerminate(() -> setIsProgressBarVisible(false))
                        .subscribe(
                                this::handleUpcomingDataWithPagination,
                                throwable -> Log.e(UpcomingFragment.class.getSimpleName(), throwable.getMessage())
                        )
        );
    }

    private void handleUpcomingMovieData(List<Upcoming> data) {
        movieAdapter.updateDataSet(filterUpcomingWithFuture(data));
    }

    private void handleUpcomingDataWithPagination(List<Upcoming> upcoming) {
        movieAdapter.addToDataSet(filterUpcomingWithFuture(upcoming));
    }

    private List<Movie> filterUpcomingWithFuture(List<Upcoming> data) {
        List<Movie> movies = new ArrayList<>();
        for (Upcoming upcoming : data) {
            for (Movie movie : upcoming.getResults()) {
                if (Utilities.getDateFromString(movie.getReleaseDate()).after(new Date())) {
                    movies.add(movie);
                }
            }
        }
        Collections.sort(movies);
        return movies;
    }
}
