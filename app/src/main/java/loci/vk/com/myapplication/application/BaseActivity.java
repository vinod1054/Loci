package loci.vk.com.myapplication.application;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import loci.vk.com.myapplication.utils.SharedPreferencesUtil;

/**
 * Created by vinod on 15/2/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    public LocationManager locationManager;
    @Inject
    public SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getModule()!=null)
            ((LociApplication) (getApplication())).injectModules(this);
    }

    @Override
    protected void onDestroy() {
        closeRealm();
        super.onDestroy();
    }

    public void getPresenterInjections(Object object){
        ((LociApplication) (getApplication())).injectModules(object);
    }

    protected abstract Object getModule();

    protected abstract void closeRealm();
}