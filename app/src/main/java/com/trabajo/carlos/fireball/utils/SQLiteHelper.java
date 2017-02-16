package com.trabajo.carlos.fireball.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Carlos Prieto on 16/02/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Usuarios";
    public static final String TABLE_NAME = "Alias";
    public static final int DATABASE_VERSION = 1;
    public final static String COLUMN_ID="id";
    public final static String COLUMN_ALIAS="alias";

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ALIAS + " TEXT)";

    public SQLiteHelper(Context contexto) {
        super(contexto, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Alias");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }

    public void insertarAlias(String alias){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ALIAS, alias);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();

    }

    public String mostrarUltimoAlias(){

        SQLiteDatabase db = this.getReadableDatabase();

        String sqlMostrar = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ALIAS + " DESC LIMIT 1";
        db.execSQL(sqlMostrar);

        return sqlMostrar;

    }


}
