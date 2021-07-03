package com.rp.mood.diary.database;

import android.content.Context;

import com.rp.mood.diary.model.MoodDao;
import com.rp.mood.diary.model.MoodDriverDao;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DaoModule {

    @Provides
    public static MoodDao getMoodDao(@ApplicationContext Context context)
    {
        return DaoHelper.getDaoSession(context).getMoodDao();
    }

    @Provides
    public static MoodDriverDao getMoodDriverDao(@ApplicationContext Context context)
    {
        return DaoHelper.getDaoSession(context).getMoodDriverDao();
    }
}
