package loci.vk.com.myapplication.LocationsList;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import loci.vk.com.myapplication.R;
import loci.vk.com.myapplication.addnewlocation.AddNewLocationActivity;
import loci.vk.com.myapplication.application.BaseActivity;
import loci.vk.com.myapplication.locationdetails.LocationDestailsActivity;
import loci.vk.com.myapplication.model.PerDayAudit;
import loci.vk.com.myapplication.modules.LocationsModule;
import loci.vk.com.myapplication.services.LocationTrackerService;
import loci.vk.com.myapplication.touchhelper.RecycletViewItemTouchHelper;

public class LocationListActivity extends BaseActivity implements LocationListView, LocationListAdapter.OnLocationClickListener{


    final String TAG="LocationListActivity";

    @Bind(R.id.locations_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.no_groups_found)
    TextView noGroupsFoundTextView;
    @Bind(R.id.root_coord_layout)
    CoordinatorLayout rootLayout;

    @Inject
    LocationPresenter mLocationsPresenter;

    @Inject
    LocationListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        ButterKnife.bind(this);
        mAdapter.view=this;
        initToolbar();
        initList();
        startService();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"Setting the view in presenter");
        mLocationsPresenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationsPresenter.clearView();
    }


    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.location_wait));
        getSupportActionBar().setTitle(" Loci");
    }

    private void initList() {
        mAdapter.setOnLocationClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecycletViewItemTouchHelper(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected Object getModule() {
        return new LocationsModule();
    }

    @Override
    protected void closeRealm() {

    }

    @Override
    public void showLocations(RealmResults<PerDayAudit> locations) {
        mAdapter.setLocations(locations);
    }

    @Override
    public void showLocationDetailView(Bundle bundle) {
        Intent intent=new Intent(this, LocationDestailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showAddNewLocationView() {
        Intent intent = new Intent(this, AddNewLocationActivity.class);
        startActivity(intent);
    }

    @Override
    public void showNoGroupsText(int visibility) {
        noGroupsFoundTextView.setVisibility(visibility);
    }

    @Override
    public View getRootView() {
        return rootLayout;
    }

    @Override
    public void showSnackMessage(View.OnClickListener listener, String locationName) {
        Snackbar.make(rootLayout,"\'"+locationName+"\' has been deleted",Snackbar.LENGTH_LONG)
                .setAction("UNDO",listener)
                .show();
    }

    @Override
    public void onLocationClick(int id) {
        mLocationsPresenter.onLocationClick(id);
    }

    @OnClick(R.id.fab)
    public void onAddNewLocationClick() {
        mLocationsPresenter.onAddNewLocationClick();
    }

    public void startService(){
        Log.i(TAG,"Starting location background Service");
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                startService(new Intent(LocationListActivity.this, LocationTrackerService.class));
            }
        };
        runnable.run();
    }

}
