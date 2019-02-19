package com.npe.youji.model.dbsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.npe.youji.model.user.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserOperations {
    public static final String LOGTAG = "SHOP_SYS";

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    private static final String[] allColumns = {
            DatabaseHelper.COLUMN_IDUSER,
            DatabaseHelper.COLUMN_NAMAUSER,
            DatabaseHelper.COLUMN_EMAILUSER
    };

    public UserOperations(Context context) {
        sqLiteOpenHelper = new DatabaseHelper(context);
    }

    public void openDb() {
        Log.i(LOGTAG, "Database open");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void closeDb() {
        Log.i(LOGTAG, "database close");
    }

    //insert data user
    public UserModel insertUser(UserModel users) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IDUSER, users.getId());
        values.put(DatabaseHelper.COLUMN_NAMAUSER, users.getNama());
        values.put(DatabaseHelper.COLUMN_EMAILUSER, users.getEmail());

        int insertId = (int) sqLiteDatabase.insert(DatabaseHelper.TABLE_USER, null,
                values);
        users.setId(insertId);
        return users;
    }

    //check record user
    public Boolean checkRecordUser() {
        String checkRecord = "SELECT * FROM " + DatabaseHelper.TABLE_USER;
        Cursor cursor = sqLiteDatabase.rawQuery(checkRecord, null);
        boolean hasRecord = false;
        if (cursor.moveToFirst()) {
            hasRecord = true;
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
        }

        cursor.close();
        return hasRecord;
    }

    //get single data user
    public UserModel getUser(int id) {
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_USER, allColumns,
                DatabaseHelper.COLUMN_IDUSER + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        List<UserModel> userModels = new ArrayList<>();
        if (cursor != null)
            cursor.moveToFirst();
        UserModel e = new UserModel(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2)
        );
        return e;
    }

    //get list cart
    public List<UserModel> getAllUser() {
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_USER,
                allColumns, null, null, null, null,
                null);

        List<UserModel> users = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                UserModel user = new UserModel();
                user.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_IDUSER)));
                user.setNama(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAMAUSER)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAILUSER)));
                users.add(user);
            }
        }
        // return All Employees
        return users;
    }

    // Updating user
    public int updateuser(UserModel user) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IDUSER, user.getId());
        values.put(DatabaseHelper.COLUMN_NAMAUSER, user.getNama());
        values.put(DatabaseHelper.COLUMN_EMAILUSER, user.getEmail());

        // updating row
        return sqLiteDatabase.update(DatabaseHelper.TABLE_USER, values,
                DatabaseHelper.COLUMN_IDUSER + "=?", new String[]{String.valueOf(user.getId())});
    }

    // Deleting user
    public void removeuser(UserModel userModel) {

        sqLiteDatabase.delete(DatabaseHelper.TABLE_USER, DatabaseHelper.COLUMN_IDUSER + "=" +
                userModel.getId(), null);
    }

    //drop table user
    public void dropUser(){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_USER);
    }

    //deltering spesific row
    public void deleteRow(String id) {
        sqLiteDatabase.execSQL("DELETE FROM " + DatabaseHelper.TABLE_USER + " WHERE " + DatabaseHelper.COLUMN_IDUSER
                + "=" + id);
    }


}
