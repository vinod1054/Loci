package loci.vk.com.myapplication.model;

import io.realm.RealmObject;

/**
 * Created by vinod on 21/2/16.
 */
public class LocationAudit extends RealmObject{

    private long timeStamp;
    private double latitude;
    private double longitude;
    private long myLocationId;
    private boolean isSameAsPrevious;

    public boolean isSameAsPrevious() {
        return isSameAsPrevious;
    }

    public void setIsSameAsPrevious(boolean isSameAsPrevious) {
        this.isSameAsPrevious = isSameAsPrevious;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getMyLocationId() {
        return myLocationId;
    }

    public void setMyLocationId(long myLocationId) {
        this.myLocationId = myLocationId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
