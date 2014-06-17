package org.muffinapps.onlyaweek;

import java.util.Calendar;

import org.muffinapps.onlyaweek.AddNewExamFragment.OnConfirmListener;
import org.muffinapps.onlyaweek.ExamListFragment.ExamActionListener;
import org.muffinapps.onlyaweek.PlanningFragment.PlanningListener;
import org.muffinapps.onlyaweek.database.CustomCursorAdapter;
import org.muffinapps.onlyaweek.database.ExamCursorAdapter;
import org.muffinapps.onlyaweek.database.ExamDataSource;
import org.muffinapps.onlyaweek.database.QueryExamList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

public class MainActivity extends FragmentActivity implements OnNavigationListener,
															ExamActionListener,
															OnConfirmListener,
															PlanningListener{
	private static final int PREPARING_LIST = 0,
			NO_PREPARING_LIST = 1,
			ALL_LIST = 2;
	private static final int ADD_FRAGMENT = 0,
			EDIT_FRAGMENT = 1,
			PLANNING_FRAGMENT = 2;
	
	private ExamListFragment allListFragment, preparingListFragment, notPreparingListFragment;
	private View contentFrame;
	private long examEnhanced;
	private int currentListContent, currentRightContent;
	private Fragment currentRightFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		contentFrame = findViewById(R.id.add_exam_content_frame);
		
		String[] listnames = getResources().getStringArray(R.array.lists_titles);
		ArrayAdapter<String> aAdpt = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, listnames);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		actionBar.setListNavigationCallbacks(aAdpt, this);
		
		if(savedInstanceState != null){
			currentListContent = savedInstanceState.getInt("currentListContent", PREPARING_LIST);
			if(contentFrame != null){
				currentRightContent = savedInstanceState.getInt("currentRightContent", ADD_FRAGMENT);
				examEnhanced = savedInstanceState.getInt("exam", -1);
			}
		}else{
			currentListContent = PREPARING_LIST;
			currentRightContent = ADD_FRAGMENT;
			examEnhanced = -1;
		}
		
		setListContent();
		setRightContent(currentRightContent, examEnhanced);
	}

	@Override
	public void onSaveInstanceState(Bundle saveInstanceState){
		super.onSaveInstanceState(saveInstanceState);		
		saveInstanceState.putInt("currentListContent", currentListContent);
		if(contentFrame != null){
			saveInstanceState.putInt("currentRightContent", currentRightContent);
			saveInstanceState.putLong("exam", examEnhanced);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onDestroy(){
		((OnlyAWeekApplication) getApplicationContext()).closeDataBase();
		super.onDestroy();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_add:
	        	if(contentFrame == null){
	        		Intent intent = new Intent(this, AddNewExamActivity.class);
		            startActivity(intent);
	        	}else{
	        		setRightContent(ADD_FRAGMENT, -1);
	        	}	            
	            return true;
	        case R.id.action_sort:
	        	//TODO
	            return true;
	        case R.id.action_settings:
	        	//TODO
	            return true;
	        case R.id.action_exit:
	        	this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}	
		
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		int newContent;
		switch(position){
		case 0:
			newContent = PREPARING_LIST;
			break;
		case 1:
			newContent = NO_PREPARING_LIST;
			break;
		case 2:
			newContent = ALL_LIST;
			break;
		default: return false;
		}
		
		if(newContent != currentListContent){
			currentListContent = newContent;
			setListContent();
		}
		
		return true;
	}

	private void setListContent() {
		ExamListFragment listFragment = null;
		switch(currentListContent){
		case PREPARING_LIST:
			if(preparingListFragment == null){
				preparingListFragment = new ExamListFragment();
				QueryExamList queryPreparExam = new QueryExamList(((OnlyAWeekApplication) getApplicationContext()).getDataBase());
				queryPreparExam.setTypeQuery(QueryExamList.EXAM_PREPARATION);
				
				preparingListFragment.setQuery(queryPreparExam);
				preparingListFragment.setListAdapter(new CustomCursorAdapter(this));
				preparingListFragment.setExamActionListener(this);
			}
			listFragment = preparingListFragment;
			break;
		case NO_PREPARING_LIST:
			if(notPreparingListFragment == null){
				notPreparingListFragment = new ExamListFragment();
				QueryExamList queryNotPrepar = new QueryExamList(((OnlyAWeekApplication) getApplicationContext()).getDataBase());
				queryNotPrepar.setTypeQuery(QueryExamList.EXAM_NOT_PREPARATION);
				notPreparingListFragment.setQuery(queryNotPrepar);
				
				notPreparingListFragment.setListAdapter(new ExamCursorAdapter(this));
				notPreparingListFragment.setExamActionListener(this);
			}
			listFragment = notPreparingListFragment;
			break;
		case ALL_LIST:
			if(allListFragment == null){
				allListFragment = new ExamListFragment();
				QueryExamList queryAllExam = new QueryExamList(((OnlyAWeekApplication) getApplicationContext()).getDataBase());
				queryAllExam.setTypeQuery(QueryExamList.ALL_EXAM);
				allListFragment.setQuery(queryAllExam);
				
				allListFragment.setListAdapter(new ExamCursorAdapter(this));
				allListFragment.setExamActionListener(this);
			}
			listFragment = allListFragment;
			break;
		}
		
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.main_content_frame, listFragment);
		fragmentTransaction.commit();
	}
	
	private void setRightContent(int newContent, long examId){
		if(contentFrame == null)
			return;
		
		Fragment fragment = null;
		
		switch(newContent){
		case ADD_FRAGMENT:
			currentRightFragment = new AddNewExamFragment();
    		((AddNewExamFragment) currentRightFragment).setOnConfirmListener(this);
			break;
		case EDIT_FRAGMENT:
			if(newContent != currentRightContent){
				fragment = new AddNewExamFragment();
				((AddNewExamFragment) fragment).setArguments(AddNewExamFragment.getArgsAsBundle(examId));
				((AddNewExamFragment) fragment).setOnConfirmListener(this);
				currentRightFragment = fragment;
			}else{
				((AddNewExamFragment) currentRightFragment).setExamId(examId);
			}
			break;
		case PLANNING_FRAGMENT:
			if(newContent != currentRightContent){
				fragment = new PlanningFragment();
				((PlanningFragment) fragment).setArguments(PlanningFragment.getArgsAsBundle(examId));
				((PlanningFragment) fragment).setPlanningListener(this);
				currentRightFragment = fragment;
			}else{
				((PlanningFragment) currentRightFragment).setExamId(examId);
			}
			break;
		default:
			return;
		}
		
		currentRightContent = newContent;
		examEnhanced = examId;
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.add_exam_content_frame, currentRightFragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onExamClick(long id) {
		if(contentFrame == null){
			Intent intent = new Intent(this, PlanningActivity.class);
			intent.putExtras(PlanningFragment.getArgsAsBundle(id));
			startActivity(intent);
		}else{
			setRightContent(PLANNING_FRAGMENT, id);
		}
	}

	@Override
	public void onExamEdit(long id) {
		if(contentFrame == null){
			Intent intent = new Intent(this, AddNewExamActivity.class);
			intent.setAction(Intent.ACTION_EDIT);
			intent.putExtras(AddNewExamFragment.getArgsAsBundle(id));
			startActivity(intent);
		}else{
			setRightContent(EDIT_FRAGMENT, id);
		}
	}

	@Override
	public void onExamsDelete(long[] idList) {
		((OnlyAWeekApplication) getApplicationContext()).getDataBase().deleteExams(idList);
	}

	@Override
	public void onAdd(String name, Calendar date, int totalPages) {
		ExamDataSource dataSource = ((OnlyAWeekApplication) getApplicationContext()).getDataBase();
		
		dataSource.insertNewExam(name, date, totalPages);
	}

	@Override
	public void onEdit(long id, String name, Calendar date, int totalPages) {
		android.util.Log.d("Act", "onEdit");
		ExamDataSource dataSource = ((OnlyAWeekApplication) getApplicationContext()).getDataBase();
		
		dataSource.editExam(id, name, date, totalPages);
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
