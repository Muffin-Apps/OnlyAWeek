package org.muffinapps.onlyaweek;

import java.util.ArrayList;
import java.util.List;

import database.CustomCursorAdapter;
import database.ExamCursorAdapter;
import database.ExamDataSource;
import prueba.DataSubject;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.ContentValues;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements PagerAdapter.PageProvider, TabListener{
	private static final int PREPARING_LIST = 0,
			NO_PREPARING_LIST = 1,
			ALL_LIST = 2;
	
	private ViewPager viewPager;
	private List<ListFragment> listFragments;
	private String[] listTitles;
	private ExamDataSource db;
	private CustomCursorAdapter adapterExamPrepar;
	private ExamCursorAdapter adapterExam;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new ExamDataSource(this);
		insert();
		
		adapterExamPrepar = new CustomCursorAdapter(this, db.getExamPreparation(), false);
		adapterExam = new ExamCursorAdapter(this, db.getExamNotPreparation(), false);
		
		setContentView(R.layout.activity_main);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		listFragments = new ArrayList<ListFragment>();
		listTitles = getResources().getStringArray(R.array.lists_titles);
		for(int i=0; i<3; i++)
			listFragments.add(null);
		
		final ActionBar actionBar = getActionBar();
		PagerAdapter adapter = new PagerAdapter(this, getSupportFragmentManager());
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
            public void onPageSelected(int pos) {
                actionBar.setSelectedNavigationItem(pos);
            }
        });
		
		actionBar.addTab(actionBar.newTab().setText(getPageTitle(PREPARING_LIST)).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(getPageTitle(NO_PREPARING_LIST)).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(getPageTitle(ALL_LIST)).setTabListener(this));
		
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
	            //TODO
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

	
	
	//*****************************************************************************************************************
	// Implementacion PageProvider
	//*****************************************************************************************************************
	
	@Override
	public Fragment getPage(int i) {
		// TODO Aqui irian la inicializacion de las listas
		// Lo que hay ahora mismo es simplemente para poder probarlo
		
		ListFragment result = listFragments.get(i);
		
		if(result == null){
			if(i == 0){
				result = new ListFragment();
				result.setListAdapter(adapterExamPrepar);
				
				adapterExamPrepar.changeCursor(db.getExamPreparation());
			}else{
				result = new ListFragment();
				result.setListAdapter(adapterExam);
				
				if(i == 1)
					adapterExam.changeCursor(db.getExamNotPreparation());
				else
					adapterExam.changeCursor(db.getAllExam());
			}
		}
		return result;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public String getPageTitle(int i) {
		return listTitles[i];
	}

	
	
	//*****************************************************************************************************************
	// Implementacion TabListener
	//*****************************************************************************************************************
	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	

	// Esta clase esta simplemente para poder probarlo
	public static class DummyFragment extends Fragment{		
		@Override
		public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState){
			return inflater.inflate(android.R.layout.simple_list_item_1, container, false);
		}
	}
	
	public void insert(){
		ContentValues content = new ContentValues();
		content.put(ExamDataSource.NAME_COL[1], data[0].name);
		content.put(ExamDataSource.NAME_COL[2], data[0].date);
		content.put(ExamDataSource.NAME_COL[3], data[0].assignedPag);
		content.put(ExamDataSource.NAME_COL[4], data[0].remainingPag);
		content.put(ExamDataSource.NAME_COL[5], data[0].totalPag);
		
		db.insert(content);
		
		content.clear();
		
		content.put(ExamDataSource.NAME_COL[1], data[1].name);
		content.put(ExamDataSource.NAME_COL[2], data[1].date);
		
		db.insert(content);
	}
	
	private DataSubject[] data = new DataSubject[]{
			new DataSubject("Dispositivos Moviles", "05/06/2014", 1200, 80, 2),
			new DataSubject("DIU", "20/06/2014", 0, 0, 0),
			};

}
