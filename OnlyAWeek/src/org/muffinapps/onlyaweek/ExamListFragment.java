package org.muffinapps.onlyaweek;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ExamListFragment extends ListFragment {
	private ExamActionListener actionListener;
	
	public void setExamActionListener(ExamActionListener listener){
		actionListener = listener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle args){
		return inflater.inflate(R.layout.list_exam, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		ExamListListener examListener;
		SwipeListView swipeListView = (SwipeListView) getListView();
		
		swipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_CHOICE);
		
		examListener = new ExamListListener(swipeListView);
		
		swipeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			swipeListView.setMultiChoiceModeListener(examListener);
		
		swipeListView.setSwipeListViewListener(examListener);
		swipeListView.setOnItemClickListener(examListener);
	}
	
	public static interface ExamActionListener{
		//TODO Algo como
		/*
		 * public void onExamClick(Exam exam);
		 * 
		 * public void onExamEdit(Exam exam);
		 * 
		 * public void onExamsDelete(List<Exam> exams);
		 */
	}
	
	public static class ExamListListener extends BaseSwipeListViewListener implements AbsListView.MultiChoiceModeListener, OnItemClickListener{
		private SwipeListView swipeListView;
		
		public ExamListListener(SwipeListView listView){
			swipeListView = listView;
		}
		
		@Override
		public void onOpened(int position, boolean toRight) {
			// TODO Lamar al actionListener.onExamEdit()
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
			mode.setTitle("Selected (" + swipeListView.getCountSelected() + ")");
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_delete:
				// TODO Lamar al actionListener.onExamsDelete()
				return true;
			default:
				return false;
			}
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.main_context, menu);
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			swipeListView.unselectedChoiceStates();
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			// TODO Lamar al actionListener.onExamClick()
		}
	}
}
