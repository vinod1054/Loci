package loci.vk.com.myapplication.addnewlocation;

import android.location.Location;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import javax.inject.Inject;

import loci.vk.com.myapplication.TestPurpose.RealmService;
import loci.vk.com.myapplication.model.MyLocation;
import loci.vk.com.myapplication.utils.LocationUtils;

/**
 * Created by vinod on 21/2/16.
 */
public class AddNewLocationPresenterImpl implements AddNewLocationPresenter,RealmService.OnTransactionCallback {

    public static final String TAG="AddNewLocationPresenter";


    Handler handler=new Handler();

    @Inject
    LocationUtils locationUtils;
    @Inject
    RealmService realmService;
    AddNewLocationView addNewLocationView=new AddNewLocationView.EmptyView();
    Location location;
    String address;

    public AddNewLocationPresenterImpl(AddNewLocationView addNewLocationView){
        this.addNewLocationView=addNewLocationView;
    }

    @Override
    public LatLng setCurrentLocation() {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                location=locationUtils.getLocation();
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
              //  address=locationUtils.getLocationAddress(location); //lets not show the address for now
                addNewLocationView.setMapToLocation(latLng,"");
            }
        };
        handler.post(runnable);
        return null;
    }

    @Override
    public boolean saveNewLocation() {
        if(location==null){
            addNewLocationView.showMessage("Please choose a location");
            return false;
        }
        String title,description;
        int radius;
        title=addNewLocationView.getLocationTitle();
        radius =addNewLocationView.getRadius();
        if(TextUtils.isEmpty(title)){
            addNewLocationView.showMessage("Please enter the title of this location");
            return false;
        }
        MyLocation myLocation =new MyLocation();
        myLocation.setId(new Date().getTime());
        myLocation.setLocationName(title);
        myLocation.setRadius(radius);
        myLocation.setLongitude(location.getLongitude());
        myLocation.setLatitude(location.getLatitude());
        myLocation.setLocationAddress(address);
        realmService.addLocationAsync(this, myLocation);
        addNewLocationView.savedNewLocation();
        return true;
    }

    @Override
    public boolean pickNewLocation() {
        addNewLocationView.startPlacePickerActivity();
        return false;
    }

    @Override
    public boolean newLocationPicked(Place place) {
        location=new Location("new");
        location.setLongitude(place.getLatLng().longitude);
        location.setLatitude(place.getLatLng().latitude);
        address=place.getAddress().toString();
        addNewLocationView.setMapToLocation(place.getLatLng(),address);
        return false;
    }

    @Override
    public void setView(Object view) {

    }

    @Override
    public void clearView() {

    }

    @Override
    public void closeRealm() {

    }

    @Override
    public void onRealmSuccess() {
        Log.i(TAG,"saved successfully");
    }

    @Override
    public void onRealmError(Exception e) {
        Log.i(TAG,"Failed to save "+e.toString());

    }
}
