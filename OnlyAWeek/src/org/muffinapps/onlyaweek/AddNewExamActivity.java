package org.muffinapps.onlyaweek;

import java.util.Calendar;

import org.muffinapps.onlyaweek.AddNewExamFragment.OnConfirmListener;
import org.muffinapps.onlyaweek.database.ExamDataSource;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class AddNewExamActivity extends FragmentActivity implements OnConfirmListener{
	private ExamDataSource dataBase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_add_new_exam);
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		AddNewExamFragment addFragment = new AddNewExamFragment();
		addFragment.setOnConfirmListener(this);
		fragmentTransaction.replace(R.id.add_exam_content_frame, addFragment);
		fragmentTransaction.commit();
		
	}

	@Override
	public void onAdd(String name, Calendar date, int totalPages) {
		// TODO Auto-generated method stub
		if(dataBase == null)
			dataBase = ((OnlyAWeekApplication) getApplicationContext()).getDataBase();
		
		dataBase.insertNewExam(name, date, totalPages);
		this.finish();
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
		
	}

	
}
