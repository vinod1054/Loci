package loci.vk.com.myapplication.addnewlocation;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import loci.vk.com.myapplication.R;
import loci.vk.com.myapplication.application.BaseActivity;

public class AddNewLocationActivity extends BaseActivity implements
        OnMapReadyCallback,
        AddNewLocationView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG="AddNewLocationActivity";
    private GoogleMap googleMap;
    private AddNewLocationPresenter  addNewLocationPresenter;
    private PlacePicker.IntentBuilder builder;
    protected GoogleApiClient mGoogleApiClient;
    private static final int PLACE_PICKER_FLAG = 1;

    @Bind(R.id.root_layout)CoordinatorLayout rootLayout;
    @Bind(R.id.edt_title)EditText locationTitle;
    @Bind(R.id.edt_radius)EditText radius;
    @Bind(R.id.toolbar) Toolbar toolbar;
    Location mLastLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNewLocationPresenter=new AddNewLocationPresenterImpl(this);
        super.getPresenterInjections(addNewLocationPresenter);
        setContentView(R.layout.activity_add_new_location);
        ButterKnife.bind(this);
        setUpToolbar();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .build();

    }

    public void setUpToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        googleMap.setMyLocationEnabled(true);
        addNewLocationPresenter.setCurrentLocation();
    }

    @Override
    protected Object getModule() {
        return null;
    }

    @Override
    protected void closeRealm() {

    }

    @Override
    public String getLocationTitle() {
        return locationTitle.getText().toString();
    }

    @Override
    public int getRadius() {
        if(TextUtils.isEmpty(radius.getText().toString()))
            return 100;
        return Integer.parseInt(radius.getText().toString());
    }

    @Override
    public boolean setMapToLocation(LatLng latLng,String address) {
        if(googleMap==null)
            return false;
        else{
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng).title(address));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        return true;
    }

    @Override
    public boolean savedNewLocation() {
        finish();
        return false;
    }

    @Override
    public boolean pickLocation() {
        return false;
    }

    @Override
    public boolean showMessage(String message) {
        Snackbar.make(rootLayout,message, Snackbar.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean startPlacePickerActivity() {
        try{
            builder = new PlacePicker.IntentBuilder();
            Intent intent = builder.build(AddNewLocationActivity.this);
            // Start the Intent by requesting a result, identified by a request code.
            startActivityForResult(intent, PLACE_PICKER_FLAG);
            Log.i(TAG,"PlacePickerActivity Started");
            return true;
        }
        catch (Exception e){
            Log.i(TAG,e.toString());
        }
        return false;
    }

    @OnClick(R.id.fab)
    public void onFabPickLocation(){
        Log.i(TAG,"onFabPickLocation");
        addNewLocationPresenter.pickNewLocation();
    }
    @OnClick(R.id.add_location)
    public void addLocation(){
        Log.i(TAG,"addLocation");
        addNewLocationPresenter.saveNewLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PLACE_PICKER_FLAG:
                    Place place = PlacePicker.getPlace(data, this);
                    Log.i(TAG,"PlacePicker onActivityResult");
                    addNewLocationPresenter.newLocationPicked(place);
                    break;
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
       // setMapToLocation(new LatLng(mLastLocation.lat))
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }
}
