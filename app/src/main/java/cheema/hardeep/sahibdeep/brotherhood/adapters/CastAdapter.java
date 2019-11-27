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

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.models.Cast;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_92;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private List<Cast> castList;

    public CastAdapter(List<Cast> castList) {
        this.castList = castList;
    }

    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cast_item, viewGroup, false);
        return new CastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder castViewHolder, int i) {
        Cast cast = castList.get(i);
        castViewHolder.castName.setText(cast.getName());
        setUpActorImage(castViewHolder.castImage, cast);
    }

    private void setUpActorImage(@NonNull ImageView imageView, Cast cast) {
        Glide.with(imageView.getContext())
                .load(Utilities.createImageUrl(cast.getProfilePath(), SIZE_92))
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    class CastViewHolder extends RecyclerView.ViewHolder {
        ImageView castImage;
        TextView castName;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            castImage = itemView.findViewById(R.id.castImage);
            castName = itemView.findViewById(R.id.castName);
        }
    }
}
