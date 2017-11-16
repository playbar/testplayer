package com.baofeng.mj.unity.launcher;

import android.content.Context;

import com.baofeng.mj.vrplayer.MyAppliaction;
import com.bfmj.sdk.util.SecurePreferences;


public class SharedPreferencesUtil {
    private SecurePreferences mSharedPreferences;
    private SecurePreferences.Editor mEditor;
    private Context mContext;
    private static SharedPreferencesUtil instance;

    public static final String KEY_IS_FIRST_INSTALL_APP = "key_is_first_install_app";


    public static SharedPreferencesUtil getInstance() {
        if (instance != null)
            return instance;
        return instance = new SharedPreferencesUtil(MyAppliaction.getInstance());
    }

    private SharedPreferencesUtil(Context context) {
        mContext = context;
        mSharedPreferences = new SecurePreferences(context);
        if (mSharedPreferences != null) {
            mEditor = mSharedPreferences.edit();
        }
    }

    public void setString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void setInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void setBoolean(String key, Boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public void setByte(String key, byte[] value) {
        setString(key, String.valueOf(value));
    }

    public void setShort(String key, short value) {
        setString(key, String.valueOf(value));
    }

    public void setLong(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public void setFloat(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public void setDouble(String key, double value) {
        setString(key, String.valueOf(value));
    }

    public void setString(int resID, String value) {
        setString(this.mContext.getString(resID), value);

    }

    public void setInt(int resID, int value) {
        setInt(this.mContext.getString(resID), value);
    }

    public void setBoolean(int resID, Boolean value) {
        setBoolean(this.mContext.getString(resID), value);
    }

    public void setByte(int resID, byte[] value) {
        setByte(this.mContext.getString(resID), value);
    }

    public void setShort(int resID, short value) {
        setShort(this.mContext.getString(resID), value);
    }

    public void setLong(int resID, long value) {
        setLong(this.mContext.getString(resID), value);
    }

    public void setFloat(int resID, float value) {
        setFloat(this.mContext.getString(resID), value);
    }

    public void setDouble(int resID, double value) {
        setDouble(this.mContext.getString(resID), value);
    }

    public String getString(String key, String defaultValue) {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    public boolean getBoolean(String key, Boolean defaultValue) {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    public byte[] getByte(String key, byte[] defaultValue) {
        try {
            return getString(key, "").getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public short getShort(String key, Short defaultValue) {
        try {
            return Short.valueOf(getString(key, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public long getLong(String key, Long defaultValue) {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    public float getFloat(String key, Float defaultValue) {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getFloat(key, defaultValue);
        }
        return defaultValue;
    }

    public double getDouble(String key, Double defaultValue) {
        try {
            return Double.valueOf(getString(key, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public String getString(int resID, String defaultValue) {
        return getString(this.mContext.getString(resID), defaultValue);
    }

    public int getInt(int resID, int defaultValue) {
        return getInt(this.mContext.getString(resID), defaultValue);
    }

    public boolean getBoolean(int resID, Boolean defaultValue) {
        return getBoolean(this.mContext.getString(resID), defaultValue);
    }

    public byte[] getByte(int resID, byte[] defaultValue) {
        return getByte(this.mContext.getString(resID), defaultValue);
    }

    public short getShort(int resID, Short defaultValue) {
        return getShort(this.mContext.getString(resID), defaultValue);
    }

    public long getLong(int resID, Long defaultValue) {
        return getLong(this.mContext.getString(resID), defaultValue);
    }

    public float getFloat(int resID, Float defaultValue) {
        return getFloat(this.mContext.getString(resID), defaultValue);
    }

    public double getDouble(int resID, Double defaultValue) {
        return getDouble(this.mContext.getString(resID), defaultValue);
    }

    public void remove(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    public void remove(String... keys) {
        for (String key : keys)
            remove(key);
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
    }


    public boolean getIsFirstInstall() {
        return getBoolean(KEY_IS_FIRST_INSTALL_APP, true);
    }

    public void setIsFirstInstall(boolean isFirstInstall) {
        setBoolean(KEY_IS_FIRST_INSTALL_APP, isFirstInstall);
    }
}
