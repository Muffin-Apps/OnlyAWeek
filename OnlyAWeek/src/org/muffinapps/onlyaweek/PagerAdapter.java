package org.muffinapps.onlyaweek;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

	PageProvider pageProvider;
	
	public PagerAdapter(PageProvider pp, FragmentManager fm) {
		super(fm);
		pageProvider = pp;
	}

	@Override
	public Fragment getItem(int pos) {
		return pageProvider.getPage(pos);
	}

	@Override
	public int getCount() {
		return pageProvider.getCount();
	}
	
	@Override
    public CharSequence getPageTitle(int pos) {
        return pageProvider.getPageTitle(pos);
    }
	
	public interface PageProvider {

		public Fragment getPage(int i);
		
		public int getCount();
		
		public String getPageTitle(int i);
	}

}
