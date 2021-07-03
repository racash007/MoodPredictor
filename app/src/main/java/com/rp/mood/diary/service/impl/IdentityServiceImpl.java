package com.rp.mood.diary.service.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.rp.mood.diary.service.IdentityService;

import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class IdentityServiceImpl implements IdentityService {

    private final Context context;

    @Inject
    public IdentityServiceImpl(@ApplicationContext Context context)
    {
        this.context = context;
    }

    private static String uniqueID;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    @Override
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
