package com.rp.mood.diary.service;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class UniqueIdentifierService {

    private final Context context;

    public UniqueIdentifierService(Context context)
    {

        this.context = context;
    }

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    public synchronized String getUniqueID() {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = this.context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }
}
