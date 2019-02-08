package com.npe.youji.model.dbsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.npe.youji.model.shop.CartModel;

import java.util.ArrayList;
import java.util.List;

public class dbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cart_db";

    public dbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CartModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CartModel.TABLE_NAME);
        onCreate(db);
    }

    public long insertCart(int idProduct, String nameProduct) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(CartModel.COLUMN_PRODUCT_ID, idProduct);
        values.put(CartModel.COLUMN_PRODUCT_NAME, nameProduct);

        // insert row
        long id = db.insert(CartModel.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public CartModel getCart(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CartModel.TABLE_NAME,
                new String[]{CartModel.COLUMN_ID,CartModel.COLUMN_PRODUCT_ID, CartModel.COLUMN_PRODUCT_NAME},
                CartModel.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        CartModel note = new CartModel(
                cursor.getInt(cursor.getColumnIndex(CartModel.COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(CartModel.COLUMN_PRODUCT_ID)),
                cursor.getString(cursor.getColumnIndex(CartModel.COLUMN_PRODUCT_NAME)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<CartModel> getAllCart() {
        List<CartModel> carts = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + CartModel.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CartModel cart = new CartModel();
                cart.setId(cursor.getInt(cursor.getColumnIndex(CartModel.COLUMN_ID)));
                cart.setIdProduct(cursor.getInt(cursor.getColumnIndex(CartModel.COLUMN_PRODUCT_ID)));
                cart.setNameProduct(cursor.getString(cursor.getColumnIndex(CartModel.COLUMN_PRODUCT_NAME)));

                carts.add(cart);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return carts;
    }

    public int getCartCount() {
        String countQuery = "SELECT  * FROM " + CartModel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateCart(CartModel note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CartModel.COLUMN_PRODUCT_ID, note.getIdProduct());
        values.put(CartModel.COLUMN_PRODUCT_NAME, note.getNameProduct());

        // updating row
        return db.update(CartModel.TABLE_NAME, values, CartModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteCart(CartModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CartModel.TABLE_NAME, CartModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }


}
