package com.npe.youji.model.dbsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.npe.youji.model.shop.CartModel;

import java.util.ArrayList;
import java.util.List;

public class CartOperations {
    public static final  String LOGTAG = "CART_SYS";

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;


    private static final String[] allColumns = {
        DatabaseHelper.COLUMN_ID,
        DatabaseHelper.COLUMN_IDPRODUCT,
        DatabaseHelper.COLUMN_NAMEPRODUCT
    };

    public CartOperations(Context context) {
        sqLiteOpenHelper = new DatabaseHelper(context);
    }

    public void openDb(){
        Log.i(LOGTAG, "DATABASE OPENED");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void closeDb(){
        Log.i(LOGTAG, "DATABASE CLOSED");
        sqLiteOpenHelper.close();
    }

    //insert data cart
    public CartModel insertCart(CartModel carts){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IDPRODUCT, carts.getIdProduct());
        values.put(DatabaseHelper.COLUMN_NAMEPRODUCT,carts.getNameProduct());
        long insertId = sqLiteDatabase.insert(DatabaseHelper.TABLE_CART, null, values);
        carts.setIdcart(insertId);
        return carts;
    }
    //get single data cart
    public CartModel getCart(long id){
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_CART,allColumns,DatabaseHelper.COLUMN_ID
                + "=?",new String[]{String.valueOf(id)},null,null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CartModel e = new CartModel(
                Long.parseLong(cursor.getString(0)),
                Long.parseLong(cursor.getString(1)),
                cursor.getString(2));
        // return Cart
        return e;
    }

    //get list cart
    public List<CartModel> getAllCart(){
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_CART,allColumns,
                null,null,null, null, null);

        List<CartModel> carts = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                CartModel cart = new CartModel();
                cart.setIdcart(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                cart.setIdProduct(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_IDPRODUCT)));
                cart.setNameProduct(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAMEPRODUCT)));
                carts.add(cart);
            }
        }
        // return All Employees
        return carts;
    }

    // Updating cart
    public int updateCart(CartModel cart) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IDPRODUCT , cart.getIdProduct());
        values.put(DatabaseHelper.COLUMN_NAMEPRODUCT, cart.getNameProduct());

        // updating row
        return sqLiteDatabase.update(DatabaseHelper.TABLE_CART, values,
                DatabaseHelper.COLUMN_ID + "=?",new String[] { String.valueOf(cart.getIdcart())});
    }

    // Deleting Cart
    public void removeCart(CartModel cartModel) {

        sqLiteDatabase.delete(DatabaseHelper.TABLE_CART, DatabaseHelper.COLUMN_ID + "=" + cartModel.getIdcart(), null);
    }

}
