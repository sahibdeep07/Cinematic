package cheema.hardeep.sahibdeep.brotherhood.adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.models.Movie;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.ROUNDED_CORNER;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_342;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_500;

public class UpcomingMovieAdapter extends RecyclerView.Adapter<UpcomingMovieAdapter.UpcomingMovieViewHolder> {

    private List<Movie> movies = new ArrayList<>();

    public void updateDataSet(List<Movie> upcomingMovies) {
        movies.clear();
        movies.addAll(upcomingMovies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UpcomingMovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upcoming_item_movie, viewGroup, false);
        return new UpcomingMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingMovieViewHolder upcomingMovieViewHolder, int i) {
        Movie movie = movies.get(i);
        upcomingMovieViewHolder.movieName.setText(movie.getTitle());
        upcomingMovieViewHolder.movieRating.setText(String.valueOf(movie.getVoteAverage()));
        loadImageWithGlide(movie.getBackdropPath(), upcomingMovieViewHolder.movieImage);
    }

    private void loadImageWithGlide(String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(ROUNDED_CORNER));
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

        ImageView movieImage;
        TextView movieRating;
        TextView movieName;

        UpcomingMovieViewHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.upcomingMovieImage);
            movieRating = itemView.findViewById(R.id.upcomingMovieRating);
            movieName = itemView.findViewById(R.id.upcomingMovieName);
        }
    }
}
