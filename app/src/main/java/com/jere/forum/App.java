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

    @Override
    public void onCreate() {
        super.onCreate();

        // regular SQLite database
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chats-db");
        Database db = helper.getWritableDb();

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");

        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
