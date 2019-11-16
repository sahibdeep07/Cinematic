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
import java.util.concurrent.TimeoutException;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.activities.DetailActivity;
import cheema.hardeep.sahibdeep.brotherhood.models.Movie;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.ROUNDED_CORNER_UPCOMING;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_342;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.NowPlayingViewHolder> {

    private List<Movie> movies = new ArrayList<>();

    public void updateDataSet(List<Movie> upcomingMovies) {
        movies.clear();
        movies.addAll(upcomingMovies);
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

        holder.itemView.setOnClickListener(v ->
                v.getContext().startActivity(DetailActivity.createIntent(v.getContext(), movie.getId())));
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

    class NowPlayingViewHolder extends RecyclerView.ViewHolder {

        ImageView movieImage;
        TextView movieRating;
        TextView movieName;
        TextView movieGenre;

        NowPlayingViewHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.nowPlayingImageView);
            movieRating = itemView.findViewById(R.id.nowPlayingRating);
            movieName = itemView.findViewById(R.id.nowPlayingMovieName);
            movieGenre = itemView.findViewById(R.id.nowPlayingGenre);
        }
    }
}


