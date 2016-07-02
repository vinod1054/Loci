package loci.vk.com.myapplication.TestPurpose;

/**
 * Created by vinod on 15/2/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import loci.vk.com.myapplication.R;
import loci.vk.com.myapplication.model.User;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> implements RealmChangeListener {

    private RealmResults<User> mUsers;
    private OnUserClickListener mOnUserClickListener;

    public UserListAdapter() {}

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final User user = mUsers.get(position);

        holder.mTextTitle.setText(user.getfName());
        holder.mTextDetails.setText(user.getlName());
        holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mOnUserClickListener != null) {
                    mOnUserClickListener.onUserClick(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    @Override
    public void onChange() {
        notifyDataSetChanged();
    }

    public void setOnUserClickListener(final OnUserClickListener onUserClickListener) {
        mOnUserClickListener = onUserClickListener;
    }

    public void setUsers(final RealmResults<User> users) {
        mUsers = users;
        mUsers.addChangeListener(this);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_item_container) LinearLayout mLayoutItem;
        @Bind(R.id.text_title) TextView mTextTitle;
        @Bind(R.id.text_details) TextView mTextDetails;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnUserClickListener {
        void onUserClick(int id);
    }
}
