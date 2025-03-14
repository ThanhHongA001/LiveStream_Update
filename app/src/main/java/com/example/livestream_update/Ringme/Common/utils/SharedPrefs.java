package com.example.livestream_update.Ringme.Common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.values.Constants;

import java.lang.reflect.Type;

public class SharedPrefs {
    private final String TAG = "SharedPrefs";
    private static SharedPrefs mInstance;
    private SharedPreferences mSharedPreferences;
    private String mPreferencesName;
    public static final String HOME_NEW_V5 = "home_new_v5";
    public static final String KEY_FIRST_OPEN_APP = "KEY_FIRST_OPEN_APP";
    public static final String KEY_DATE_OPEN_APP = "KEY_DATE_OPEN_APP";
    public static final String KEY_OPEN_APP_IN_DAY = "KEY_OPEN_APP_IN_DAY";

    public static final String AUTO_PLAY_VIDEO = "AUTO_PLAY_VIDEO";
    private SharedPrefs() {

    }

    public static SharedPrefs getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPrefs();
            mInstance.mPreferencesName = Constants.PREFERENCE.PREF_DIR_NAME;
            mInstance.mSharedPreferences = ApplicationController.self().getPref();
            if (mInstance.mSharedPreferences == null) {
                mInstance.mSharedPreferences = ApplicationController.self().getApplicationContext()
                        .getSharedPreferences(mInstance.mPreferencesName, Context.MODE_PRIVATE);
            }
        }
        return mInstance;
    }

    private static SharedPrefs newInstance(Context context, String preferencesName) {
        SharedPrefs item = new SharedPrefs();
        item.mPreferencesName = preferencesName;
        if (context != null)
            item.mSharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        else
            item.mSharedPreferences = ApplicationController.self().getApplicationContext()
                    .getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return item;
    }

    public static SharedPrefs getInstance(String preferencesName) {
        return newInstance(ApplicationController.self(), preferencesName);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> anonymousClass) {
        if (mSharedPreferences != null) {
            try {
                if (anonymousClass == String.class) {
                    return (T) mSharedPreferences.getString(key, "");
                } else if (anonymousClass == Boolean.class) {
                    return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, false));
                } else if (anonymousClass == Float.class) {
                    return (T) Float.valueOf(mSharedPreferences.getFloat(key, 0));
                } else if (anonymousClass == Integer.class) {
                    return (T) Integer.valueOf(mSharedPreferences.getInt(key, 0));
                } else if (anonymousClass == Long.class) {
                    return (T) Long.valueOf(mSharedPreferences.getLong(key, 0));
                } else {
                    return ApplicationController.self()
                            .getGson()
                            .fromJson(mSharedPreferences.getString(key, ""), anonymousClass);
                }
            } catch (OutOfMemoryError e) {
                Log.e(TAG, e);
            } catch (NoSuchMethodError e) {
                Log.e(TAG, e);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Type typeOfT) {
        if (mSharedPreferences != null) {
            try {
                return ApplicationController.self()
                        .getGson()
                        .fromJson(mSharedPreferences.getString(key, ""), typeOfT);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (NoSuchMethodError e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> anonymousClass, T defaultValue) {
        if (mSharedPreferences != null) {
            try {
                if (anonymousClass == String.class) {
                    return (T) mSharedPreferences.getString(key, (String) defaultValue);
                } else if (anonymousClass == Boolean.class) {
                    return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, (Boolean) defaultValue));
                } else if (anonymousClass == Float.class) {
                    return (T) Float.valueOf(mSharedPreferences.getFloat(key, (Float) defaultValue));
                } else if (anonymousClass == Integer.class) {
                    return (T) Integer.valueOf(mSharedPreferences.getInt(key, (Integer) defaultValue));
                } else if (anonymousClass == Long.class) {
                    return (T) Long.valueOf(mSharedPreferences.getLong(key, (Long) defaultValue));
                } else {
                    return ApplicationController.self()
                            .getGson()
                            .fromJson(mSharedPreferences.getString(key, ""), anonymousClass);
                }
            } catch (OutOfMemoryError e) {
                Log.e(TAG, e);
            } catch (NoSuchMethodError e) {
                Log.e(TAG, e);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
        return defaultValue;
    }

    public <T> void put(String key, T data) {
        if (mSharedPreferences != null) {
            try {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                if (data instanceof String) {
                    editor.putString(key, (String) data);
                } else if (data instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) data);
                } else if (data instanceof Float) {
                    editor.putFloat(key, (Float) data);
                } else if (data instanceof Integer) {
                    editor.putInt(key, (Integer) data);
                } else if (data instanceof Long) {
                    editor.putLong(key, (Long) data);
                } else {
                    editor.putString(key, ApplicationController.self().getGson().toJson(data));
                }
                editor.apply();
            } catch (OutOfMemoryError e) {
                Log.e(TAG, e);
            } catch (NoSuchMethodError e) {
                Log.e(TAG, e);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
    }

    public void clear() {
        Log.i(TAG, "clear preferencesName \"" + mPreferencesName + "\"");
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().clear().apply();
        }
    }

    public void remove(String key) {
        Log.i(TAG, "remove key \"" + key + "\" of preferencesName \"" + mPreferencesName + "\"");
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().remove(key).apply();
        }
    }

    public static class KEY {
        public static final String PERMISSION_EVER_REQUESTED = "EverRequestedPermission";

        public static final String BACKUP_LAST_TIME = "BackupLastTime";
        public static final String BACKUP_SETTING_NETWORK_ONLY_WIFI = "BackupOnlyWiFi";
        public static final String BACKUP_SETTING_AUTO_TYPE = "BackupAutoType";
        public static final String BACKUP_PASSED_RESTORE_PHASE = "BackupPassedRestorePhase";
    }
}
