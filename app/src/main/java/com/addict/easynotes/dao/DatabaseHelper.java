package com.addict.easynotes.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.addict.easynotes.models.Note;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "esaynotes.db";
    private static final int DATABASE_VERSION = 1;

    private Map<String, Dao> daos = new HashMap<String, Dao>();
    private Map<String, RuntimeExceptionDao> runtimeDaos = new HashMap<String, RuntimeExceptionDao>();

    private Dao<Note, Integer> noteDao = null;

    private RuntimeExceptionDao<Note, Integer> noteRuntimeDao = null;

    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.createTable(connectionSource, Note.class);
        }
        catch (SQLException e)
        {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try
        {
            TableUtils.dropTable(connectionSource, Note.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        }
        catch (SQLException e)
        {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context)
    {
        context = context.getApplicationContext();
        if (instance == null)
        {
            synchronized (DatabaseHelper.class)
            {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException
    {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className))
        {
            dao = daos.get(className);
        }
        if (dao == null)
        {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }


    public synchronized RuntimeExceptionDao getRuntimeDao(Class clazz)
    {
        RuntimeExceptionDao runtimeDao = null;
        String className = clazz.getSimpleName();

        if (runtimeDaos.containsKey(className))
        {
            runtimeDao = runtimeDaos.get(className);
        }
        if (runtimeDao == null)
        {
            runtimeDao = super.getRuntimeExceptionDao(clazz);
            runtimeDaos.put(className, runtimeDao);
        }
        return runtimeDao;
    }

//    public Dao<Note, Integer> getNoteDao() throws SQLException {
//        if (noteDao == null){
//            noteDao = getDao(Note.class);
//        }
//        return noteDao;
//    }
//
//    public RuntimeExceptionDao<Note, Integer> getNoteRuntimeDao() {
//        if (noteRuntimeDao == null){
//            noteRuntimeDao = getRuntimeExceptionDao(Note.class);
//        }
//        return noteRuntimeDao;
//    }

    /**
     * 释放资源
     */
    @Override
    public void close()
    {
        super.close();

        for (String key : daos.keySet())
        {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
