package loci.vk.com.myapplication.locationdetails;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import loci.vk.com.myapplication.R;
import loci.vk.com.myapplication.application.BaseActivity;
import loci.vk.com.myapplication.model.PerDayAudit;

public class LocationDestailsActivity extends BaseActivity implements
        OnMapReadyCallback,LocationDetailsView {

    final String TAG="MyLocationDestails";

    private GoogleMap googleMap;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.no_data_found)
    TextView noDataFound;
    private MapFragment mapFragment;
    private LocationDetailsPresenter presenter;
    GoogleApiClient mGoogleApiClient;
    LocationDetailsAdapter mAdapter=new LocationDetailsAdapter();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_destails);
        presenter=new LocationDetailsPresenterImpl(this);
        super.getPresenterInjections(presenter);
        ButterKnife.bind(this);
        Log.i(TAG,"settingUp setUpRecyclerView");
        setUpRecyclerView();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .build();
        setUpMap();
        setUpToolbar();
        presenter.onCreate(getIntent().getExtras());

    }

    public void setUpMap(){
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void setUpToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));
    }

    public void setUpRecyclerView(){
        Log.i(TAG,"setUpRecyclerView");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    protected Object getModule() {
        return null;
    }

    @Override
    protected void closeRealm() {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        presenter.onMapReady();
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
        mCollapsingToolbarLayout.setTitle(title);
    }

    @Override
    public void setAdapter(List<PerDayAudit> list) {
        mAdapter.setList(list);
        Log.i(TAG,"Adapter has been set to mRecyclerView");
        if(list.size()==0)
            noDataFound.setVisibility(View.VISIBLE);
        else
            noDataFound.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(LatLng latLng,String title) {
        setMapToLocation(latLng,title);
    }

    @Override
    public void setMapToLocation(LatLng latLng, String address) {
        if(googleMap==null)
            return ;
        else{
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng).title(address));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }
    }


}
