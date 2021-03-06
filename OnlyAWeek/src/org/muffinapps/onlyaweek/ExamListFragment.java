package org.muffinapps.onlyaweek;

import java.util.List;

import org.emud.support.v4.content.ObserverCursorLoader;
import org.muffinapps.onlyaweek.database.QueryExamList;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
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

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

public class ExamListFragment extends ListFragment implements LoaderCallbacks<Cursor>{
	private ExamActionListener actionListener;
	private QueryExamList query;
	
	public void setExamActionListener(ExamActionListener listener){
		actionListener = listener;
	}
	
	public void setQuery(QueryExamList q){
		query = q;
	}
	
	public void setTypeQuery(int type){
		if(query != null){
			query.setTypeQuery(type);
			if(isAdded()){
				getLoaderManager().restartLoader(0, null, this);
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		if(query != null)
			getLoaderManager().initLoader(0, null, this);
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
		swipeListView.setSwipeOpenOnLongPress(false);
		
		examListener = new ExamListListener(this, swipeListView, actionListener);
		
		swipeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			swipeListView.setMultiChoiceModeListener(examListener);
		
		swipeListView.setSwipeListViewListener(examListener);
		swipeListView.setOnItemClickListener(examListener);
	}
	
	public static interface ExamActionListener{
		public void onExamsLoaded();
		
		public void onExamClick(long id);

		public void onExamEdit(long id);

		public void onExamsDelete(long[] id);
	}
	
	public long getExamId(int position){
		CursorAdapter adapter = (CursorAdapter) getListAdapter();
		
		if(adapter == null)
			return -1;
		
		return adapter.getItemId(position);
	}
	
	public static class ExamListListener extends BaseSwipeListViewListener implements AbsListView.MultiChoiceModeListener, OnItemClickListener{
		private SwipeListView swipeListView;
		private ListFragment examListFragment;
		private ExamActionListener examActionListener;
		
		public ExamListListener(ListFragment fragment, SwipeListView listView, ExamActionListener lstn){
			examListFragment = fragment;
			swipeListView = listView;
			examActionListener = lstn;
		}
		
		@Override
		public void onOpened(int position, boolean toRight) {
			swipeListView.closeOpenedItems();
			examActionListener.onExamEdit(swipeListView.getAdapter().getItemId(position));
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
			mode.setTitle("Selecionado/s (" + swipeListView.getCountSelected() + ")");
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_delete:
				CursorAdapter adapter = (CursorAdapter) examListFragment.getListAdapter();
				List<Integer> position = swipeListView.getPositionsSelected();
				int n = position.size();
				long[] ids = new long[n];
				
				for(int i=0; i<n; i++)
					ids[i] = adapter.getItemId(position.get(i));
				
				mode.finish();
				
				examActionListener.onExamsDelete(ids);
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
		public void onClickFrontView(int position){
			CursorAdapter adapter = (CursorAdapter) examListFragment.getListAdapter();
			long examId = adapter.getItemId(position);
			
			examActionListener.onExamClick(examId);			
		}

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			CursorAdapter adapter = (CursorAdapter) examListFragment.getListAdapter();
			long examId = adapter.getItemId(position);
			
			examActionListener.onExamClick(examId);
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		
		return new ObserverCursorLoader( getActivity(), query, ((OnlyAWeekApplication) getActivity().getApplicationContext()).getDataBase().getSubject());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		
		((CursorAdapter) getListAdapter()).swapCursor(arg1);
		
		if(actionListener != null)
			actionListener.onExamsLoaded();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
		((CursorAdapter) getListAdapter()).swapCursor(null);
	}
}
