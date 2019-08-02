package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.ActorAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApiProvider;
import cheema.hardeep.sahibdeep.brotherhood.models.ActorResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class ActorActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ActorAdapter actorAdapter;

    public static Intent createIntent(Context context){
        return new Intent(context, ActorActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
        recyclerView = findViewById(R.id.actorRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        requestActors();
    }

    private void requestActors() {
        MovieApiProvider.getMovieApi().getActors(EN_US).enqueue(new Callback<ActorResponse>() {
            @Override
            public void onResponse(Call<ActorResponse> call, Response<ActorResponse> response) {
                recyclerView.setAdapter(new ActorAdapter(response.body().getActors()));
            }

            @Override
            public void onFailure(Call<ActorResponse> call, Throwable t) {
                Log.e("actorRequest", "onFailure: Error in getting the genres");
                Toast.makeText(ActorActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
