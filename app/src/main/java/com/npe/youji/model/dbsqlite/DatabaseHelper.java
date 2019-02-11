package com.npe.youji.model.dbsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "card.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CART = "carts";
    public static final String COLUMN_ID = "idCart";
    public static final String COLUMN_IDPRODUCT = "idProduct";
    public static final String COLUMN_NAMEPRODUCT = "nameProduct";
    public static final String COLUMN_STOKPRODUCT = "stokProduct";
    public static final String COLUMN_QUANTITY = "quantity";

    private static final String CREATE_TABLE =
            "CREATE TABLE "+ TABLE_CART + " ( "+
                    COLUMN_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IDPRODUCT + " INTEGER NOT NULL, "+
                    COLUMN_NAMEPRODUCT + " TEXT NOT NULL ,"+
                    COLUMN_STOKPRODUCT + " INTEGER NOT NULL,"+
                    COLUMN_QUANTITY + " INTEGER NOT NULL)";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CART);
        onCreate(db);
    }
}
