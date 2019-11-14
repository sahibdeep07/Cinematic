package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.security.Permissions;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;
import cheema.hardeep.sahibdeep.brotherhood.utils.LocationUtil;

public class SplashActivity extends AppCompatActivity {

    private static final int TRANSITION_TIME = 2000;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(() -> showPermissionDialog(), TRANSITION_TIME);
    }

    private void showPermissionDialog() {
        if (!LocationUtil.checkPermission(this)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            transition();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Some features of the application might not work properly without location permissions", Toast.LENGTH_LONG).show();
        }
        transition();
    }

    private void transition() {
        Intent intent = SharedPreferenceProvider.isFirstLaunch(this)
                ? NameActivity.createIntent(this) : HomeActivity.createIntent(this);
        startActivity(intent);
        finish();
    }
}
