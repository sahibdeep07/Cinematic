package cheema.hardeep.sahibdeep.brotherhood.fragments;

import android.annotation.SuppressLint;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.UpcomingAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApiProvider;
import cheema.hardeep.sahibdeep.brotherhood.models.Dates;
import cheema.hardeep.sahibdeep.brotherhood.models.Movie;
import cheema.hardeep.sahibdeep.brotherhood.models.Upcoming;
import cheema.hardeep.sahibdeep.brotherhood.models.UpcomingData;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class UpcomingFragment extends Fragment {

    public static final int PAGES = 3;
    RecyclerView recyclerView;
    UpcomingAdapter upcomingAdapter;
    ArrayList<Upcoming> data = new ArrayList<>();
    boolean[] requestTracker = new boolean[PAGES];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        recyclerView = view.findViewById(R.id.upcomingRecyclerView);
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

    private void requestUpcomingMovies() {
        for (int i = 0; i < PAGES; i++) {
            requestTracker[i] = false;
            MovieApiProvider.getMovieApi().getUpcomingMovies(EN_US, i + 1).enqueue(getCallback(i));
        }
    }

    private Callback<Upcoming> getCallback(int i) {
        return new Callback<Upcoming>() {
            @Override
            public void onResponse(Call<Upcoming> call, Response<Upcoming> response) {
                data.add(response.body());
                requestTracker[i] = true;
                if (allRequestsRecieved()) handleUpcomingMovieData();
            }

            @Override
            public void onFailure(Call<Upcoming> call, Throwable t) {
                requestTracker[i] = true;
                Log.d(UpcomingFragment.class.getSimpleName(), t.getMessage());
            }
        };
    }

    private void handleUpcomingMovieData() {
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

    private boolean allRequestsRecieved() {
        for (boolean b : requestTracker) if (!b) return false;
        return true;
    }
}
