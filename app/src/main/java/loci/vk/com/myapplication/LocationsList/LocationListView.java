package loci.vk.com.myapplication.LocationsList;

import android.os.Bundle;
import android.view.View;

import io.realm.RealmResults;
import loci.vk.com.myapplication.model.PerDayAudit;

/**
 * Created by vinod on 18/2/16.
 */
public interface LocationListView {

    void showLocations(RealmResults<PerDayAudit> locationses);
    void showLocationDetailView(Bundle bundle);
    void showAddNewLocationView();
    void showNoGroupsText(int visibility);
    View getRootView();
    void showSnackMessage(View.OnClickListener listener,String locationName);

    class EmptyMyListView implements LocationListView {

        @Override
        public void showLocations(RealmResults<PerDayAudit> locationses) {

        }

        @Override
        public void showLocationDetailView(Bundle bundle) {

        }

        @Override
        public void showAddNewLocationView() {

        }

        @Override
        public void showNoGroupsText(int visibility) {

        }

        @Override
        public View getRootView() {
            return null;
        }

        @Override
        public void showSnackMessage(View.OnClickListener listener, String locationName) {

        }
    }
}

