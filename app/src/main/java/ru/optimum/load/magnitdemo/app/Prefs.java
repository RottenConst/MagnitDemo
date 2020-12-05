package ru.optimum.load.magnitdemo.app;

import android.content.SharedPreferences;

import ru.optimum.load.magnitdemo.R;

public class Prefs {

    public static String getServerAddress() {
        String key = DemoApp.app().getString(R.string.pref_key_server_ip);
        return Prefs.getStringPreference(key, "");
    }

    public static int getServerPort() {
        String key = DemoApp.app().getString(R.string.pref_key_server_port);
        String serverPort = Prefs.getStringPreference(key, "0");
        int port = 0;
        try {
            port = Integer.parseInt(serverPort);
            return port;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean getUseSecureConnection() {
        String key = DemoApp.app().getString(R.string.pref_key_use_secure_connection);
        boolean useSecureConnetction = Prefs.getBooleanPreference(key, false);
        return useSecureConnetction;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // PREFERENCE GETTING & SETTING
    ////////////////////////////////////////////////////////////////////////////////////////////
    private static final String getStringPreference(String key, String defValue) {
        final SharedPreferences settings = DemoApp.getSharedPreferences();
        return settings.getString(key, defValue);
    }

    private static final boolean getBooleanPreference(String key, boolean defValue) {
        final SharedPreferences settings = DemoApp.getSharedPreferences();
        return settings.getBoolean(key, defValue);
    }

    private static final int getIntPreference(String key, int defValue) {
        final SharedPreferences settings = DemoApp.getSharedPreferences();
        return settings.getInt(key, defValue);
    }

    private static final void setStringPreference(String key, String value) {
        final SharedPreferences settings = DemoApp.getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static final void setBooleanPreference(String key, boolean value) {
        final SharedPreferences settings = DemoApp.getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static final void setIntPreference(String key, int value) {
        final SharedPreferences settings = DemoApp.getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static final void removePreference(String key) {
        final SharedPreferences settings = DemoApp.getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.apply();
    }
}
