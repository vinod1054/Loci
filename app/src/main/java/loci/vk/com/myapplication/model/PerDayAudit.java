package loci.vk.com.myapplication.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vinod on 26/2/16.
 */
public class  PerDayAudit extends RealmObject{

    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private Date day;
    private Date lastSeen;
    private Date firstSeen;
    private String timeSpentString;
    private long timeSpent;
    private long locationId;
    private int timeSpentPercentage;
    private int status;
    @PrimaryKey
    private String id;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTimeSpentPercentage() {
        return timeSpentPercentage;
    }

    public void setTimeSpentPercentage(int timeSpentPercentage) {
        this.timeSpentPercentage = timeSpentPercentage;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getTimeSpentString() {
        return timeSpentString;
    }

    public void setTimeSpentString(String timeSpentString) {
        this.timeSpentString = timeSpentString;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(Date firstSeen) {
        this.firstSeen = firstSeen;
    }


    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
