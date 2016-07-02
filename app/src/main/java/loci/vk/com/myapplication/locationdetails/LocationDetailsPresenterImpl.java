package loci.vk.com.myapplication.locationdetails;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import loci.vk.com.myapplication.TestPurpose.RealmService;
import loci.vk.com.myapplication.model.PerDayAudit;

/**
 * Created by vinod on 26/2/16.
 */
public class LocationDetailsPresenterImpl implements LocationDetailsPresenter {

    final String TAG="MyLocationDetPrestr";

    @Inject
    RealmService realmService;

    LocationDetailsView view;
    String title;
    long locationId;
    double latitude,longitude;


    List<PerDayAudit> perDayAuditList=new ArrayList<>();


    public LocationDetailsPresenterImpl(LocationDetailsView view){
        this.view=view;
    }

    @Override
    public void onCreate(Bundle bundle) {
        readBundle(bundle);
        view.setTitle(title);
        try{
            //fetchData();
            perDayAuditList=realmService.getAllDaysLocations(locationId);
            view.setAdapter(perDayAuditList);
        }
        catch (Exception e){
            Log.i(TAG,"error occured "+e.toString());
        }
    }

//    private void fetchData() throws  Exception{
//
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//
//        Date today = new Date();
//
//        Date todayWithZeroTime = formatter.parse(formatter.format(today));
//        RealmResults<LocationAudit> realmResults=realmService.getLocationAuditByPlaceAndDate(locationId,todayWithZeroTime);
//        if(realmResults.size()<1){
//            mAdapter.setList(perDayAuditList);
//            view.setAdapter(mAdapter);
//            Log.i(TAG,"setting the adapter with size empty"+mAdapter.getList().size());
//            return;
//        }
//        long mins= TimeCalculationUtil.calculateTimeSpentInLocation(realmResults);
//        Log.i(TAG," mins: "+mins);
//        PerDayAudit perDayAudit= new PerDayAudit();
//        perDayAudit.setDay(todayWithZeroTime);
//        perDayAudit.setTimeSpentString(TimeCalculationUtil.hoursAndMinutes(mins));
//        perDayAudit.setTimeSpentPercentage((int)(mins*100)/(24*60));
//        realmResults.sort("timeStamp");
//        perDayAudit.setFirstSeen(new Date(realmResults.get(0).getTimeStamp()));
//        perDayAudit.setLastSeen(new Date(realmResults.get(realmResults.size()-1).getTimeStamp()));
//
//        perDayAuditList.add(perDayAudit);
//        mAdapter.setList(perDayAuditList);
//        todayWithZeroTime.setDate(26);
//        RealmResults<LocationAudit> realmResults1=realmService.getLocationAuditByPlaceAndDate(locationId,todayWithZeroTime);
//        if(realmResults1.size()<1){
//            mAdapter.setList(perDayAuditList);
//            view.setAdapter(mAdapter);
//            Log.i(TAG,"setting the adapter with size empty"+mAdapter.getList().size());
//            return;
//        }
//        mins= TimeCalculationUtil.calculateTimeSpentInLocation(realmResults1);
//        Log.i(TAG," mins: "+mins);
//        PerDayAudit perDayAudit1= new PerDayAudit();
//        perDayAudit1.setDay(todayWithZeroTime);
//        perDayAudit1.setTimeSpentString(TimeCalculationUtil.hoursAndMinutes(mins));
//        perDayAudit1.setTimeSpentPercentage((int)(mins*100)/(24*60));
//        realmResults1.sort("timeStamp");
//        perDayAudit1.setFirstSeen(new Date(realmResults1.get(0).getTimeStamp()));
//        perDayAudit1.setLastSeen(new Date(realmResults1.get(realmResults1.size()-1).getTimeStamp()));
//        perDayAuditList.add(perDayAudit1);
//        mAdapter.setList(perDayAuditList);
//        Log.i(TAG,"setting the adapter with size "+mAdapter.getList().size());
//        view.setAdapter(mAdapter);
//    }

    public void readBundle(Bundle bundle){
        title =bundle.getString("name");
        locationId=bundle.getLong("id");
        latitude=bundle.getDouble("latitude");
        longitude=bundle.getDouble("longitude");
    }

    @Override
    public void onMapReady() {
        view.onMapReady(new LatLng(latitude,longitude),title);
    }
}
