package cheema.hardeep.sahibdeep.brotherhood.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.activities.DetailActivity;
import cheema.hardeep.sahibdeep.brotherhood.models.CallerType;
import cheema.hardeep.sahibdeep.brotherhood.models.Movie;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.ROUNDED_CORNER_UPCOMING;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_342;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.UpcomingMovieViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private CallerType callerType;

    public MovieAdapter(CallerType callerType) {
        this.callerType = callerType;
    }

    public void updateDataSet(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void addToDataSet(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UpcomingMovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item, viewGroup, false);
        return new UpcomingMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingMovieViewHolder upcomingMovieViewHolder, int i) {
        Movie movie = movies.get(i);
        upcomingMovieViewHolder.movieName.setText(movie.getTitle());

        if (movie.getVoteAverage() != null && !callerType.isUpcoming()) {
            upcomingMovieViewHolder.movieRating.setText(String.valueOf(movie.getVoteAverage()));
        } else {
            upcomingMovieViewHolder.movieRating.setVisibility(View.GONE);
        }

        loadImageWithGlide(movie.getBackdropPath(), upcomingMovieViewHolder.movieImage);

        upcomingMovieViewHolder.itemView.setOnClickListener(v ->
                v.getContext().startActivity(DetailActivity.createIntent(v.getContext(), movie.getId(), callerType.isNowPlaying())));
    }

    private void loadImageWithGlide(String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(ROUNDED_CORNER_UPCOMING));
        Glide.with(imageView.getContext())
                .load(Utilities.createImageUrl(url, SIZE_342))
                .apply(requestOptions)
                .placeholder(R.drawable.curved_bottom_right)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class UpcomingMovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movieImage)
        ImageView movieImage;

        @BindView(R.id.movieRating)
        TextView movieRating;

        @BindView(R.id.movieName)
        TextView movieName;

        UpcomingMovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
