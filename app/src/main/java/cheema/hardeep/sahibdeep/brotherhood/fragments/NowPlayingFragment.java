package cheema.hardeep.sahibdeep.brotherhood.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.NowPlayingAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApi;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;
import cheema.hardeep.sahibdeep.brotherhood.models.Movie;
import cheema.hardeep.sahibdeep.brotherhood.models.NowPlaying;
import cheema.hardeep.sahibdeep.brotherhood.models.TopRated;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;


public class NowPlayingFragment extends Fragment {

    @Inject
    MovieApi movieApi;

    @Inject
    CompositeDisposable compositeDisposable;

    private RecyclerView nowPlayingRV;
    private ProgressBar nowPlayingProgressBar;
    private NowPlayingAdapter nowPlayingAdapter = new NowPlayingAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Brotherhood) getActivity().getApplication()).getBrotherhoodComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        nowPlayingRV = view.findViewById(R.id.nowPlayingRV);
        nowPlayingProgressBar = view.findViewById(R.id.nowPlayingProgressBar);
        nowPlayingRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        nowPlayingRV.setAdapter(nowPlayingAdapter);
        requestNowPlayingMovies();
        return view;
    }

    private void requestNowPlayingMovies() {
        compositeDisposable.add(
                movieApi.getNowPlaying(EN_US, 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> setIsProgressBarVisible(true))
                        .doOnTerminate(() -> setIsProgressBarVisible(false))
                        .subscribe(
                                this::handleNowPlayingResponse,
                                throwable -> Log.e(RecommendedFragment.class.getSimpleName(), "onFailure: Error in getting the Top Rated" + throwable)
                        )
        );
    }

    private void setIsProgressBarVisible(boolean visible) {
        nowPlayingProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        nowPlayingRV.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    private void handleNowPlayingResponse(NowPlaying nowPlaying) {
        List<Movie> nowPlayingList = nowPlaying.getResults();
        nowPlayingAdapter.updateDataSet(nowPlayingList);
    }
}
