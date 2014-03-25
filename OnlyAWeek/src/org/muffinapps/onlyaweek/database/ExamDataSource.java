package org.muffinapps.onlyaweek.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamDataSource{
	private static final String NAME_DB = "exams_db";
	public static final String NAME_TABLE = "exams";
	private static final int VERSION = 2;
	public static final String [] NAME_COL = {"_id", "name", "date", "remainingPag",
												"assignedPag", "totalPag"
	};
	
	private SQLiteDatabase db;
    private ExamSQLiteHelper dbExamSqliteHelper;
    
	public ExamDataSource(Context context){
		dbExamSqliteHelper = new ExamSQLiteHelper(context, NAME_DB, null, VERSION);
		openDBModeWriter();
	}
	
	private void openDBModeWriter(){
		if(db == null){
			db = dbExamSqliteHelper.getWritableDatabase();
		}
	}
	
	private void openDBModeReader(){
		if(db == null){
			db = dbExamSqliteHelper.getReadableDatabase();
		}
	}
	
	public void closeDB(){
        if(db != null){
            db.close();
            db = null;
        }
    }
	
	public void insert(ContentValues content){
		
		db.insert(NAME_TABLE, null, content);
	}
	
	public Cursor getExam(long id){
		Cursor c = null;
		
		c = db.query(NAME_TABLE, NAME_COL, NAME_COL[0] + " = ?", new String[]{String.valueOf(id)}, null, null, null, "1");
		
		c.moveToFirst();
		
		return c;
	}
	
	public Cursor getAllExam(){
		Cursor c = null;
		String [] col = {NAME_COL[0], NAME_COL[1], NAME_COL[2]};
		
		c = db.query(NAME_TABLE, col, null, null, null, null, null);
		
		c.moveToFirst();
		
		return c;
	}
	
	public Cursor getExamPreparation(){
		Cursor c = null;
		
		c = db.query(NAME_TABLE, NAME_COL, NAME_COL[5] + " IS NOT NULL", null, null, null, null);
		
		c.moveToFirst();
		
		return c;
	}
	
	public Cursor getExamNotPreparation(){
		Cursor c = null;
		String [] col = {NAME_COL[0], NAME_COL[1], NAME_COL[2]};
		
		c = db.query(NAME_TABLE, col, NAME_COL[5] + " IS NULL", null, null, null, null);
		
		c.moveToFirst();
		
		return c;
	}
	
	private static class ExamSQLiteHelper extends SQLiteOpenHelper{
		private static final String DB_CREATE="CREATE TABLE " + NAME_TABLE +
                " ("+
                NAME_COL[0] + " LONG PRIMARY KEY, " +
                NAME_COL[1] + " TEXT NOT NULL, " +  
                NAME_COL[2] + " TEXT NOT NULL, " +
                NAME_COL[3] + " INTEGER , " +
                NAME_COL[4] + " INTEGER , " +
                NAME_COL[5] + " INTEGER );";

		public ExamSQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "+ NAME_TABLE);
            onCreate(db);
		}

		
	}
}