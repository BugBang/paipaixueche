package com.session.dgjp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.session.dgjp.enity.Init;
import com.session.dgjp.enity.MyMessage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库操作工具类
 *
 * @author YJ Liang
 *         2015年9月24日 下午5:33:35
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "dgjp.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance;
    private Context context;
    private static Map<String, Dao<?, ?>> daoMap = new HashMap<String, Dao<?, ?>>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, MyMessage.class);
            TableUtils.createTable(connectionSource, Init.class);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVer,
                          int newVer) {
        switch (oldVer) {
            case 1:
                break;

            default:
                break;
        }
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null || !instance.isOpen()) {
            instance = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public static <T, D extends Dao<T, ?>> D getDao(Context context, Class<T> clazz) throws SQLException {
        String name = clazz.getName();
        if (daoMap.containsKey(name)) {
            return (D) daoMap.get(name);
        } else {
            D dao = (D) getInstance(context).getDao(clazz);
            daoMap.put(name, dao);
            return dao;
        }
    }


    public static synchronized void release() {
        if (instance != null && instance.isOpen()) {
            daoMap.clear();
            OpenHelperManager.releaseHelper();
        }
    }

}
