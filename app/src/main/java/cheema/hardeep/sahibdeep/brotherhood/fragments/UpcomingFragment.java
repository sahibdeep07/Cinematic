package cheema.hardeep.sahibdeep.brotherhood.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.UpcomingAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApi;
import cheema.hardeep.sahibdeep.brotherhood.models.Movie;
import cheema.hardeep.sahibdeep.brotherhood.models.Upcoming;
import cheema.hardeep.sahibdeep.brotherhood.models.UpcomingData;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class UpcomingFragment extends Fragment {

    RecyclerView recyclerView;
    UpcomingAdapter upcomingAdapter;
    ProgressBar upcomingProgressBar;

    @Inject
    MovieApi movieApi;

    @Inject
    CompositeDisposable compositeDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Brotherhood) getActivity().getApplication()).getBrotherhoodComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        recyclerView = view.findViewById(R.id.upcomingRecyclerView);
        upcomingProgressBar = view.findViewById(R.id.upcomingProgressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        upcomingAdapter = new UpcomingAdapter();
        recyclerView.setAdapter(upcomingAdapter);
        return view;
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

    public void setIsProgressBarVisible(boolean visible) {
        upcomingProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
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
                                upcomings -> handleUpcomingMovieData(upcomings),
                                throwable -> Log.e(UpcomingFragment.class.getSimpleName(), throwable.getMessage())
                        )
        );
    }

    private void handleUpcomingMovieData(List<Upcoming> data) {
        HashMap<String, ArrayList<Movie>> mapGroupedByDate = groupMovieByWeek(data);
        List<UpcomingData> adapterData = generateAdapterDataFromMap(mapGroupedByDate);
        upcomingAdapter.updateDataSet(adapterData);

    }

    private HashMap<String, ArrayList<Movie>> groupMovieByWeek(List<Upcoming> upcomingMovie) {
        HashMap<String, ArrayList<Movie>> map = new HashMap<>();
        for (Upcoming upcoming : upcomingMovie) {
            for (Movie movie : upcoming.getResults()) {
                String weekFromDate = Utilities.getWeekFromDate(movie.getReleaseDate());
                if (map.containsKey(weekFromDate)) {
                    map.get(weekFromDate).add(movie);
                } else {
                    ArrayList<Movie> list = new ArrayList<>();
                    list.add(movie);
                    map.put(weekFromDate, list);
                }
            }
        }
        return map;
    }

    private List<UpcomingData> generateAdapterDataFromMap(HashMap<String, ArrayList<Movie>> mapGroupedByWeek) {
        ArrayList<UpcomingData> result = new ArrayList<>();
        for (String s : mapGroupedByWeek.keySet()) {
            List<Movie> values = mapGroupedByWeek.get(s);
            String minMaxDateInWeek = Utilities.getMinMaxDateInWeek(values);
            if (minMaxDateInWeek != null) {
                result.add(new UpcomingData(minMaxDateInWeek, values));
            }
        }
        return result;
    }
}
