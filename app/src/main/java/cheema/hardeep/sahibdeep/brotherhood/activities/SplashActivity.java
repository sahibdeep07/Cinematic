package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import cheema.hardeep.sahibdeep.brotherhood.R;

public class SplashActivity extends AppCompatActivity {

    private static final int TRANSITION_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(() -> {
            startActivity(NameActivity.createIntent(SplashActivity.this));
            finish();
        }, TRANSITION_TIME);
    }
}
