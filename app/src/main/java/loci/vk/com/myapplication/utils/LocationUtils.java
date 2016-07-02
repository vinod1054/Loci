package loci.vk.com.myapplication.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by vinod on 20/2/16.
 */
public class LocationUtils {

    public static final String TAG="LocationUtils";

    @Inject
    LocationManager locationManager;

    Context context;

    // flag for GPS status
    boolean isGPSEnabled = false;

    //To get LOcation address
    Geocoder geocoder;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    public LocationUtils(Context context,LocationManager locationManager){
        this.locationManager=locationManager;
        this.context=context;
        geocoder = new Geocoder(context, Locale.getDefault());
    }


    public Location getLocation() {
        try {

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            }
            else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        getLocationAddress(location);
        return location;
    }

    public String getLocationAddress(Location location) {
        List<Address> addresses = null;
        StringBuffer readableAddress=new StringBuffer();

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            Log.i("vinod locationAddr ",ioException.toString() );
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Log.i("vinod locationAddr ", illegalArgumentException.toString());
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {

        } else {
            Address address = addresses.get(0);
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                readableAddress.append(address.getAddressLine(i)+", ");
                Log.i("vinod locationAddr ", address.getAddressLine(i));
            }
        }
        return readableAddress.toString().trim();
    }


    public double calculateDistance(LatLng source, LatLng destination){
        Log.i(TAG,source.latitude+" "+source.longitude);
        Log.i(TAG,destination.latitude+" "+destination.longitude);
        return SphericalUtil.computeDistanceBetween(source,destination);
    }

}
