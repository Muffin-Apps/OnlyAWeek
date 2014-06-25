package org.muffinapps.onlyaweek.database;

import org.emud.content.Query;

import android.database.Cursor;

public class QueryExamList implements Query<Cursor> {
	public static final int ORDER_DATE = 0,
							ORDER_NAME = 1,
							ORDER_PREPARING = 2;
	
	private ExamDataSource db;
	private int typeQuery;
	
	public QueryExamList(ExamDataSource db){
		this.db = db;
	}
	
	public void setTypeQuery(int newType){
		typeQuery = newType;
	}
	
	@Override
	public Cursor execute() {
		switch(typeQuery){
		case ORDER_DATE:
			return db.getAllExam(ExamDataSource.NAME_COL[2]);
		case ORDER_NAME:
			return db.getAllExam(ExamDataSource.NAME_COL[1]);
		case ORDER_PREPARING:
			return db.getAllExam(ExamDataSource.NAME_COL[5] + " DESC");
		default:
			break;
		}
		return null;
	}

}
