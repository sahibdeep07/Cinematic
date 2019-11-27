package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.fragments.NowPlayingFragment;
import cheema.hardeep.sahibdeep.brotherhood.fragments.RecommendedFragment;
import cheema.hardeep.sahibdeep.brotherhood.fragments.SettingsFragment;
import cheema.hardeep.sahibdeep.brotherhood.fragments.UpcomingFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottomNav)
    BottomNavigationView navView;

    public static Intent createIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        navView.setOnNavigationItemSelectedListener(this);
        loadFragment(new NowPlayingFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recommended:
                loadFragment(new RecommendedFragment());
                return true;
            case R.id.nowPlaying:
                loadFragment(new NowPlayingFragment());
                return true;
            case R.id.upcoming:
                loadFragment(new UpcomingFragment());
                return true;
            case R.id.settings:
                loadFragment(new SettingsFragment());
                return true;
        }
        return false;
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }
}
