package org.muffinapps.onlyaweek;

import org.muffinapps.onlyaweek.PlanningFragment.PlanningListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class PlanningActivity extends FragmentActivity implements PlanningListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_add_new_exam);
		
		PlanningFragment addFragment = new PlanningFragment(); 
		
		addFragment.setArguments(getIntent().getExtras());
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		addFragment.setPlanningListener(this);
		fragmentTransaction.replace(R.id.add_exam_content_frame, addFragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onPlanningSet(long id, boolean planning) {
		((OnlyAWeekApplication) getApplicationContext()).getDataBase().editPlanning(id, planning);
	}

	@Override
	public void onPlanningUpdated(long id, int pages) {
		((OnlyAWeekApplication) getApplicationContext()).getDataBase().updatePlanning(id, pages);
	}

	@Override
	public void onRevisionDaysSet(long id, int revisionDays) {
		((OnlyAWeekApplication) getApplicationContext()).getDataBase().editRevisionDays(id, revisionDays);
	}
}
