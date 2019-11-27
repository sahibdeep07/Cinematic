package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import javax.inject.Inject;

import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.api.LocationService;
import cheema.hardeep.sahibdeep.brotherhood.database.UserInfoManager;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.FEATURE_UNAVAILABLE;

public class SplashActivity extends AppCompatActivity {

    private static final int TRANSITION_TIME = 2000;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Inject
    LocationService locationService;

    @Inject
    UserInfoManager userInfoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        ((Brotherhood) getApplication()).getBrotherhoodComponent().inject(this);

        new Handler().postDelayed(this::showPermissionDialog, TRANSITION_TIME);
    }

    private void showPermissionDialog() {
        if (!locationService.checkPermission()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            locationService.requestSingleLocation(null);
            transition();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, FEATURE_UNAVAILABLE, Toast.LENGTH_LONG).show();
        } else {
            locationService.requestSingleLocation(null);
        }
        transition();
    }

    private void transition() {
        Intent intent = userInfoManager.isFirstLaunch()
                ? NameActivity.createIntent(this) : HomeActivity.createIntent(this);
        startActivity(intent);
        finish();
    }
}
