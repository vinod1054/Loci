package loci.vk.com.myapplication.addnewlocation;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vinod on 21/2/16.
 */
public interface AddNewLocationView {

    public String getLocationTitle();
    public int getRadius();
    public boolean setMapToLocation(LatLng latLng,String address);
    public boolean savedNewLocation();
    public boolean pickLocation();
    public boolean showMessage(String message);
    public boolean startPlacePickerActivity();
    class EmptyView implements AddNewLocationView{

        @Override
        public String getLocationTitle() {
            return null;
        }

        public int getRadius() {
            return 0;
        }

        @Override
        public boolean setMapToLocation(LatLng latLng,String address) {
            return false;
        }

        @Override
        public boolean savedNewLocation() {
            return false;
        }

        @Override
        public boolean pickLocation() {
            return false;
        }

        @Override
        public boolean showMessage(String message) {
            return false;
        }

        @Override
        public boolean startPlacePickerActivity() {
            return false;
        }
    }

}
