package org.muffinapps.onlyaweek;

import java.util.List;

import org.emud.content.Query;
import org.muffinapps.onlyaweek.ExamListFragment.ExamActionListener;
import org.muffinapps.onlyaweek.database.CustomCursorAdapter;
import org.muffinapps.onlyaweek.database.ExamCursorAdapter;
import org.muffinapps.onlyaweek.database.ExamDataSource;
import org.muffinapps.onlyaweek.database.QueryExam;

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

public class MainActivity extends FragmentActivity implements OnNavigationListener, ExamActionListener{
	private static final int PREPARING_LIST = 0,
			NO_PREPARING_LIST = 1,
			ALL_LIST = 2;
	
	private ExamDataSource dataBase;
	private ExamListFragment allListFragment, preparingListFragment, notPreparingListFragment;
	
	private int currentListContent;
	private QueryExam queryExam;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dataBase = ((OnlyAWeekApplication) this.getApplicationContext()).getDataBase();
	
		
		String[] listnames = getResources().getStringArray(R.array.lists_titles);
		ArrayAdapter<String> aAdpt = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, listnames);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		actionBar.setListNavigationCallbacks(aAdpt, this);
		
		if(savedInstanceState != null){
			currentListContent = savedInstanceState.getInt("currentListContent", PREPARING_LIST);	
		}else{
			currentListContent = PREPARING_LIST;
		}
		
		setListContent();
		
		queryExam = new QueryExam(dataBase);
	}

	@Override
	public void onSaveInstanceState(Bundle saveInstanceState){
		super.onSaveInstanceState(saveInstanceState);		
		saveInstanceState.putInt("currentListContent", currentListContent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_add:
	        	View view = findViewById(R.id.add_exam_content_frame);
	        	if(view == null){
	        		Intent intent = new Intent(this, AddNewExamActivity.class);
		            startActivity(intent);
	        	}else{
	        		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
	        		AddNewExamFragment addFragment = new AddNewExamFragment();
	        		fragmentTransaction.replace(R.id.add_exam_content_frame, addFragment);
	        		fragmentTransaction.commit();
	        	}	            
	            return true;
	        case R.id.action_sort:
	        	//TODO
	            return true;
	        case R.id.action_settings:
	        	//TODO
	            return true;
	        case R.id.action_exit:
	        	//TODO
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

		

	// Esta clase esta simplemente para poder probarlo
	public static class DummyFragment extends Fragment{		
		@Override
		public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState){
			return inflater.inflate(android.R.layout.simple_list_item_1, container, false);
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
				QueryExam queryPreparExam = new QueryExam(dataBase);
				queryPreparExam.setTypeQuery(QueryExam.EXAM_PREPARATION);
				
				preparingListFragment.setQuery(queryExam);
				preparingListFragment.setListAdapter(new CustomCursorAdapter(this));
				preparingListFragment.setExamActionListener(this);
			}
			listFragment = preparingListFragment;
			break;
		case NO_PREPARING_LIST:
			if(notPreparingListFragment == null){
				notPreparingListFragment = new ExamListFragment();
				QueryExam queryNotPrepar = new QueryExam(dataBase);
				queryNotPrepar.setTypeQuery(QueryExam.EXAM_NOT_PREPARATION);
				preparingListFragment.setQuery(queryExam);
				
				notPreparingListFragment.setListAdapter(new ExamCursorAdapter(this));
				notPreparingListFragment.setExamActionListener(this);
			}
			listFragment = notPreparingListFragment;
			break;
		case ALL_LIST:
			if(allListFragment == null){
				allListFragment = new ExamListFragment();
				QueryExam queryAllExam = new QueryExam(dataBase);
				queryAllExam.setTypeQuery(QueryExam.ALL_EXAM);
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

	@Override
	public void onExamClick(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExamEdit(long id) {
		Intent intent = new Intent(this, AddNewExamActivity.class);
		intent.setAction(Intent.ACTION_EDIT);
		intent.putExtras(AddNewExamFragment.getArgsAsBundle(id));
        startActivity(intent);
	}

	@Override
	public void onExamsDelete(long[] idList) {
		dataBase.deleteExams(idList);
	}

}
