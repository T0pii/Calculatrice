package com.example.calculmental.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.calculmental.entities.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao<T extends BaseEntity> {
    private final DataBaseHelper dbHelper;

    public BaseDao(DataBaseHelper helper){
        this.dbHelper = helper;
    }

    protected abstract String getTableName();
    protected abstract void putValues(ContentValues values, T entity);
    protected abstract T getEntity(Cursor cursor);

    /**
     * @param entity : element a ajouter dans la base
     * @return : l element créait avec son ID
     */
    public T create(T entity){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        putValues(values, entity);

        long newRowId = db.insert(getTableName(), null, values);
        entity.id = newRowId;
        return entity;
    }

    protected List<T> query(String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                getTableName(),
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        List items = new ArrayList<T>();
        while(cursor.moveToNext()) {
            items.add(getEntity(cursor));

        }
        cursor.close();

        return items;
    }


    public T lastOrNull() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor =db.query(
                getTableName(),
                null,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToLast();
        T last = this.getEntity(cursor);
        cursor.close();

        return last;
    }


    public long count() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select count(*) from "+getTableName(), null);
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        cursor.close();

        return count;
    }

    public int leBestEasy() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int bestScoreEasy=-69;

        Cursor cursor = db.rawQuery("select * from "+getTableName()+" order by bestEasy DESC", null);
        cursor.moveToFirst();
        if (count()>0) {
            bestScoreEasy = cursor.getInt(1);
            cursor.close();
        }

        return bestScoreEasy;
    }

    public int leBestMedium() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int bestScoreMedium=-69;

        Cursor cursor = db.rawQuery("select * from "+getTableName()+" order by bestMedium DESC", null);
        cursor.moveToFirst();
        if(count()>0) {
            bestScoreMedium = cursor.getInt(2);
            cursor.close();
        }

        return bestScoreMedium;
    }

    public int leBestHard() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int bestScoreHard=-69;
        Cursor cursor = db.rawQuery("select * from " + getTableName() + " order by bestHard DESC", null);
        cursor.moveToFirst();
        if (count()>0) {
            bestScoreHard = cursor.getInt(3);
            cursor.close();
        }

        return bestScoreHard;
    }
}
