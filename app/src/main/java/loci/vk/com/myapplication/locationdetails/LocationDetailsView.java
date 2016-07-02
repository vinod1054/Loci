package loci.vk.com.myapplication.locationdetails;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import loci.vk.com.myapplication.model.PerDayAudit;

/**
 * Created by vinod on 26/2/16.
 */
public interface LocationDetailsView {

    public void setTitle(String title);
    public void setAdapter(List<PerDayAudit> list);
    public void onMapReady(LatLng latLng,String title);
    public void setMapToLocation(LatLng latLng, String address);

}
