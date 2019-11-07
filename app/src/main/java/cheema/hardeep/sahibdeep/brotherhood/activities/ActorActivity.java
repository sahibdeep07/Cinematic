package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.ActorAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApi;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;
import cheema.hardeep.sahibdeep.brotherhood.models.Actor;
import cheema.hardeep.sahibdeep.brotherhood.models.ActorResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class ActorActivity extends AppCompatActivity {

    private static final String SAVE = "Save";
    private static final String HOME = "Home";
    private static final float PERCENT_30 = 0.30f;
    private static final float PERCENT_0 = 0.00f;

    RecyclerView actorsRecyclerView;
    ActorAdapter actorAdapter;
    View moveToHomeOrSaveBackground, moveToGenreBackground;
    ProgressBar actorsProgressBar;
    Guideline actorGuideline;
    TextView moveToHomeOrSave;
    Group genreGroup;


    @Inject
    MovieApi movieApi;

    public static Intent createIntent(Context context){
        return new Intent(context, ActorActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
        getSupportActionBar().hide();
        ((Brotherhood) getApplication()).getBrotherhoodComponent().inject(this);

        findViews();
        setupBottomButtonsAndGuideline();
        setIsProgressBarVisible(true);
        setListeners();
        setupRecyclerView();
        requestActors();
    }

    private void findViews() {
        moveToGenreBackground = findViewById(R.id.moveToGenreBackground);
        moveToHomeOrSaveBackground = findViewById(R.id.moveToHomeOrSaveBackground);
        actorsProgressBar = findViewById(R.id.actorProgressBar);
        actorsRecyclerView = findViewById(R.id.actorsRecyclerView);
        actorGuideline = findViewById(R.id.actorGuideline);
        moveToHomeOrSave = findViewById(R.id.moveToHomeOrSave);
        genreGroup = findViewById(R.id.genreGroup);
    }

    private void setupBottomButtonsAndGuideline() {
        boolean isFirstLaunch = SharedPreferenceProvider.isFirstLaunch(this);
        genreGroup.setVisibility(isFirstLaunch ? View.VISIBLE : View.GONE);
        actorGuideline.setGuidelinePercent(isFirstLaunch ? PERCENT_30 : PERCENT_0);
        moveToHomeOrSave.setText(isFirstLaunch ? HOME : SAVE);
    }

    void setIsProgressBarVisible(Boolean visible) {
        actorsProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        actorsRecyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    void setListeners() {
        moveToGenreBackground.setOnClickListener(v -> finish());
        moveToHomeOrSaveBackground.setOnClickListener(v -> handleHomeClick());
    }

    void setupRecyclerView() {
        actorsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        actorAdapter = new ActorAdapter(true);
        actorsRecyclerView.setAdapter(actorAdapter);
    }

    private void requestActors() {
        movieApi.getActors(EN_US).enqueue(new Callback<ActorResponse>() {
            @Override
            public void onResponse(Call<ActorResponse> call, Response<ActorResponse> response) {
                handleActorResponse(response);
            }

            @Override
            public void onFailure(Call<ActorResponse> call, Throwable t) {
                Log.e("actorRequest", "onFailure: Error in getting the genres");
                Toast.makeText(ActorActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleActorResponse(Response<ActorResponse> actorResponse) {
        setIsProgressBarVisible(false);
        List<Actor> userActors = SharedPreferenceProvider.getUserActors(this);
        for (Actor actor : actorResponse.body().getActors()) {
            //Pre Select Genre if already in SharedPreferences
            if (!userActors.isEmpty()) {
                for (Actor userActor : userActors) {
                    if (actor.getId().equals(userActor.getId())) {
                        actor.setSelected(true);
                    }
                }
            }
        }
        actorAdapter.update(actorResponse.body().getActors());
    }

    private void handleHomeClick() {
        ArrayList<Actor> selectedActors = getSelectedActorsList();
        saveSelectedActors(selectedActors);
    }

    private ArrayList<Actor> getSelectedActorsList() {
        ArrayList<Actor> result = new ArrayList<>();
        for (Actor actor : actorAdapter.getUpdatedList()) {
            if (actor.isSelected()) {
                result.add(actor);
            }
        }
        return result;
    }

    private void saveSelectedActors(ArrayList<Actor> selectedActors) {
        SharedPreferenceProvider.saveUserActors(this, selectedActors);
        handleTransition();
    }

    private void handleTransition() {
        if (SharedPreferenceProvider.isFirstLaunch(this)) {
            startActivity(HomeActivity.createIntent(this));
            SharedPreferenceProvider.saveFirstLaunchCompleted(this);
        } else {
            finish();
        }
    }
}
