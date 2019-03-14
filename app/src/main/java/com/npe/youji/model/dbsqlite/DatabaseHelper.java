package com.npe.youji.model.dbsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shop.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CART = "carts";
    public static final String TABLE_SHOP = "shops";
    public static final String TABLE_USER = "users";


    //cart
    public static final String COLUMN_IDPRODUCTCART = "idProduct";
    public static final String COLUMN_QUANTITY = "quantity";

    private static final String CREATE_TABLECART =
            "CREATE TABLE " + TABLE_CART + " ( " +
                    COLUMN_IDPRODUCTCART + " INTEGER UNIQUE NOT NULL, " +
                    COLUMN_QUANTITY + " INTEGER NOT NULL)";

    //shops
    public static final String COLUMN_IDPRODUCTSHOP = "idProduct";
    public static final String COLUMN_CABANGPRODUCT = "cabangProduct";
    public static final String COLUMN_KODEPRODUCT = "kodeProduct";
    public static final String COLUMN_KETERANGANPRODUCT = "keteranganProduct";
    public static final String COLUMN_KATEGORIPRODUCT = "kategoriProduct";
    public static final String COLUMN_JENISPRODUCT = "jenisProduct";
    public static final String COLUMN_SATUANPRODUCT = "satuanProduct";
    public static final String COLUMN_STOKPRODUCT = "stokProduct";
    public static final String COLUMN_HARGAPRODUCT = "hargaProduct";
    public static final String COLUMN_GAMBARPRODUCT = "gambarProduct";
    public static final String COLUMN_DESKRIPSIPRODUCT = "deskripsiProduct";
    public static final String COLUMN_CREATEDPRODUCT = "createdProduct";
    public static final String COLUMN_UPDATEDPRODUCT = "updatedProduct";
    public static final String COLUMN_DELETEDPRODUCT = "deletedProduct";

    private static final String CREATE_TABLESHOP =
            "CREATE TABLE " + TABLE_SHOP + " ( " +
                    COLUMN_IDPRODUCTSHOP + " INTEGER UNIQUE NOT NULL, " +
                    COLUMN_CABANGPRODUCT + " TEXT NOT NULL, " +
                    COLUMN_KODEPRODUCT + " TEXT NOT NULL, " +
                    COLUMN_KETERANGANPRODUCT + " TEXT NOT NULL, " +
                    COLUMN_KATEGORIPRODUCT + " TEXT NOT NULL, " +
                    COLUMN_JENISPRODUCT + " TEXT NOT NULL, " +
                    COLUMN_SATUANPRODUCT + " TEXT NOT NULL, " +
                    COLUMN_STOKPRODUCT + " INTEGER NOT NULL, " +
                    COLUMN_HARGAPRODUCT + " INTEGER NOT NULL, " +
                    COLUMN_GAMBARPRODUCT + " TEXT NOT NULL, " +
                    COLUMN_DESKRIPSIPRODUCT + " TEXT NOT NULL, " +
                    COLUMN_CREATEDPRODUCT + " TEXT NOT NULL, " +
                    COLUMN_UPDATEDPRODUCT + " TEXT , " +
                    COLUMN_DELETEDPRODUCT + " TEXT )";


    //user
    public static final String COLUMN_IDUSER = "iduser";
    public static final String COLUMN_NAMAUSER = "namaUser";
    public static final String COLUMN_EMAILUSER = "emailUser";
    public static final String COLUMN_FULLNAMEUSER = "fullNameUser";
    public static final String COLUMN_ALAMATUSER = "alamatUser";
    public static final String COLUMN_NOTELPUSER = "notelpUser";

    private static final String CREATE_TABLEUSER =
            "CREATE TABLE " + TABLE_USER + " ( " +
                    COLUMN_IDUSER + " INTEGER UNIQUE NOT NULL, " +
                    COLUMN_NAMAUSER + " TEXT NOT NULL, " +
                    COLUMN_EMAILUSER + " TEXT NOT NULL, " +
                    COLUMN_FULLNAMEUSER + " TEXT , " +
                    COLUMN_ALAMATUSER + " TEXT , " +
                    COLUMN_NOTELPUSER + " TEXT)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLECART);
        db.execSQL(CREATE_TABLESHOP);
        db.execSQL(CREATE_TABLEUSER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}
