package org.muffinapps.onlyaweek;

import java.util.Calendar;

import org.muffinapps.onlyaweek.AddNewExamFragment.OnConfirmListener;
import org.muffinapps.onlyaweek.database.ExamDataSource;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

public class AddNewExamActivity extends FragmentActivity implements OnConfirmListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_add_new_exam);
		
		AddNewExamFragment addFragment = new AddNewExamFragment();
		Intent intent = getIntent();
		String action = intent.getAction(); 
		
		if(action != null && action.equals(Intent.ACTION_EDIT)){
			addFragment.setArguments(intent.getExtras());
		}
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		addFragment.setOnConfirmListener(this);
		fragmentTransaction.replace(R.id.add_exam_content_frame, addFragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onAdd(String name, Calendar date, int totalPages) {		
		((OnlyAWeekApplication) getApplicationContext()).getDataBase().insertNewExam(name, date, totalPages);
		this.finish();
	}

	@Override
	public void onEdit(long id, String name, Calendar date, int totalPages) {
		android.util.Log.d("Act", "onEdit");
		ExamDataSource dataSource = ((OnlyAWeekApplication) getApplicationContext()).getDataBase();
		
		dataSource.editExam(id, name, date, totalPages);
		this.finish();
	}
}
