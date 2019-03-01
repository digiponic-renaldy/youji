package com.npe.youji.model.dbsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
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
            DatabaseHelper.COLUMN_CABANGPRODUCT,
            DatabaseHelper.COLUMN_KODEPRODUCT,
            DatabaseHelper.COLUMN_KETERANGANPRODUCT,
            DatabaseHelper.COLUMN_KATEGORIPRODUCT,
            DatabaseHelper.COLUMN_JENISPRODUCT,
            DatabaseHelper.COLUMN_SATUANPRODUCT,
            DatabaseHelper.COLUMN_STOKPRODUCT,
            DatabaseHelper.COLUMN_HARGAPRODUCT,
            DatabaseHelper.COLUMN_GAMBARPRODUCT,
            DatabaseHelper.COLUMN_CREATEDPRODUCT,
            DatabaseHelper.COLUMN_UPDATEDPRODUCT,
            DatabaseHelper.COLUMN_DELETEDPRODUCT
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
        values.put(DatabaseHelper.COLUMN_CABANGPRODUCT, data.getCabang());
        values.put(DatabaseHelper.COLUMN_KODEPRODUCT, data.getKode());
        values.put(DatabaseHelper.COLUMN_KETERANGANPRODUCT, data.getKeterangan());
        values.put(DatabaseHelper.COLUMN_KATEGORIPRODUCT, data.getKategori());
        values.put(DatabaseHelper.COLUMN_JENISPRODUCT, data.getJenis());
        values.put(DatabaseHelper.COLUMN_SATUANPRODUCT, data.getSatuan());
        values.put(DatabaseHelper.COLUMN_STOKPRODUCT, data.getStok());
        values.put(DatabaseHelper.COLUMN_HARGAPRODUCT, data.getHarga());
        values.put(DatabaseHelper.COLUMN_GAMBARPRODUCT, data.getGambar());
        values.put(DatabaseHelper.COLUMN_CREATEDPRODUCT, data.getCreated_at());
        values.put(DatabaseHelper.COLUMN_UPDATEDPRODUCT, data.getUpdated_at());
        values.put(DatabaseHelper.COLUMN_DELETEDPRODUCT, data.getDeleted_at());
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
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                Integer.parseInt(cursor.getString(7)),
                Integer.parseInt(cursor.getString(8)),
                cursor.getString(9),
                cursor.getString(10),
                cursor.getString(11),
                cursor.getString(12)
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
                cart.setCabang(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CABANGPRODUCT)));
                cart.setKode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_KODEPRODUCT)));
                cart.setKeterangan(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_KETERANGANPRODUCT)));
                cart.setKeterangan(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_KATEGORIPRODUCT)));
                cart.setJenis(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_JENISPRODUCT)));
                cart.setSatuan(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SATUANPRODUCT)));
                cart.setStok(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STOKPRODUCT)));
                cart.setHarga(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_HARGAPRODUCT)));
                cart.setGambar(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GAMBARPRODUCT)));
                cart.setCreated_at(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CREATEDPRODUCT)));
                cart.setUpdated_at(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_UPDATEDPRODUCT)));
                cart.setDeleted_at(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DELETEDPRODUCT)));
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
        values.put(DatabaseHelper.COLUMN_CABANGPRODUCT, data.getCabang());
        values.put(DatabaseHelper.COLUMN_KODEPRODUCT, data.getKode());
        values.put(DatabaseHelper.COLUMN_KETERANGANPRODUCT, data.getKeterangan());
        values.put(DatabaseHelper.COLUMN_KATEGORIPRODUCT, data.getKategori());
        values.put(DatabaseHelper.COLUMN_JENISPRODUCT, data.getJenis());
        values.put(DatabaseHelper.COLUMN_SATUANPRODUCT, data.getSatuan());
        values.put(DatabaseHelper.COLUMN_STOKPRODUCT, data.getStok());
        values.put(DatabaseHelper.COLUMN_HARGAPRODUCT, data.getHarga());
        values.put(DatabaseHelper.COLUMN_GAMBARPRODUCT, data.getGambar());
        values.put(DatabaseHelper.COLUMN_CREATEDPRODUCT, data.getCreated_at());
        values.put(DatabaseHelper.COLUMN_UPDATEDPRODUCT, data.getUpdated_at());
        values.put(DatabaseHelper.COLUMN_DELETEDPRODUCT, data.getDeleted_at());
        // updating row
        return sqLiteDatabase.update(DatabaseHelper.TABLE_SHOP, values,
                DatabaseHelper.COLUMN_IDPRODUCTSHOP + "=?", new String[]{String.valueOf(data.getIdproduk())});
    }

    //join data
    public ArrayList<JoinModel> joinData() {
        String query = "SELECT S." + DatabaseHelper.COLUMN_IDPRODUCTSHOP + ",S." + DatabaseHelper.COLUMN_CABANGPRODUCT +
                ", S."+ DatabaseHelper.COLUMN_KODEPRODUCT + " , S."+ DatabaseHelper.COLUMN_KETERANGANPRODUCT + ", S."+DatabaseHelper.COLUMN_KATEGORIPRODUCT 
                + " ,S."+DatabaseHelper.COLUMN_JENISPRODUCT+" ,S."+DatabaseHelper.COLUMN_SATUANPRODUCT + " ,S."+DatabaseHelper.COLUMN_STOKPRODUCT+
                ",S." + DatabaseHelper.COLUMN_HARGAPRODUCT + ",S." + DatabaseHelper.COLUMN_GAMBARPRODUCT + ",S." +
                DatabaseHelper.COLUMN_CREATEDPRODUCT + " ,S."+DatabaseHelper.COLUMN_UPDATEDPRODUCT + " ,S."+DatabaseHelper.COLUMN_DELETEDPRODUCT +
                ",C." + DatabaseHelper.COLUMN_QUANTITY +
                " FROM " + DatabaseHelper.TABLE_SHOP + " as S LEFT OUTER JOIN " + DatabaseHelper.TABLE_CART + " as C ON S." +
                DatabaseHelper.COLUMN_IDPRODUCTSHOP +
                "= C." + DatabaseHelper.COLUMN_IDPRODUCTCART;
        /*String query1 = "SELECT * FROM " + DatabaseHelper.TABLE_SHOP + " as S JOIN " + DatabaseHelper.TABLE_CART + " as C ON S." +
                DatabaseHelper.COLUMN_IDPRODUCTSHOP +
                "= C." + DatabaseHelper.COLUMN_IDPRODUCTCART;*/

        Cursor c = sqLiteDatabase.rawQuery(query, null);
        //c.moveToFirst();
        Log.i("QUERYJOIN", String.valueOf(query));
        ArrayList<JoinModel> shop = new ArrayList<>();
        if (c.getCount() > 0) {
            Log.i("IFMASUK", "MASUK");
            while (c.moveToNext()) {
                JoinModel cart = new JoinModel();
                cart.setIdproduk(c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_IDPRODUCTSHOP)));
                cart.setCabang(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CABANGPRODUCT)));
                cart.setKode(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_KODEPRODUCT)));
                cart.setKeterangan(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_KETERANGANPRODUCT)));
                cart.setKategori(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_KATEGORIPRODUCT)));
                cart.setJenis(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_JENISPRODUCT)));
                cart.setSatuan(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_SATUANPRODUCT)));
                cart.setStok(c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_STOKPRODUCT)));
                cart.setHarga(c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_HARGAPRODUCT)));
                cart.setGambar(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_GAMBARPRODUCT)));
                cart.setCreated_at(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CREATEDPRODUCT)));
                cart.setUpdated_at(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_UPDATEDPRODUCT)));
                cart.setDeleted_at(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_DELETEDPRODUCT)));
                cart.setQuantity(c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY)));
                shop.add(cart);
            }
        }
        //Log.i("JOINKU", String.valueOf(shop.get(2).getQuantity()));
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

    public void dropTable(){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_SHOP);
    }
}
