package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamDataSource{
	private static final String NAME_DB = "exams_db";
	private static final String NAME_TABLE = "exams";
	private static final int VERSION = 1;
	private static final String [] NAME_COL = {"name", "date", "remainingPag",
												"assignedPag", "totalPag"
	};
	
	private SQLiteDatabase db;
    private ExamSQLiteHelper dbExamSqliteHelper;
    
	public ExamDataSource(Context context){
		dbExamSqliteHelper = new ExamSQLiteHelper(context, NAME_DB, null, VERSION);
	}
	
	private void openDBModeWriter(){
		if(db != null){
			db = dbExamSqliteHelper.getWritableDatabase();
		}
	}
	
	private void openDBModeReader(){
		if(db != null){
			db = dbExamSqliteHelper.getReadableDatabase();
		}
	}
	
	private void closeDB(){
        if(db != null){
            db.close();
            db = null;
        }
    }
	
	public Cursor getExam(long id){
		Cursor c = null;
		
		openDBModeReader();
		
		c = db.query(NAME_TABLE, NAME_COL, NAME_COL[0] + " = ?", new String[]{String.valueOf(id)}, null, null, null, "1");
		
		c.moveToFirst();
		
		closeDB();
		
		return c;
	}
	
	public Cursor getAllExam(){
		Cursor c = null;
		
		openDBModeReader();
		
		c = db.query(NAME_TABLE, NAME_COL, null, null, null, null, null);
		
		c.moveToFirst();
		
		closeDB();
		
		return c;
	}
	
	public Cursor getExamPreparation(){
		Cursor c = null;
		
		openDBModeReader();
		
		c = db.query(NAME_TABLE, NAME_COL, NAME_COL[4] + " != null", null, null, null, null);
		
		c.moveToFirst();
		
		closeDB();
		
		return c;
	}
	
	/*
	 * Decision de diseño:
	 *  Yo creo que el nombre de la asign. es unico salvo los acronimos que creo que en distintas
	 *  carreras se pueden repetir. Pero como la aplicacion es para un usuario no creo que esto pase.
	 *
	 */
	private static class ExamSQLiteHelper extends SQLiteOpenHelper{
		private static final String DB_CREATE="CREATE TABLE " + NAME_TABLE +
                " ("+
                NAME_COL[0] + " TEXT PRIMARY KEY, " +  
                NAME_COL[1] + " TEXT NOT NULL, " +
                NAME_COL[2] + " INTEGER NOT NULL, " +
                NAME_COL[3] + " INTEGER NOT NULL, " +
                NAME_COL[4] + " INTEGER NOT NULL);";

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