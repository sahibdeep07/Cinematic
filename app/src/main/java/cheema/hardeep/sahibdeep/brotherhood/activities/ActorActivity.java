package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.adapters.ActorAdapter;
import cheema.hardeep.sahibdeep.brotherhood.api.MovieApi;
import cheema.hardeep.sahibdeep.brotherhood.database.UserInfoManager;
import cheema.hardeep.sahibdeep.brotherhood.models.Actor;
import cheema.hardeep.sahibdeep.brotherhood.models.ActorResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EN_US;

public class ActorActivity extends AppCompatActivity {

    private static final String SAVE = "Save";
    private static final String HOME = "Home";
    private static final float PERCENT_30 = 0.30f;
    private static final float PERCENT_0 = 0.00f;

    @BindView(R.id.actorsRecyclerView)
    RecyclerView actorsRecyclerView;

    @BindView(R.id.moveToHomeOrSaveBackground)
    View moveToHomeOrSaveBackground;

    @BindView(R.id.moveToGenreBackground)
    View moveToGenreBackground;

    @BindView(R.id.actorProgressBar)
    ProgressBar actorsProgressBar;

    @BindView(R.id.actorGuideline)
    Guideline actorGuideline;

    @BindView(R.id.moveToHomeOrSave)
    TextView moveToHomeOrSave;

    @BindView(R.id.genreGroup)
    Group genreGroup;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    MovieApi movieApi;

    @Inject
    UserInfoManager userInfoManager;

    private ActorAdapter actorAdapter;


    public static Intent createIntent(Context context) {
        return new Intent(context, ActorActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
        getSupportActionBar().hide();
        ((Brotherhood) getApplication()).getBrotherhoodComponent().inject(this);
        ButterKnife.bind(this);

        setupBottomButtonsAndGuideline();
        setListeners();
        setupRecyclerView();
        requestActors();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compositeDisposable.clear();
    }

    private void setupBottomButtonsAndGuideline() {
        boolean isFirstLaunch = userInfoManager.isFirstLaunch();
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
        compositeDisposable.add(
                movieApi.getActors(EN_US)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> setIsProgressBarVisible(true))
                        .doOnTerminate(() -> setIsProgressBarVisible(false))
                        .subscribe(
                                actorResponse -> handleActorResponse(actorResponse),
                                throwable -> Log.e(ActorResponse.class.getSimpleName(), "onFailure: Error in getting the actors" + throwable)
                        )
        );
    }

    private void handleActorResponse(ActorResponse actorResponse) {
        List<Actor> userActors = userInfoManager.getUserActors();
        for (Actor actor : actorResponse.getActors()) {
            //Pre Select Genre if already in SharedPreferences
            if (!userActors.isEmpty()) {
                for (Actor userActor : userActors) {
                    if (actor.getId().equals(userActor.getId())) {
                        actor.setSelected(true);
                    }
                }
            }
        }
        actorAdapter.update(actorResponse.getActors());
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
        userInfoManager.saveUserActors(selectedActors);
        handleTransition();
    }

    private void handleTransition() {
        if (userInfoManager.isFirstLaunch()) {
            startActivity(HomeActivity.createIntent(this));
            userInfoManager.saveFirstLaunchCompleted();
        } else {
            finish();
        }
    }
}
