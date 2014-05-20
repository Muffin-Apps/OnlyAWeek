package org.muffinapps.onlyaweek.database;

import java.util.Calendar;
import java.util.Date;

import org.emud.content.DataSubject;
import org.emud.content.observer.Observer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamDataSource{
	private static final String NAME_DB = "exams_db";
	public static final String NAME_TABLE = "exams";
	private static final int VERSION = 3;
	public static final String [] NAME_COL = {"_id", "name", "date", "remainingPag", "totalPag", "preparing"};
	
	private SQLiteDatabase db;
    private ExamSQLiteHelper dbExamSqliteHelper;
    
    private DataSubject subject;
    
	public ExamDataSource(Context context){
		dbExamSqliteHelper = new ExamSQLiteHelper(context, NAME_DB, null, VERSION);
		openDBModeWriter();
		
		subject = new DataSubject();
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
	
	public void insertNewExam(String name, Calendar date, int totalPag){
		ContentValues content = new ContentValues();
		content.put(ExamDataSource.NAME_COL[1], name);
		content.put(ExamDataSource.NAME_COL[2], date.getTimeInMillis());
		content.put(ExamDataSource.NAME_COL[3], totalPag);
		content.put(ExamDataSource.NAME_COL[4], totalPag);
		content.put(ExamDataSource.NAME_COL[5], 0);
		
		db.insert(NAME_TABLE, null, content);
		
		notifyObservers();
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
		
		c = db.query(NAME_TABLE, NAME_COL, NAME_COL[5] + " = 1", null, null, null, null);
		
		c.moveToFirst();
		
		return c;
	}
	
	public Cursor getExamNotPreparation(){
		Cursor c = null;
		String [] col = {NAME_COL[0], NAME_COL[1], NAME_COL[2]};
		
		c = db.query(NAME_TABLE, col, NAME_COL[5] + " = 0", null, null, null, null);
		
		c.moveToFirst();
		
		return c;
	}
	
	public void deleteExam(long id){
		deleteExams(new long[]{id});
	}
	
	public void deleteExams(long[] ids){
		int n = ids.length;
		
		if(n == 0)
			return;
		
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i<n-1; i++){
			builder.append(ids[i]);
			builder.append(", ");
		}
		
		builder.append(ids[n-1]);
		
		
		db.delete(NAME_TABLE, NAME_COL[0] + " IN (" + builder.toString() + ")", null);
		
		notifyObservers();
	}
	
	
	private static class ExamSQLiteHelper extends SQLiteOpenHelper{
		private static final String DB_CREATE="CREATE TABLE " + NAME_TABLE +
                " ("+
                NAME_COL[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL[1] + " TEXT NOT NULL, " +  
                NAME_COL[2] + " LONG NOT NULL, " +
                NAME_COL[3] + " INTEGER NOT NULL, " +
                NAME_COL[4] + " INTEGER NOT NULL, " +
                NAME_COL[5] + " INTEGER NOT NULL);";

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
	
	public DataSubject getSubject(){
		return subject;
	}
	
	public void registerObserver(Observer o){
		subject.registerObserver(o);
	}
	
	public void unregisterObserver(Observer o){
		subject.unregisterObserver(o);
	}
	
	public void notifyObservers(){
		subject.notifyObservers();
	}
}