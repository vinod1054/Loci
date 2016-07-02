package loci.vk.com.myapplication.LocationsList;

import android.view.View;

import loci.vk.com.myapplication.application.BasePresenter;

/**
 * Created by vinod on 18/2/16.
 */
public interface LocationPresenter extends BasePresenter {
    void onLocationClick(int id);
    void onAddNewLocationClick();
    void showSwipeDelteSnackNotification(View.OnClickListener listener,String locationName);
    void setView(LocationListView view);
}
