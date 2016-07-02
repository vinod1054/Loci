package loci.vk.com.myapplication.TestPurpose;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import loci.vk.com.myapplication.model.LocationAudit;
import loci.vk.com.myapplication.model.MyLocation;
import loci.vk.com.myapplication.model.PerDayAudit;
import loci.vk.com.myapplication.model.User;
import loci.vk.com.myapplication.utils.TimeCalculationUtil;

/**
 * Created by vinod on 15/2/16.
 */
public class  RealmService {
    private final Realm mRealm;

    final String TAG="MyRealmService";

    public RealmService(final Realm realm) {
        mRealm = realm;
    }

    public void closeRealm() {
        mRealm.close();
    }

    public RealmResults<User> getAllUsers() {
        return mRealm.allObjects(User.class);
    }
    public RealmResults<MyLocation> getAllLocations() {
        return mRealm.allObjects(MyLocation.class);
    }

    public RealmResults<PerDayAudit> getAllPerDayLocationsForToday() {
        return mRealm.allObjects(PerDayAudit.class).where().equalTo("status",0).equalTo("day", TimeCalculationUtil.getToday()).findAll();
    }

    public RealmResults<PerDayAudit> getAllDaysLocations(long locationId) {
        RealmResults<PerDayAudit> realmResults= mRealm.allObjects(PerDayAudit.class).where()
                .equalTo("locationId", locationId)
                .isNotNull("lastSeen")
                .findAll();
        realmResults.sort("day", Sort.DESCENDING);
        return  realmResults;
    }





    public Date getFirstDateForLocation(MyLocation myLocation){
        RealmQuery<LocationAudit> query=mRealm.where(LocationAudit.class);
        query.equalTo("myLocationId", myLocation.getId());
        LocationAudit locationAudit =query.findFirst();
        return new Date(locationAudit.getTimeStamp());
    }

    public Date getLastDateForLocation(MyLocation myLocation){
        RealmQuery<LocationAudit> query=mRealm.where(LocationAudit.class);
        query.equalTo("myLocationId", myLocation.getId());
        LocationAudit locationAudit =query.findFirst();
        return new Date(locationAudit.getTimeStamp());
    }

    public RealmResults<LocationAudit> getLocationAuditByPlace(long placeId) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        RealmQuery<LocationAudit> query=mRealm.where(LocationAudit.class);
        query.equalTo("myLocationId", placeId).greaterThanOrEqualTo("timeStamp",todayWithZeroTime.getTime());
        RealmResults<LocationAudit> realmResults=query.findAll();
        return realmResults;
    }


    public RealmResults<LocationAudit> getLocationAuditByPlaceAndDate(long placeId,Date calendar) {
        RealmQuery<LocationAudit> query=mRealm.where(LocationAudit.class);
        Date nextDay=new Date(calendar.getTime());
        nextDay.setDate(calendar.getDate()+1);
        query.equalTo("myLocationId", placeId).greaterThanOrEqualTo("timeStamp",calendar.getTime()).lessThan("timeStamp",nextDay.getTime());
        RealmResults<LocationAudit> realmResults=query.findAll();
        return realmResults;
    }



    //other methods

    public void addUpdateAsDone(final OnTransactionCallback onTransactionCallback, final long locationId,final int status) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                RealmQuery<PerDayAudit> query=realm.where(PerDayAudit.class)
                        .equalTo("locationId", locationId)
                        .equalTo("day", TimeCalculationUtil.getToday());
                query.findFirst().setStatus(status);
                realm.where(MyLocation.class).equalTo("id",locationId).findFirst().setStatus(status);
            }
        }, new Realm.Transaction.Callback() {

            @Override
            public void onSuccess() {
                Log.i(TAG,"updated");
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }

            @Override
            public void onError(final Exception e) {
                Log.i(TAG,"updated failed "+e.toString() );
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmError(e);
                }
            }
        });
    }



    public void addUserAsync(final OnTransactionCallback onTransactionCallback) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                User user = realm.createObject(User.class);
               user.setlName("Kollipara");
               user.setfName("Vinod");
            }
        }, new Realm.Transaction.Callback() {

            @Override
            public void onSuccess() {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }

            @Override
            public void onError(final Exception e) {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmError(e);
                }
            }
        });
    }

    public void updateDistance(final OnTransactionCallback onTransactionCallback,
                               final long locationId,
                               final long timeStamp,
                               final long amountToAdd,
                               final boolean canIAdd) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                PerDayAudit perDayAudit = realm.where(PerDayAudit.class)
                        .equalTo("id", timeStamp+","+locationId ).findFirst();
                if(perDayAudit.getFirstSeen()==null)
                    perDayAudit.setFirstSeen(new Date());
                perDayAudit.setLastSeen(new Date());
                if (canIAdd)
                    perDayAudit.setTimeSpent(perDayAudit.getTimeSpent()+amountToAdd);
                Log.i(TAG,"canIAdd "+canIAdd);
                Log.i(TAG,"changed distance in terms of ms "+perDayAudit.getTimeSpent()+"");
                Log.i(TAG,"changed distance in terms of mins "+perDayAudit.getTimeSpent()/(1000*60)+"");
            }
        }, new Realm.Transaction.Callback() {

            @Override
            public void onSuccess() {
                Log.i(TAG,"Storing into PerDayTable sucesss "+amountToAdd);
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }

            @Override
            public void onError(final Exception e) {
                Log.i(TAG,"Storing into PerDayTable failed "+e.toString());
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmError(e);
                }
            }
        });
    }




    public void addPerDayAuditAsync(final OnTransactionCallback onTransactionCallback, final PerDayAudit perDayAudit) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                realm.copyToRealm(perDayAudit);
            }
        }, new Realm.Transaction.Callback() {

            @Override
            public void onSuccess() {
                Log.i(TAG," stored successfully "+perDayAudit.getName()+" "+perDayAudit.getDay());
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }

            @Override
            public void onError(final Exception e) {
                Log.i(TAG," storing Failed "+perDayAudit.getName()+" "+perDayAudit.getDay()+" "+e.toString());
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmError(e);
                }
            }
        });
    }



    public void addLocationAsync(final OnTransactionCallback onTransactionCallback, final MyLocation myLocation) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                realm.copyToRealm(myLocation);

            }
        }, new Realm.Transaction.Callback() {

            @Override
            public void onSuccess() {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                    Log.i("vinod ", "stored Successfully");
                }
            }

            @Override
            public void onError(final Exception e) {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmError(e);
                    Log.i("vinod ", e.toString());
                }
            }
        });
    }


    public void deleteUserAsync(final OnTransactionCallback onTransactionCallback) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                realm.clear(User.class);
            }
        }, new Realm.Transaction.Callback() {

            @Override
            public void onSuccess() {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }

            @Override
            public void onError(final Exception e) {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmError(e);
                }
            }
        });
    }


    public void addLocationAuditAsync(final OnTransactionCallback onTransactionCallback, final LocationAudit locationAudit) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                realm.copyToRealm(locationAudit);

            }
        }, new Realm.Transaction.Callback() {

            @Override
            public void onSuccess() {
                Log.i("vinod ", "stored Successfully");
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }

            @Override
            public void onError(final Exception e) {
                Log.i("vinod ", e.toString());
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmError(e);
                }
            }
        });
    }


    //other methods

    public interface OnTransactionCallback {
        void onRealmSuccess();
        void onRealmError(final Exception e);
    }
}
