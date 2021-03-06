package com.jere.forum;

import android.app.Application;

import com.jere.forum.entity.DaoMaster;
import com.jere.forum.entity.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * @author jere
 */
public class App extends Application {
    private DaoSession daoSession;
    public static App app;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        // regular SQLite database
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chats-db");
        Database db = helper.getWritableDb();

        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
