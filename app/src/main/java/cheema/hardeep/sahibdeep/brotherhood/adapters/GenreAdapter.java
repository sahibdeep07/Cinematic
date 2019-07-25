package cheema.hardeep.sahibdeep.brotherhood.adapters;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.models.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    ArrayList<Genre> genreList = new ArrayList();

    public void update(List<Genre> genreList){
        this.genreList.clear();
        this.genreList.addAll(genreList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.genre_icon_design, viewGroup, false);
        return new GenreViewHolder(v);
    }

    @Override
    public void onBindViewHolder( GenreViewHolder genreViewHolder, int i) {
        Genre genre = genreList.get(i);
        genreViewHolder.icon.setImageResource(genre.getIcon());
        genreViewHolder.name.setText(genre.getName());

        genreViewHolder.icon.setOnClickListener(v -> {
            genre.setSelected(!genre.isSelected());
            int color = genre.isSelected() ? R.color.colorAccent : android.R.color.darker_gray;
            genreViewHolder.icon.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        });
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView name;

        GenreViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.genreIcon);
            name = itemView.findViewById(R.id.genreName);
        }
    }

}
