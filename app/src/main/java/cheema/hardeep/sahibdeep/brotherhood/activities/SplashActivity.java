package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;

public class SplashActivity extends AppCompatActivity {

    private static final int TRANSITION_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(() -> {
            Intent intent = SharedPreferenceProvider.isFirstLaunch(this)
                    ? NameActivity.createIntent(this) : HomeActivity.createIntent(this);
            startActivity(intent);
            finish();
        }, TRANSITION_TIME);
    }
}
