package org.muffinapps.onlyaweek.database;

import org.emud.content.Query;

import android.database.Cursor;

public class QueryExam implements Query<Cursor> {
	private ExamDataSource dataSource;
	private long examId;
	
	public QueryExam(ExamDataSource ds, long id){
		dataSource = ds;
		examId = id;
	}
	
	public void setExamId(long id){
		examId = id;
	}

	@Override
	public Cursor execute() {
		return dataSource.getExam(examId);
	}

}
