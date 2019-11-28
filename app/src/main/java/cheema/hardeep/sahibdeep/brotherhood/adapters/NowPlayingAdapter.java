package cheema.hardeep.sahibdeep.brotherhood.adapters;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.activities.BookTicketActivity;
import cheema.hardeep.sahibdeep.brotherhood.activities.DetailActivity;
import cheema.hardeep.sahibdeep.brotherhood.api.LocationService;
import cheema.hardeep.sahibdeep.brotherhood.models.Genre;
import cheema.hardeep.sahibdeep.brotherhood.models.Movie;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.GOOGLE_MAP_MOVIE_THEATRE_URL;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.LOCATION_NOT_ENABLED;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.ROUNDED_CORNER_UPCOMING;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIXTY;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_342;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_500;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_780;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_ORIGINAL;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.NowPlayingViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private LocationService locationService;

    public NowPlayingAdapter(LocationService locationService) {
        this.locationService = locationService;
    }

    public void updateDataSet(List<Movie> nowPlayingMovies) {
        movies.clear();
        movies.addAll(nowPlayingMovies);
        notifyDataSetChanged();
    }

    public void addToDataSet(List<Movie> nowPlayingMovies) {
        movies.addAll(nowPlayingMovies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NowPlayingAdapter.NowPlayingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.now_playing_item, viewGroup, false);
        return new NowPlayingAdapter.NowPlayingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NowPlayingViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.movieName.setText(movie.getTitle());
        holder.movieRating.setText(String.valueOf(movie.getVoteAverage()));
        loadImageWithGlide(movie.getBackdropPath(), holder.movieImage);
        holder.movieGenre.setText(movie.getGenreNames());

        holder.itemView.setOnClickListener(v ->
                v.getContext().startActivity(DetailActivity.createIntent(v.getContext(), movie.getId(), true)));

        holder.ticketButton.setOnClickListener(v -> v.getContext().startActivity(BookTicketActivity.createIntent(v.getContext(), movie.getTitle())));

        holder.theatreButton.setOnClickListener(v -> locationService.requestSingleLocation(new LocationService.LocationProvider() {
            @Override
            public void onLocation(Location location) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(String.format(
                                GOOGLE_MAP_MOVIE_THEATRE_URL,
                                location.getLatitude(),
                                location.getLongitude())
                        )
                );
                v.getContext().startActivity(intent);
            }

            @Override
            public void onLocationFailure() {
                Toast.makeText(v.getContext(), LOCATION_NOT_ENABLED, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void loadImageWithGlide(String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(SIXTY));
        Glide.with(imageView.getContext())
                .load(Utilities.createImageUrl(url, SIZE_ORIGINAL))
                .apply(requestOptions)
                .placeholder(R.drawable.curved_bottom_right)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class NowPlayingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nowPlayingImageView)
        ImageView movieImage;

        @BindView(R.id.nowPlayingRating)
        TextView movieRating;

        @BindView(R.id.nowPlayingMovieName)
        TextView movieName;

        @BindView(R.id.nowPlayingGenre)
        TextView movieGenre;

        @BindView(R.id.nowPlayingTicketButton)
        Button ticketButton;

        @BindView(R.id.nowPlayingTheatreButton)
        Button theatreButton;

        NowPlayingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


