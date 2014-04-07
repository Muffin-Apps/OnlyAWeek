package org.muffinapps.onlyaweek;

import android.support.v4.app.ListFragment;
import android.widget.AdapterView.OnItemLongClickListener;

public class ExamListFragment extends ListFragment {
	private OnItemLongClickListener listener;
	
	@Override
	public void onResume(){
		super.onResume();
		if(listener != null)
			getListView().setOnItemLongClickListener(listener);
	}

	/**
	 * @return the listener
	 */
	public OnItemLongClickListener getLongClickListener() {
		return listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setLongClickListener(OnItemLongClickListener listener) {
		this.listener = listener;
	}
}
