package org.muffinapps.onlyaweek;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends FragmentActivity implements PagerAdapter.PageProvider, TabListener{
	private static final int PREPARING_LIST = 0,
			NO_PREPARING_LIST = 1,
			ALL_LIST = 2;
	
	private ViewPager viewPager;
	private List<Fragment> listFragments;
	private List<String> listTitles;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		listFragments = new ArrayList<Fragment>();
		listTitles = new ArrayList<String>();
		for(int i=0; i<3; i++)
			listFragments.add(null);
		listTitles.add(getResources().getString(R.string.preparingList_title));
		listTitles.add(getResources().getString(R.string.notPreparingList_title));
		listTitles.add(getResources().getString(R.string.allList_title));
		
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

	
	
	//*****************************************************************************************************************
	// Implementacion PageProvider
	//*****************************************************************************************************************
	
	@Override
	public Fragment getPage(int i) {
		// TODO Aqui irian la inicializacion de las listas
		// Lo que hay ahora mismo es simplemente para poder probarlo
		
		Fragment result = listFragments.get(i);
		if(result == null)
			result = new DummyFragment();
		
		return result;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public String getPageTitle(int i) {
		return listTitles.get(i);
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

}
