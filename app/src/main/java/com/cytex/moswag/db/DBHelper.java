package com.cytex.moswag.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cytex.moswag.model.entities.Company;
import com.cytex.moswag.model.entities.ProductCategoryModel;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    public  SQLiteDatabase db;


    //company  table
    private static final String CREATE_TABLE_COMPANY="create table "+ DBContract.TABLE_NAME_COMPANY+
            " (id integer primary key autoincrement, "+ DBContract.COMPANY_ID+" text,"+ DBContract.COMPANY_NAME+" text,"+ DBContract.COMPANY_ADDRESS+" text,"+
            DBContract.COMPANY_LOCATION+" text,"+ DBContract.COMPANY_ECOCASH +" text);";

    //drop tables
    private static final String DROP_TABLE_COMPANY="drop table if exists "+DBContract.TABLE_NAME_COMPANY;

    public DBHelper(Context context){
        super(context,DBContract.DATABASE_NAME,null,DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMPANY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_COMPANY);
    }


    //user tables
    public void saveCompanyToLocalDatabase(ProductCategoryModel company, SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBContract.COMPANY_ID,company.getCompanyID());
        contentValues.put(DBContract.COMPANY_NAME,company.getCompanyName());
        contentValues.put(DBContract.COMPANY_ADDRESS,company.getCompanyAddress());
        contentValues.put(DBContract.COMPANY_LOCATION,company.getCompanyLocation());
        contentValues.put(DBContract.COMPANY_ECOCASH,company.getEcocash());
        database.insert(DBContract.TABLE_NAME_COMPANY,null,contentValues);
    }

    public void updateCompanyLocalDatabase(ProductCategoryModel company, SQLiteDatabase database){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBContract.COMPANY_NAME,company.getCompanyName());
        contentValues.put(DBContract.COMPANY_ADDRESS,company.getCompanyAddress());
        contentValues.put(DBContract.COMPANY_LOCATION,company.getCompanyLocation());
        contentValues.put(DBContract.COMPANY_ECOCASH,company.getEcocash());
        String selection=DBContract.COMPANY_ID+" LIKE ?";
        String[] selection_args={company.getCompanyID()};
        database.update(DBContract.TABLE_NAME_COMPANY,contentValues,selection,selection_args);
    }

    public boolean checkCompany(String company_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + DBContract.TABLE_NAME_COMPANY + " WHERE " + DBContract.COMPANY_ID + " LIKE '%" + company_id +"%'";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return false;
        }
        cursor.moveToFirst();
        cursor.close();
        return true;
    }


    public String getEcocash(String company_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + DBContract.TABLE_NAME_COMPANY + " WHERE " + DBContract.COMPANY_ID + " LIKE '%" + company_id +"%'";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String cid = cursor.getString(cursor.getColumnIndex(DBContract.COMPANY_ECOCASH));
        cursor.close();
        return cid;
    }
}
