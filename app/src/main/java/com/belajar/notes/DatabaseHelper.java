package com.belajar.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper  extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "DataNotes";
    private static final String TABLE_NAME = "tbl_notes";
    private static final String KEY_ID = "id",KEY_JUDUL = "judul",KEY_DESKRIPSI="deskripsi",KEY_DATE = "tanggal";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createUserTable = "CREATE TABLE "+ TABLE_NAME
                + "("+KEY_ID+" INTEGER PRIMARY KEY, "
                + KEY_JUDUL + " TEXT, "
                + KEY_DESKRIPSI + " TEXT, "
                + KEY_DATE + " TEXT" + ")";
        sqLiteDatabase.execSQL(createUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = ("drop table if exists " + TABLE_NAME);
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void insert (Notes notes){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,notes.getId());
        values.put(KEY_JUDUL,notes.getJudul());
        values.put(KEY_DESKRIPSI,notes.getDeskripsi());
        values.put(KEY_DATE,notes.getDate());
        db.insert(TABLE_NAME,null,values);
    }

    public List<Notes> selectNotesData(){
        ArrayList<Notes> notesList = new ArrayList<Notes>();

        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {KEY_ID,KEY_JUDUL,KEY_DESKRIPSI,KEY_DATE};

        Cursor c = db.query(TABLE_NAME,columns,null,null,null,null,null);

        while (c.moveToNext()){
            int id = c.getInt(0);
            String judul = c.getString(1);
            String deskripsi = c.getString(2);
            String date = c.getString(3);

            Notes notes = new Notes();
            notes.setId(id);
            notes.setJudul(judul);
            notes.setDeskripsi(deskripsi);
            notes.setDate(date);
            notesList.add(notes);
        }

        return notesList;
    }
    public  void delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = KEY_ID+"='"+id+"'";
        db.delete(TABLE_NAME,whereClause,null); }

    public  void update(Notes notes){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,notes.getId());
        values.put(KEY_JUDUL,notes.getJudul());
        values.put(KEY_DESKRIPSI,notes.getDeskripsi());
        values.put(KEY_DATE,notes.getDate());
        String whereClause = KEY_ID+"='"+notes.getId()+"'";
        db.update(TABLE_NAME,values,whereClause,null);
    }


}
