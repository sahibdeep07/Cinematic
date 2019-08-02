package cheema.hardeep.sahibdeep.brotherhood.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.models.Actor;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.ROUNDED_CORNER_ACTORS;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_154;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_185;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_92;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ActorViewHolder> {

    List<Actor> actorList = new ArrayList<>();

    public ActorAdapter(List<Actor> actorList){
        this.actorList = actorList;
    }

    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.actor_icon_design, viewGroup ,false);
        return new ActorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewHolder actorViewHolder, int i) {
        Actor actor = actorList.get(i);
        actorViewHolder.name.setText(actor.getName());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.circleCropTransform();
        Glide.with(actorViewHolder.image.getContext())
                .load(Utilities.createImageUrl(actor.getProfilePath(), SIZE_92))
                .apply(requestOptions)
                .into(actorViewHolder.image);
        setImageColor(actorViewHolder.image, actor.isSelected());

        actorViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actor.setSelected(!actor.isSelected());
                setImageColor(actorViewHolder.image, actor.isSelected());
            }
        });
    }

    @Override
    public int getItemCount() {
        return actorList.size();
    }

    public void setImageColor(ImageView imageView, boolean isSelected){
        if(isSelected) imageView.setBackgroundResource(R.drawable.circular_background_selected);
        else imageView.setBackgroundResource(R.drawable.circular_background);
    }

    class ActorViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView image;
        public ActorViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.actorName);
            image = itemView.findViewById(R.id.actorIcon);
        }
    }
}
