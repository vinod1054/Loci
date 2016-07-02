package loci.vk.com.myapplication.locationdetails;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import loci.vk.com.myapplication.R;
import loci.vk.com.myapplication.model.LocationAudit;
import loci.vk.com.myapplication.model.PerDayAudit;
import loci.vk.com.myapplication.utils.TimeCalculationUtil;
import loci.vk.com.myapplication.views.CircleView;

/**
 * Created by vinod on 26/2/16.
 */
public class LocationDetailsAdapter extends RecyclerView.Adapter<LocationDetailsAdapter.ViewHolder> {

    final String TAG="MyDetailAdapter";

    private RealmResults<LocationAudit> realmResults;

    List<PerDayAudit> list=new ArrayList<>();

    public List<PerDayAudit> getList() {
        return list;
    }

    public void setList(List<PerDayAudit> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public LocationDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG,"onCreateViewHolder ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_per_day_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationDetailsAdapter.ViewHolder holder, int position) {
        Log.i(TAG,"onBindViewHolder ");
        PerDayAudit perDayAudit=list.get(position);
        String theDayText=TimeCalculationUtil.getDisplayDate(perDayAudit.getDay().getTime());
        holder.theDay.setText(theDayText);
        if(theDayText.charAt(0)!='T' && theDayText.charAt(0)!='Y')
            holder.dayInWeek.setText(TimeCalculationUtil.getDayInWeek(perDayAudit.getDay()));
        long mins=perDayAudit.getTimeSpent()/(1000*60);
        int percentage=((int)(mins*100)/(24*60));
        Log.i(TAG,"percentage "+percentage);
        Log.i(TAG, "TimeSpentString " + perDayAudit.getTimeSpentString());
        holder.circleView.setPercentage(percentage);
        if(perDayAudit.getLastSeen()!=null)
            holder.lastSeen.setText(TimeCalculationUtil.gethhmma(perDayAudit.getLastSeen()));
        else
            holder.lastSeen.setText("Not yet seen");
        if(perDayAudit.getFirstSeen()!=null)
            holder.firstSeen.setText(TimeCalculationUtil.gethhmma(perDayAudit.getFirstSeen()));
        else
            holder.firstSeen.setText("Not yet seen");
        holder.timeSpentHrs.setText(TimeCalculationUtil.hoursAndMinutes(mins));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.circle_view)
        CircleView circleView;
        @Bind(R.id.last_seen)
        TextView lastSeen;
        @Bind(R.id.first_seen)
        TextView firstSeen;
        @Bind(R.id.time_spent_hrs)
        TextView timeSpentHrs;
        @Bind(R.id.the_day)
        TextView theDay;
        @Bind(R.id.day_in_week)
        TextView dayInWeek;
        public ViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG,"ViewHolder");
            ButterKnife.bind(this, itemView);
        }
    }
}
