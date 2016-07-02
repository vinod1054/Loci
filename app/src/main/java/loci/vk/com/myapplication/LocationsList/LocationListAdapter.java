package loci.vk.com.myapplication.LocationsList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import loci.vk.com.myapplication.R;
import loci.vk.com.myapplication.TestPurpose.RealmService;
import loci.vk.com.myapplication.model.PerDayAudit;
import loci.vk.com.myapplication.touchhelper.TouchListenerPresenter;
import loci.vk.com.myapplication.utils.TimeCalculationUtil;
import loci.vk.com.myapplication.views.CircleView;

/**
 * Created by vinod on 18/2/16.
 */
public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder>
        implements RealmChangeListener,View.OnClickListener,
        TouchListenerPresenter {


    public static final String TAG="LocationListAdapter";

    RealmService realmService;
    LocationPresenter presenter;

    private RealmResults<PerDayAudit> mLocations;
    private List<PerDayAudit> mLocationsWeakRef=new ArrayList<>();
    PerDayAudit lastDeletedItem;
    int lastDeletedLocation;


    private OnLocationClickListener mOnLocationClickListener;
    LocationListView view;

    public LocationListAdapter(final RealmService realmService, final LocationPresenter presenter) {
        this.realmService=realmService;
        this.presenter=presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        long mins= mLocationsWeakRef.get(position).getTimeSpent()/(1000*60);
        Log.i("vinod ","mins "+mins);
        int timePercentage=(int)(((double)mins/(24*60))*100);
        Log.i("vinod percentage",timePercentage+" ");
        holder.circleView.setPercentage(timePercentage);
        holder.mTextTitle.setText(mLocationsWeakRef.get(position).getName());
        holder.hoursSpent.setText(TimeCalculationUtil.hoursAndMinutes(mins));
        if(mLocationsWeakRef.get(position).getLastSeen()!=null)
            holder.mTextDetails.setText("last seen "+TimeCalculationUtil.gethhmma(mLocationsWeakRef.get(position).getLastSeen()));
        else
            holder.mTextDetails.setText("No Info yet Today ");
        holder.mRealLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mOnLocationClickListener != null) {
                    mOnLocationClickListener.onLocationClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLocationsWeakRef.size();
    }

    @Override
    public void onChange() {
        Log.i(TAG," Data set onChange");
        mLocationsWeakRef.clear();
        mLocationsWeakRef.addAll(mLocations);
        notifyDataSetChanged();
    }

    public void setLocations(final RealmResults<PerDayAudit> locations) {
        mLocations = locations;
        mLocations.sort("lastSeen", Sort.DESCENDING);
        locations.addChangeListener(this);
        mLocationsWeakRef.addAll(locations);
        notifyDataSetChanged();
        if(locations.size()==0)
            view.showNoGroupsText(View.VISIBLE);
        else
            view.showNoGroupsText(View.GONE);
    }


    public void setOnLocationClickListener(final OnLocationClickListener onLocationClickListener) {
        mOnLocationClickListener = onLocationClickListener;
    }

    @Override
    public void swiped(int position) {
        lastDeletedItem=mLocationsWeakRef.get(position);
        realmService.addUpdateAsDone(null,lastDeletedItem.getLocationId(),1);
        mLocationsWeakRef.remove(position);
        notifyItemRemoved(position);
        lastDeletedLocation=position;
        view.showSnackMessage(this,lastDeletedItem.getName());
//        presenter.showSwipeDelteSnackNotification(this,lastDeletedItem.getName());
    }

    @Override
    public void onClick(View v) {
        mLocationsWeakRef.add(lastDeletedLocation,lastDeletedItem);
        notifyItemInserted(lastDeletedLocation);
        realmService.addUpdateAsDone(null,lastDeletedItem.getLocationId(),0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_item_container)
        LinearLayout mLayoutItem;
        @Bind(R.id.layout_item_container_real)
        LinearLayout mRealLayout;
        @Bind(R.id.text_title)
        TextView mTextTitle;
        @Bind(R.id.text_details)
        TextView mTextDetails;
        @Bind(R.id.hours_spent)
        TextView hoursSpent;
        @Bind(R.id.circle_view)
        CircleView circleView;
//        @Bind(R.id.hidden_view)
//        RelativeLayout relativeLayout;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnLocationClickListener {
        void onLocationClick(int id);
    }

}
