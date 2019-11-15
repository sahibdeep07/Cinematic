package cheema.hardeep.sahibdeep.brotherhood.api;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EMPTY;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.LOCATION_OFF_AND_PERMISSION_REQUIRED;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.NO_LOCATION;

public class LocationService {

    public interface LocationProvider {

        void onLocation(Location location);

        void onLocationFailure();
    }

    private Context context;
    private Location currentLocation;

    public LocationService(Context context) {
        this.context = context;
    }

    public boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestSingleLocation(LocationProvider locationProvider) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (checkPermission() && isGPSEnabled) {
            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    currentLocation = location;
                    if (locationProvider != null) locationProvider.onLocation(location);
                }

                @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
                @Override public void onProviderEnabled(String provider) { }
                @Override public void onProviderDisabled(String provider) { }
            }, null);
        } else {
            Toast.makeText(context, LOCATION_OFF_AND_PERMISSION_REQUIRED, Toast.LENGTH_SHORT).show();
            if (locationProvider != null) locationProvider.onLocationFailure();
        }
    }

    public String getCityName() {
        if (currentLocation != null) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                return addresses.size() > 0 ? addresses.get(0).getLocality() : EMPTY;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(LocationService.class.getSimpleName(), NO_LOCATION);
        }
        return EMPTY;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
}
