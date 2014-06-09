package org.muffinapps.onlyaweek;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.emud.support.v4.content.ObserverCursorLoader;
import org.muffinapps.onlyaweek.database.ExamDataSource;
import org.muffinapps.onlyaweek.database.QueryExam;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class AddNewExamFragment extends Fragment implements DatePickerDialog.OnDateSetListener, OnClickListener, LoaderCallbacks<Cursor>{
	private static final String ID_KEY = "id";
	private long id = -1;
	private TextView dateText;
	private OnConfirmListener listener;
	private Calendar date;
	private QueryExam query;
	
	public static Bundle getArgsAsBundle(long id){
		Bundle bundle = new Bundle();
		
		bundle.putLong(ID_KEY, id);
		
		return bundle;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance){
		View view = inflater.inflate(R.layout.add_new_exam, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle state){
		super.onActivityCreated(state);
		
		dateText = (TextView) getView().findViewById(R.id.addExamDate);
		
		dateText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerDialogFragment(AddNewExamFragment.this);
				newFragment.show(getFragmentManager(), "date_dialog");
			}
		});
		

		Bundle args = getArguments();
		
		if(args != null){
			id = args.getLong(ID_KEY, -1);
			ExamDataSource ds = ((OnlyAWeekApplication) getActivity().getApplicationContext()).getDataBase();
			query = new QueryExam(ds, id);
			getLoaderManager().initLoader(0, null, this);
		}
		
		
		getView().findViewById(R.id.buttonAddNewExam).setOnClickListener(this);
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		if(date == null)
			date = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		else
			date.set(year, monthOfYear, dayOfMonth);
		
		dateText.setText(DateFormat.format("dd/M/yyyy", date.getTime()));
	}
	
	public void setExamId(long newId){
		id = newId;

		if(query == null){
			ExamDataSource ds = ((OnlyAWeekApplication) getActivity().getApplicationContext()).getDataBase();
			query = new QueryExam(ds, id);
		}
		
		getLoaderManager().restartLoader(0, null, this);
	}
	
	public void setOnConfirmListener(OnConfirmListener l){
		listener = l;
	}

	@Override
	public void onClick(View v) {
		String name = ((EditText) getView().findViewById(R.id.addExamName)).getText().toString();
		int totalPages = Integer.parseInt(((EditText) getView().findViewById(R.id.addExamPages)).getText().toString());
		
		android.util.Log.d("ANEF", "onClick");
		
		if(id == -1){
			android.util.Log.d("ANEF", "onAdd");
			listener.onAdd(name, date, totalPages);
		}else{
			listener.onEdit(id, name, date, totalPages);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new ObserverCursorLoader(getActivity(), query);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		((EditText) getView().findViewById(R.id.addExamName)).setText(cursor.getString(cursor.getColumnIndex(ExamDataSource.NAME_COL[1])));
		date = new GregorianCalendar();
		date.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(ExamDataSource.NAME_COL[2])));
		dateText.setText(DateFormat.format("dd/M/yyyy", date.getTime()));
		((EditText) getView().findViewById(R.id.addExamPages)).setText("" + cursor.getInt(cursor.getColumnIndex(ExamDataSource.NAME_COL[3])));
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
	}
	
	public interface OnConfirmListener{
		public void onAdd(String name, Calendar date, int totalPages);
		public void onEdit(long id, String name, Calendar date, int totalPages);
	}
}
