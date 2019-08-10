package cheema.hardeep.sahibdeep.brotherhood.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.activities.ActorActivity;
import cheema.hardeep.sahibdeep.brotherhood.activities.GenreActivity;
import cheema.hardeep.sahibdeep.brotherhood.adapters.ActorAdapter;
import cheema.hardeep.sahibdeep.brotherhood.adapters.GenreAdapter;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;
import cheema.hardeep.sahibdeep.brotherhood.models.UserInfo;

public class SettingsFragment extends Fragment {

    EditText username;
    TextView genreEdit;
    TextView actorEdit;
    TextView noGenreMessage;
    TextView noActorMessage;
    RecyclerView genreRecyclerView;
    RecyclerView actorRecyclerView;
    GenreAdapter genreAdapter;
    ActorAdapter actorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        findViews(view);
        handleClickListeners();
        setupGenreRecyclerView();
        setupActorRecyclerView();
        return view;
    }

    private void findViews(View view) {
        username = view.findViewById(R.id.username);
        genreEdit = view.findViewById(R.id.genreEdit);
        actorEdit = view.findViewById(R.id.actorEdit);
        genreRecyclerView = view.findViewById(R.id.settingsGenresRecyclerView);
        actorRecyclerView = view.findViewById(R.id.settingsActorsRecyclerView);
        noGenreMessage = view.findViewById(R.id.noGenreMessage);
        noActorMessage = view.findViewById(R.id.noActorsMessage);
    }

    private void handleClickListeners() {
        genreEdit.setOnClickListener(v -> startActivity(GenreActivity.createIntent(getContext())));
        actorEdit.setOnClickListener(v -> startActivity(ActorActivity.createIntent(getContext())));
    }

    private void setupGenreRecyclerView() {
        genreAdapter = new GenreAdapter(false);
        genreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        genreRecyclerView.setAdapter(genreAdapter);
    }

    private void setupActorRecyclerView() {
        actorAdapter = new ActorAdapter(false);
        actorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        actorRecyclerView.setAdapter(actorAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        UserInfo userInfo = SharedPreferenceProvider.getFullUserInfo(getContext());
        username.setText(userInfo.getName());
        handleGenresUserPreferences(userInfo);
        handleUserActorsPreferences(userInfo);
    }

    private void handleGenresUserPreferences(UserInfo userInfo) {
        if(userInfo.getGenres().isEmpty()) {
            genreRecyclerView.setVisibility(View.GONE);
            noGenreMessage.setVisibility(View.VISIBLE);
        } else {
            noGenreMessage.setVisibility(View.GONE);
            genreRecyclerView.setVisibility(View.VISIBLE);
            genreAdapter.update(userInfo.getGenres());
        }
    }

    private void handleUserActorsPreferences(UserInfo userInfo) {
        if(userInfo.getActors().isEmpty()) {
            actorRecyclerView.setVisibility(View.GONE);
            noActorMessage.setVisibility(View.VISIBLE);
        } else {
            noActorMessage.setVisibility(View.GONE);
            actorRecyclerView.setVisibility(View.VISIBLE);
            actorAdapter.update(userInfo.getActors());
        }
    }
}
