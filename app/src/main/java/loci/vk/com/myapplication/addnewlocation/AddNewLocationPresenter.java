package loci.vk.com.myapplication.addnewlocation;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import loci.vk.com.myapplication.application.BasePresenter;

/**
 * Created by vinod on 21/2/16.
 */
public interface AddNewLocationPresenter extends BasePresenter {

    public LatLng setCurrentLocation();
    public boolean saveNewLocation();
    public boolean pickNewLocation();
    public boolean newLocationPicked(Place place);

}
