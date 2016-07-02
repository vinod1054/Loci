package loci.vk.com.myapplication.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import loci.vk.com.myapplication.TestPurpose.RealmService;
import loci.vk.com.myapplication.application.LociApplication;
import loci.vk.com.myapplication.model.LocationAudit;
import loci.vk.com.myapplication.model.MyLocation;
import loci.vk.com.myapplication.model.PerDayAudit;
import loci.vk.com.myapplication.utils.LocationUtils;
import loci.vk.com.myapplication.utils.SharedPreferencesUtil;
import loci.vk.com.myapplication.utils.TimeCalculationUtil;

public class LocationTrackerService extends Service implements
        LocationListener,
        RealmChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    final int radius=100; //in meters

    @Inject
    LocationManager locationManager;
    @Inject
    Context mContext;
    @Inject
    LocationUtils locationUtils;
    @Inject
    RealmService realmService;
    @Inject
    SharedPreferencesUtil sharedPreferencesUtil;

    org.greenrobot.eventbus.EventBus eventBus;
    RealmResults<MyLocation> myLocations;

    final static String TAG="LocationTrackerService";
    // flag for GPS status
    boolean isGPSEnabled = false;

    //To get LOcation address
    Geocoder geocoder;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Runnable runnable;

    long lastKnownLocationId=-1,lastKnownLocationTimeStamp=-1;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000  * 1; // 1 minute

    LatLng latLng;


    public LocationTrackerService() {
        Log.i("vinod ", getApplication() + " ");
        LociApplication.sInstance.injectModules(this);
    }

    @Override
    public void onCreate() {
        Log.i(TAG,"Start service onCreate");
        super.onCreate();
        lastKnownLocationId=sharedPreferencesUtil.getValueForKeyLong("lastlocationid");
        lastKnownLocationTimeStamp=sharedPreferencesUtil.getValueForKeyLong("lasttimestamp");
        sharedPreferencesUtil.setKeyValuePair("lasttimestamp",lastKnownLocationTimeStamp);
        geocoder = new Geocoder(this, Locale.getDefault());
        myLocations=realmService.getAllLocations();
        myLocations.addChangeListener(this);
        eventBus= org.greenrobot.eventbus.EventBus.getDefault();
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        Log.i(TAG,"Start service onCreate Completed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("vinod ","onLocationChanged");
        gotNewLocation(location);
    }

    public void gotNewLocation(final Location current){

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                lastKnownLocationId=sharedPreferencesUtil.getValueForKeyLong("lastlocationid");
                lastKnownLocationTimeStamp=sharedPreferencesUtil.getValueForKeyLong("lasttimestamp");
                if(myLocations==null)
                    myLocations=realmService.getAllLocations();
                if(sharedPreferencesUtil.getValueForKeyLong("lastcreateddate")!=TimeCalculationUtil.getToday().getTime()){
                    createPerDayAuditForToday();
                }
                LocationAudit locationAudit=new LocationAudit();
                boolean isStoredAtleastOnce=false;
                for(int i=0;i<myLocations.size();i++){
                    double distance=0;
                    LatLng currentLatLang=new LatLng(current.getLatitude(),current.getLongitude());
                    LatLng storedLatLang=new LatLng(myLocations.get(i).getLatitude(),myLocations.get(i).getLongitude());
                    distance=locationUtils.calculateDistance(currentLatLang, storedLatLang);
                    locationAudit=new LocationAudit();
                    locationAudit.setLongitude(current.getLongitude());
                    locationAudit.setLatitude(current.getLatitude());
                    locationAudit.setTimeStamp(new Date().getTime());
                    if(distance<=myLocations.get(i).getRadius()){

                        isStoredAtleastOnce=true;
                        locationAudit.setMyLocationId(myLocations.get(i).getId());
                        Log.i(TAG, "Storing into " + myLocations.get(i).getLocationName() + " ");
                        if(sharedPreferencesUtil.getValueForKeyBoolean(myLocations.get(i).getId()+""))
                            locationAudit.setIsSameAsPrevious(true);

                        else
                            locationAudit.setIsSameAsPrevious(false);
                        storeIntoPerDayAudit(locationAudit);
                        lastKnownLocationId=locationAudit.getMyLocationId();
                        sharedPreferencesUtil.setKeyValuePair("lastlocationid",lastKnownLocationId);
                        // to know later that this is the past known location
                        sharedPreferencesUtil.setKeyValuePair(myLocations.get(i).getId()+"",true);
                        realmService.addLocationAuditAsync(null, locationAudit);
                    }
                    else {
                        locationAudit.setTimeStamp(0);
                        // to know later that this is not the past known location
                        sharedPreferencesUtil.setKeyValuePair(myLocations.get(i).getId() + "", false);
                    }
                }
                lastKnownLocationTimeStamp = new Date().getTime();
                sharedPreferencesUtil.setKeyValuePair("lasttimestamp",lastKnownLocationTimeStamp);
                if(!isStoredAtleastOnce){
                    Log.i(TAG, "Storing into UnknownLocation 0 ");
                    realmService.addLocationAuditAsync(null, locationAudit);
                    lastKnownLocationId=locationAudit.getMyLocationId();
                    lastKnownLocationTimeStamp = locationAudit.getTimeStamp();
                    sharedPreferencesUtil.setKeyValuePair("lastlocationid",lastKnownLocationId);
                    sharedPreferencesUtil.setKeyValuePair("lasttimestamp",lastKnownLocationTimeStamp);
                }

            }
        };
        runnable.run();
    }

    private void createPerDayAuditForToday() {
        if(myLocations==null)
            myLocations=realmService.getAllLocations();
        for(MyLocation location: myLocations){
           // if(location.getStatus()!=1)
                createNewRow(location);
        }
       // sharedPreferencesUtil.setKeyValuePair(Long.toString(today.getTime()),true);
        sharedPreferencesUtil.setKeyValuePair("lastcreateddate",TimeCalculationUtil.getToday().getTime());
        Log.i(TAG,"lastcreateddate is updated in preferences");
    }

    private void createNewRow(MyLocation location){
        PerDayAudit perDayAudit = new PerDayAudit();
        perDayAudit.setDay(TimeCalculationUtil.getToday());
        perDayAudit.setId(TimeCalculationUtil.getToday().getTime()+","+location.getId());
        perDayAudit.setName(location.getLocationName());
        perDayAudit.setLatitude(location.getLatitude());
        perDayAudit.setLongitude(location.getLongitude());
        perDayAudit.setFirstSeen(null);
        perDayAudit.setLastSeen(null);
        perDayAudit.setLocationId(location.getId());
        realmService.addPerDayAuditAsync(null,perDayAudit);
    }


    public void storeIntoPerDayAudit(LocationAudit locationAudit){
            Log.i(TAG,"Storing into PerDayTable , same as previous "+locationAudit.isSameAsPrevious());
            Log.i(TAG,"previous time "+new Date(lastKnownLocationTimeStamp));
            Log.i(TAG,"Current time "+new Date(locationAudit.getTimeStamp()));
            long addingAmount=locationAudit.getTimeStamp()-lastKnownLocationTimeStamp;
            realmService.updateDistance(null
                    ,locationAudit.getMyLocationId()
                    , TimeCalculationUtil.getToday().getTime()
                    ,addingAmount
                    ,locationAudit.isSameAsPrevious());
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        mGoogleApiClient.connect();
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        eventBus.unregister(this);
    }


    @Override
    public void onChange() {
        Log.i(TAG,"new Place has been created "+myLocations.get(myLocations.size()-1).getLocationName());
        createNewRow(myLocations.get(myLocations.size()-1));
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000*60*2);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    protected void startLocationUpdates() {
        createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG,"service onConnected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG,"service onConnectionSuspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG,"service onConnectionFailed");
        mGoogleApiClient.connect();
    }
}
