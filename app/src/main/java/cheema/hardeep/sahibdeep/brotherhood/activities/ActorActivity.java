package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.ActorAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApiProvider;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;
import cheema.hardeep.sahibdeep.brotherhood.models.Actor;
import cheema.hardeep.sahibdeep.brotherhood.models.ActorResponse;
import cheema.hardeep.sahibdeep.brotherhood.models.Genre;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class ActorActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ActorAdapter actorAdapter = new ActorAdapter();
    View home, genre;
    ProgressBar progressBar;

    public static Intent createIntent(Context context){
        return new Intent(context, ActorActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
        getSupportActionBar().hide();

        findViews();
        setIsProgressBarVisible(true);
        setListeners();
        setupRecyclerView();
        requestActors();
    }

    private void findViews(){
        genre = findViewById(R.id.genreBackground);
        home = findViewById(R.id.homeBackground);
        progressBar = findViewById(R.id.progressBar2);
        recyclerView = findViewById(R.id.actorRecyclerView);
    }

    void setIsProgressBarVisible(Boolean visible){
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    void setListeners(){
        genre.setOnClickListener(v -> finish());
        home.setOnClickListener(v -> handleHomeClick());
    }

    void setupRecyclerView(){
        recyclerView = findViewById(R.id.actorRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(actorAdapter);
    }

    private void requestActors() {
        MovieApiProvider.getMovieApi().getActors(EN_US).enqueue(new Callback<ActorResponse>() {
            @Override
            public void onResponse(Call<ActorResponse> call, Response<ActorResponse> response) {
                setIsProgressBarVisible(false);
                actorAdapter.updateList(response.body().getActors());
            }

            @Override
            public void onFailure(Call<ActorResponse> call, Throwable t) {
                Log.e("actorRequest", "onFailure: Error in getting the genres");
                Toast.makeText(ActorActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void handleHomeClick(){
        ArrayList<String> selectedActors = getSelectedActorslist();
        saveSelectedActors(selectedActors);
    }

    ArrayList<String> getSelectedActorslist(){
        ArrayList<String> result = new ArrayList<>();
        for (Actor actor : actorAdapter.getUpdatedList()) {
            if (actor.isSelected()) {
                result.add(actor.getName());
            }
        }
        return result;
    }

    void saveSelectedActors(ArrayList selectedActors){
        if (!selectedActors.isEmpty()) {
            SharedPreferenceProvider.saveUserGenres(this, selectedActors);
            startActivity(HomeActivity.createIntent(this));
        } else {
            Toast.makeText(this, "Please select an actor", Toast.LENGTH_SHORT).show();
        }
    }

}
