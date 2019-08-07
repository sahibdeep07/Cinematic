package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.fragments.NowPlayingFragment;
import cheema.hardeep.sahibdeep.brotherhood.fragments.RecommendedFragment;
import cheema.hardeep.sahibdeep.brotherhood.fragments.SettingsFragment;
import cheema.hardeep.sahibdeep.brotherhood.fragments.UpcomingFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static Intent createIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        BottomNavigationView navView = findViewById(R.id.bottomNav);
        navView.setOnNavigationItemSelectedListener(this);
        loadFragment(new RecommendedFragment());
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
