package org.muffinapps.onlyaweek;

import org.muffinapps.onlyaweek.database.CustomCursorAdapter;
import org.muffinapps.onlyaweek.database.ExamCursorAdapter;
import org.muffinapps.onlyaweek.database.ExamDataSource;

import prueba.DataSubject;
import android.os.Bundle;
import android.content.ContentValues;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements AbsListView.MultiChoiceModeListener, OnItemLongClickListener{
	private static final int PREPARING_LIST = 0,
			NO_PREPARING_LIST = 1,
			ALL_LIST = 2;
	
	private ActionMode actionMode;
	private ExamDataSource db;
	private CustomCursorAdapter adapterExamPrepar;
	private ExamCursorAdapter adapterExam;
	private ListFragment currentListFragment;
	private int numItemsSelected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new ExamDataSource(this);
		insert();
		
		adapterExamPrepar = new CustomCursorAdapter(this, db.getExamPreparation(), false);
		adapterExam = new ExamCursorAdapter(this, db.getExamNotPreparation(), false);
		
	}

	@Override
	public void onStart(){
		super.onStart();
		//PA PROBAR NA MAS
		ListFragment fragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.FrgList);
		//adapterExam = new ExamCursorAdapter(this, db.getAllExam(), false);
		fragment.getListView().setAdapter(adapterExam);
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

	@Override
	public boolean onActionItemClicked(ActionMode arg0, MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_edit:
			//TODO
			return true;
		case R.id.action_delete:
			//TODO dialog
			//db.deleteExams(currentListFragment.getListView().getCheckedItemIds());
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean onCreateActionMode(ActionMode arg0, Menu menu) {
		numItemsSelected = 0;
		getMenuInflater().inflate(R.menu.main_context, menu);
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
		
		return false;
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode actionMode, int arg1, long arg2,
			boolean checked) {
		android.util.Log.v("XXXX", "State checked " + checked);
		if(checked){
			numItemsSelected++;
			if(numItemsSelected > 1){
				MenuItem item = actionMode.getMenu().findItem(R.id.action_edit);
				item.setEnabled(false).setVisible(false);
			}
		}else{
			numItemsSelected--;
			if(numItemsSelected == 1){
				MenuItem item = actionMode.getMenu().findItem(R.id.action_edit);
				item.setEnabled(false).setVisible(false);
			}
			if(numItemsSelected == 0){
				actionMode.finish();
				actionMode = null;
			}
		}
		android.util.Log.v("XXXX", "State checked " + numItemsSelected);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos,
			long id) {
		if(actionMode != null)
			return false;
		
		actionMode = startActionMode(this);
		ListView currentList = currentListFragment.getListView(); 
		currentList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		currentList.setMultiChoiceModeListener(this);
		//view.setSelected(true);
		
		return true;
	}


}
