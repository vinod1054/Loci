package loci.vk.com.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vinod on 17/2/16.
 */
public class SharedPreferencesUtil {

    static Context context;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String name = "userName";
    public static final String email = "email";
    public static final String phone = "phone";
    public static final String isPerDayAuditCreatedToday = "isPerDayAuditCreatedToday";
    static SharedPreferences sharedpreferences;

    public SharedPreferencesUtil(Context context){
        this.context=context;
        sharedpreferences= context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }


    public void setUserNameAndEmail(String userName, String email){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(name, userName);
        editor.putString(SharedPreferencesUtil.email, email);
        editor.commit();
    }

    public void setKeyValuePair(String key,boolean value){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setKeyValuePair(String key,long value){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void setKeyValuePair(String key,String value){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public boolean getValueForKey(String key){
            final SharedPreferences settings = context.getSharedPreferences(MyPREFERENCES, 0);
            return settings.getBoolean(key, false);
    }
    public long getValueForKeyLong(String key){
        final SharedPreferences settings = context.getSharedPreferences(MyPREFERENCES, 0);
        return settings.getLong(key, -1);
    }

    public boolean getValueForKeyBoolean(String key){
        final SharedPreferences settings = context.getSharedPreferences(MyPREFERENCES, 0);
        return settings.getBoolean(key, false);
    }

    public String getUserName(){
        final SharedPreferences settings = context.getSharedPreferences(MyPREFERENCES, 0);
        return settings.getString(name, null);
    }
    public String getEmail(){
        final SharedPreferences settings = context.getSharedPreferences(MyPREFERENCES, 0);
        return settings.getString(email, null);
    }

    public boolean getIsPerDayAuditCreatedToday(){
        final SharedPreferences settings = context.getSharedPreferences(MyPREFERENCES, 0);
        return settings.getBoolean(isPerDayAuditCreatedToday, false);
    }

    public void setIsPerDayAuditCreatedToday(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(isPerDayAuditCreatedToday, true);
        editor.commit();
    }

    public void deleteKey(String key){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(key);
        editor.commit();
    }


}
