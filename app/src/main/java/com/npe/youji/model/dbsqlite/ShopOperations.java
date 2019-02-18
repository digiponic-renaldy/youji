package com.npe.youji.model.dbsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.npe.youji.model.shop.DataShopModel;
import com.npe.youji.model.shop.JoinModel;

import java.util.ArrayList;
import java.util.List;

public class ShopOperations {
    public static final String LOGTAG = "SHOP_SYS";

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;


    private static final String[] allColumns = {
            DatabaseHelper.COLUMN_IDPRODUCTSHOP,
            DatabaseHelper.COLUMN_NAMEPRODUCT,
            DatabaseHelper.COLUMN_HARGAPRODUCT,
            DatabaseHelper.COLUMN_IMAGEPRODUCT,
            DatabaseHelper.COLUMN_STOKPRODUCT
    };

    public ShopOperations(Context context) {
        sqLiteOpenHelper = new DatabaseHelper(context);
    }

    public void openDb() {
        Log.i(LOGTAG, "DATABASE OPENED");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void closeDb() {
        Log.i(LOGTAG, "DATABASE CLOSED");
        sqLiteOpenHelper.close();
    }

    //insert
    public DataShopModel insertShop(DataShopModel data) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IDPRODUCTSHOP, data.getIdproduk());
        values.put(DatabaseHelper.COLUMN_NAMEPRODUCT, data.getName());
        values.put(DatabaseHelper.COLUMN_HARGAPRODUCT, data.getSell_price());
        values.put(DatabaseHelper.COLUMN_IMAGEPRODUCT, data.getImage());
        values.put(DatabaseHelper.COLUMN_STOKPRODUCT, data.getStock());
        long insertId = sqLiteDatabase.insert(DatabaseHelper.TABLE_SHOP, null, values);
        data.setIdproduk((int) insertId);
        return data;
    }

    //get single data cart
    public DataShopModel getShop(long id) {
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_SHOP, allColumns, DatabaseHelper.COLUMN_IDPRODUCTSHOP
                + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DataShopModel e = new DataShopModel(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                Integer.parseInt(cursor.getString(2)),
                cursor.getString(3),
                Integer.parseInt(cursor.getString(4))
        );
        // return Cart
        return e;
    }

    //get list cart
    public List<DataShopModel> getAllShop() {
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_SHOP, allColumns,
                null, null, null, null, null);

        List<DataShopModel> shop = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                DataShopModel cart = new DataShopModel();
                cart.setIdproduk(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_IDPRODUCTSHOP)));
                cart.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAMEPRODUCT)));
                cart.setSell_price(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_HARGAPRODUCT)));
                cart.setImage(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGEPRODUCT)));
                cart.setStock(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOKPRODUCT)));
                shop.add(cart);
            }
        }
        // return All Employees
        return shop;
    }

    // Updating cart
    public int updateShop(DataShopModel data) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IDPRODUCTSHOP, data.getIdproduk());
        values.put(DatabaseHelper.COLUMN_NAMEPRODUCT, data.getName());
        values.put(DatabaseHelper.COLUMN_HARGAPRODUCT, data.getSell_price());
        values.put(DatabaseHelper.COLUMN_IMAGEPRODUCT, data.getImage());
        values.put(DatabaseHelper.COLUMN_STOKPRODUCT, data.getStock());
        // updating row
        return sqLiteDatabase.update(DatabaseHelper.TABLE_SHOP, values,
                DatabaseHelper.COLUMN_IDPRODUCTSHOP + "=?", new String[]{String.valueOf(data.getIdproduk())});
    }

    //join data
    public ArrayList<JoinModel> joinData() {
        String query = "SELECT S." + DatabaseHelper.COLUMN_IDPRODUCTSHOP + ",S." + DatabaseHelper.COLUMN_NAMEPRODUCT +
                ",S." + DatabaseHelper.COLUMN_HARGAPRODUCT + ",S." + DatabaseHelper.COLUMN_IMAGEPRODUCT + ",S." +
                DatabaseHelper.COLUMN_STOKPRODUCT +
                ",C." + DatabaseHelper.COLUMN_QUANTITY +
                " FROM " + DatabaseHelper.TABLE_SHOP + " as S LEFT OUTER JOIN " + DatabaseHelper.TABLE_CART + " as C ON S." +
                DatabaseHelper.COLUMN_IDPRODUCTSHOP +
                "= C." + DatabaseHelper.COLUMN_IDPRODUCTCART;
        /*String query1 = "SELECT * FROM " + DatabaseHelper.TABLE_SHOP + " as S JOIN " + DatabaseHelper.TABLE_CART + " as C ON S." +
                DatabaseHelper.COLUMN_IDPRODUCTSHOP +
                "= C." + DatabaseHelper.COLUMN_IDPRODUCTCART;*/

        Cursor c = sqLiteDatabase.rawQuery(query, null);
        c.moveToFirst();
        Log.i("QUERYJOIN", String.valueOf(query));
        ArrayList<JoinModel> shop = new ArrayList<>();
        if (c.getCount() > 0) {
            Log.i("IFMASUK", "MASUK");
            while (c.moveToNext()) {
                JoinModel cart = new JoinModel();
                Log.i("JOINID", String.valueOf(c.getColumnIndex(DatabaseHelper.COLUMN_IDPRODUCTSHOP)));
                cart.setIdproduk(c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_IDPRODUCTSHOP)));
                cart.setName(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_NAMEPRODUCT)));
                cart.setSell_price(c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_HARGAPRODUCT)));
                cart.setImage(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_IMAGEPRODUCT)));
                cart.setStock(c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_STOKPRODUCT)));
                cart.setQuantity(c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)));
                shop.add(cart);
            }
        }
        Log.i("JOINKU", String.valueOf(shop.get(2).getQuantity()));
        return shop;
    }

    //alter data
    public void alterColumn() {
        String query = "ALTER TABLE " + DatabaseHelper.TABLE_SHOP + " ADD " + DatabaseHelper.COLUMN_QUANTITY + " INTEGER DEFAULT 0";
        sqLiteDatabase.rawQuery(query, null);
    }

    // Deleting Cart
    public void removeCart(DataShopModel data) {

        sqLiteDatabase.delete(DatabaseHelper.TABLE_SHOP, DatabaseHelper.COLUMN_IDPRODUCTSHOP + "=" +
                data.getIdproduk(), null);
    }

    //deltering spesific row
    public void deleteRow(String idProduct) {
        sqLiteDatabase.execSQL("DELETE FROM " + DatabaseHelper.TABLE_SHOP + " WHERE " + DatabaseHelper.COLUMN_IDPRODUCTSHOP
                + "=" + idProduct);
    }
}
