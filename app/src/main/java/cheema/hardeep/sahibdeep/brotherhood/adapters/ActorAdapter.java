package cheema.hardeep.sahibdeep.brotherhood.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.models.Actor;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_92;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ActorViewHolder> {

    private ArrayList<Actor> actorList = new ArrayList<>();
    private boolean isSelectable;

    public ActorAdapter(boolean isSelectable) {
        this.isSelectable = isSelectable;
    }

    public void update(List<Actor> actorList) {
        this.actorList.clear();
        this.actorList.addAll(actorList);
        notifyDataSetChanged();
    }

    public void addItems(List<Actor> actorList) {
        this.actorList.addAll(actorList);
        notifyDataSetChanged();
    }

    public ArrayList<Actor> getUpdatedList() {
        return actorList;
    }

    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.actor_icon_design, viewGroup, false);
        return new ActorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewHolder actorViewHolder, int i) {
        Actor actor = actorList.get(i);
        actorViewHolder.name.setText(actor.getName());

        setUpActorImage(actorViewHolder.image, actor);

        if (isSelectable) {
            actorViewHolder.image.setOnClickListener(v -> {
                actor.setSelected(!actor.isSelected());
                setImageColor(actorViewHolder.image, actor.isSelected());
            });
        }
    }

    private void setUpActorImage(@NonNull ImageView imageView, Actor actor) {
        Glide.with(imageView.getContext())
                .load(Utilities.createImageUrl(actor.getProfilePath(), SIZE_92))
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
        setImageColor(imageView, actor.isSelected());
    }

    @Override
    public int getItemCount() {
        return actorList.size();
    }

    private void setImageColor(ImageView imageView, boolean isSelected) {
        if (isSelected) imageView.setBackgroundResource(R.drawable.circular_background_selected);
        else imageView.setBackgroundResource(R.drawable.circular_background);
    }

    class ActorViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.actorName)
        TextView name;

        @BindView(R.id.actorIcon)
        ImageView image;

        public ActorViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
