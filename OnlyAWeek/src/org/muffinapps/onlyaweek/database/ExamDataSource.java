package org.muffinapps.onlyaweek.database;

import java.util.Calendar;

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
	private static final int VERSION = 4;
	public static final String [] NAME_COL = {"_id", "name", "date", "totalPag", "remainingPag", "preparing", "revisionDays"};
	
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
		content.put(ExamDataSource.NAME_COL[6], 0);
		
		db.insert(NAME_TABLE, null, content);
		
		notifyObservers();
	}
	
	public Cursor getExam(long id){
		Cursor c = null;
		
		c = db.query(NAME_TABLE, NAME_COL, NAME_COL[0] + " = ?", new String[]{String.valueOf(id)}, null, null, null, "1");
		
		c.moveToFirst();
		
		return c;
	}
	
	public Cursor getAllExam(String order){
		Cursor c = null;
		
		c = db.query(NAME_TABLE, NAME_COL, null, null, null, null, order);
		
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
	
	public void editExam(long id, String name, Calendar date, int totalPag){
		Cursor c = null;
		
		c = db.query(NAME_TABLE, new String[]{NAME_COL[0], NAME_COL[3]}, NAME_COL[0] + " = ?", new String[]{String.valueOf(id)}, null, null, null, "1");
		
		c.moveToFirst();
		
		ContentValues content = new ContentValues();
		content.put(ExamDataSource.NAME_COL[1], name);
		content.put(ExamDataSource.NAME_COL[2], date.getTimeInMillis());
		if(totalPag != c.getLong(c.getColumnIndex(NAME_COL[3]))){
			content.put(ExamDataSource.NAME_COL[3], totalPag);
			content.put(ExamDataSource.NAME_COL[4], totalPag);
		}
		
		c.close();
		
		db.update(NAME_TABLE, content, NAME_COL[0] + " = " + id, null);
		
		notifyObservers();
	}
	
	public void updatePlanning(long id, int pages){
		String sqlQuery = "UPDATE " + NAME_TABLE + " SET " + NAME_COL[4] + " = " + NAME_COL[4] + " - " + pages + " WHERE " + NAME_COL[0] + " = " + id;  
		db.execSQL(sqlQuery);

		notifyObservers();
	}
	
	public void editPlanning(long id, boolean planning){
		ContentValues content = new ContentValues();
		content.put(ExamDataSource.NAME_COL[5], planning ? 1 : 0); 
		
		db.update(NAME_TABLE, content, NAME_COL[0] + " = " + id, null);
		
		notifyObservers();
	}
	
	public void editRevisionDays(long id, int days){
		ContentValues content = new ContentValues();
		content.put(ExamDataSource.NAME_COL[6], days); 
		
		db.update(NAME_TABLE, content, NAME_COL[0] + " = " + id, null);
		
		notifyObservers();
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
                NAME_COL[5] + " INTEGER NOT NULL, " +
                NAME_COL[6] + " INTEGER NOT NULL);";

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