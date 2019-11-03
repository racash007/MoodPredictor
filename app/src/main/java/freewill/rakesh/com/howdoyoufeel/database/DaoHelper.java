package freewill.rakesh.com.howdoyoufeel.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import freewill.rakesh.com.howdoyoufeel.model.DaoMaster;
import freewill.rakesh.com.howdoyoufeel.model.DaoSession;

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