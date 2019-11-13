package cheema.hardeep.sahibdeep.brotherhood.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

    private ArrayList<Genre> genreList = new ArrayList();
    private boolean isSelectable;

    public GenreAdapter(boolean isSelectable) {
        this.isSelectable = isSelectable;
    }

    public void update(List<Genre> genreList) {
        this.genreList.clear();
        this.genreList.addAll(genreList);
        notifyDataSetChanged();
    }

    public ArrayList<Genre> getUpdatedList() {
        return genreList;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.genre_icon_design, viewGroup, false);
        return new GenreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder genreViewHolder, int i) {
        Genre genre = genreList.get(i);
        genreViewHolder.icon.setImageResource(genre.getIcon());
        genreViewHolder.name.setText(genre.getName());
        setImageColor(genreViewHolder.icon, genre.isSelected());

        if (isSelectable) {
            genreViewHolder.icon.setOnClickListener(v -> {
                genre.setSelected(!genre.isSelected());
                setImageColor(genreViewHolder.icon, genre.isSelected());
            });
        }
    }

    private void setImageColor(ImageView imageView, boolean isSelected) {
        int colors = isSelected ? imageView.getResources().getColor(R.color.colorAccentLight)
                : imageView.getResources().getColor(R.color.colorPrimaryLight);
        imageView.setColorFilter(colors);
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        GenreViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.genreIcon);
            name = itemView.findViewById(R.id.genreName);
        }
    }

}