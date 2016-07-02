package loci.vk.com.myapplication.LocationsList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.realm.RealmResults;
import loci.vk.com.myapplication.TestPurpose.RealmService;
import loci.vk.com.myapplication.model.PerDayAudit;

/**
 * Created by vinod on 18/2/16.
 */
public class LocationPresenterImpl implements LocationPresenter,RealmService.OnTransactionCallback {

    private final RealmService mRealmService;
    private RealmResults<PerDayAudit> perDayAuditRealmResults;
    private LocationListView mMyListView; //= new LocationListView.EmptyMyListView();
    private boolean usersWereShown = false;
    final String TAG="LocationPresenter";

    public LocationPresenterImpl(final RealmService realmService) {
        mRealmService = realmService;
    }

    @Override
    public void onLocationClick(int id) {
        Bundle bundle = new Bundle();
        bundle.putString("name",perDayAuditRealmResults.get(id).getName());
        bundle.putLong("id",perDayAuditRealmResults.get(id).getLocationId());
        bundle.putDouble("latitude",perDayAuditRealmResults.get(id).getLatitude());
        bundle.putDouble("longitude",perDayAuditRealmResults.get(id).getLongitude());
        mMyListView.showLocationDetailView(bundle);
    }

    @Override
    public void onAddNewLocationClick() {
        mMyListView.showAddNewLocationView();}

    @Override
    public void showSwipeDelteSnackNotification(View.OnClickListener listener, String locationName) {
        if(mMyListView==null){
            Log.e(TAG,"view is null");
            return;
        }
        mMyListView.showSnackMessage(listener,locationName);
//        Snackbar.make(mMyListView.getRootView(),"\'"+locationName+"\' has been deleted",Snackbar.LENGTH_LONG)
//                .setAction("UNDO",listener)
//                .show();
    }

    @Override
    public void setView(LocationListView view) {
        Log.i(TAG,"View has been set");
        mMyListView = view;
        showLocationsIfNeeded();
    }

    private void showLocationsIfNeeded() {

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if(!usersWereShown) {
                    //It has change callbacks in the adapter. So be careful while modifying the Collection
                    perDayAuditRealmResults=mRealmService.getAllPerDayLocationsForToday();
                    mMyListView.showLocations(perDayAuditRealmResults);
                    usersWereShown = true;
                }
            }
        };
        runnable.run();
    }

    @Override
    public void setView(Object view) {

    }

    @Override
    public void clearView() {
        mMyListView = new LocationListView.EmptyMyListView();
    }

    @Override
    public void closeRealm() {
        mRealmService.closeRealm();
    }


    @Override
    public void onRealmSuccess() {
        Log.i("vinod ","success");
    }

    @Override
    public void onRealmError(Exception e) {
        Log.i("vinod ",e.toString());
    }
}
