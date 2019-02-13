package com.npe.youji.model.dbsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shop.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CART = "carts";
    public static final String TABLE_SHOP = "shops";
    //shop


    //cart
    public static final String COLUMN_IDPRODUCTCART = "idProduct";
    public static final String COLUMN_QUANTITY = "quantity";

    private static final String CREATE_TABLECART =
            "CREATE TABLE " + TABLE_CART + " ( " +
                    COLUMN_IDPRODUCTCART + " INTEGER UNIQUE NOT NULL, " +
                    COLUMN_QUANTITY + " INTEGER NOT NULL)";

    //shops
    public static final String COLUMN_IDPRODUCTSHOP = "idProduct";
    public static final String COLUMN_NAMEPRODUCT = "namaProduct";
    public static final String COLUMN_HARGAPRODUCT = "hargaProduct";
    public static final String COLUMN_IMAGEPRODUCT = "imageProduct";
    public static final String COLUMN_STOKPRODUCT = "stokProduct";

    private static final String CREATE_TABLESHOP =
            "CREATE TABLE " + TABLE_SHOP + " ( " +
                    COLUMN_IDPRODUCTSHOP + " INTEGER UNIQUE NOT NULL, " +
                    COLUMN_NAMEPRODUCT + " TEXT NOT NULL, " +
                    COLUMN_HARGAPRODUCT + " INTEGER NOT NULL, " +
                    COLUMN_IMAGEPRODUCT + " TEXT NOT NULL, " +
                    COLUMN_STOKPRODUCT + " INTEGER NOT NULL)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLECART);
        db.execSQL(CREATE_TABLESHOP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP);
        onCreate(db);
    }
}
