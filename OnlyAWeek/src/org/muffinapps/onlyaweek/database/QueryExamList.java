package org.muffinapps.onlyaweek.database;

import org.emud.content.Query;

import android.database.Cursor;

public class QueryExamList implements Query<Cursor> {
	public static final int ALL_EXAM = 0,
							EXAM_PREPARATION = 1,
							EXAM_NOT_PREPARATION =2;
	
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
		case ALL_EXAM:
			return db.getAllExam();
		case EXAM_PREPARATION:
			return db.getExamPreparation();
		case EXAM_NOT_PREPARATION:
			return db.getExamNotPreparation();
		default:
			break;
		}
		return null;
	}

}
