package org.muffinapps.onlyaweek;

import org.muffinapps.onlyaweek.database.ExamDataSource;
import android.app.Application;

public class OnlyAWeekApplication extends Application{
	private ExamDataSource dataBase;
	
	public ExamDataSource getDataBase(){
		if(dataBase == null)
			dataBase = new ExamDataSource(this);
		
		return dataBase;
	}
}
