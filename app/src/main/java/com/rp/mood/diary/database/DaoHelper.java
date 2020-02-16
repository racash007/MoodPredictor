package com.rp.mood.diary.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import com.rp.mood.diary.model.DaoMaster;
import com.rp.mood.diary.model.DaoSession;

public class DaoHelper  {

    private static DaoSession daoSession;



    public static DaoSession getDaoSession(Context context) {
        if(daoSession!=null)
        {return daoSession;}

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "notes-db");
        Database db = helper.getWritableDb();
        DaoMaster daoMaster =new DaoMaster(db);
        daoSession = daoMaster.newSession();

        return daoSession;
    }
}