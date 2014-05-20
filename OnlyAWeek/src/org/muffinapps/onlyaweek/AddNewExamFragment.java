package org.muffinapps.onlyaweek;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class AddNewExamFragment extends Fragment implements DatePickerDialog.OnDateSetListener, OnClickListener{
	private static final String ID_KEY = "id", NAME_KEY = "name", DATE_KEY = "date", PAGES_KEY = "pages";
	private long id = -1;
	private TextView date;
	private OnConfirmListener listener;
	private Calendar cal;
	
	public static Bundle getArgsAsBundle(long id, String name, Calendar date, int totalpages){
		Bundle bundle = new Bundle();
		
		bundle.putLong(ID_KEY, id);
		bundle.putString(NAME_KEY, name);
		bundle.putLong(DATE_KEY, date.getTimeInMillis());
		bundle.putInt(PAGES_KEY, totalpages);
		
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
		
		date = (TextView) getView().findViewById(R.id.addExamDate);
		
		date.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerDialogFragment(AddNewExamFragment.this);
				newFragment.show(getFragmentManager(), "date_dialog");
			}
		});
		

		Bundle args = getArguments();
		
		if(args != null){
			id = args.getLong(ID_KEY, -1);
			((EditText) getView().findViewById(R.id.addExamName)).setText(args.getString(NAME_KEY));
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTimeInMillis(args.getLong(DATE_KEY));
			date.setText(DateFormat.format("dd/M/yyyy", cal.getTime()));
			((EditText) getView().findViewById(R.id.addExamPages)).setText("" + args.getInt(PAGES_KEY));
		}
		
		
		getView().findViewById(R.id.buttonAddNewExam).setOnClickListener(this);
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		if(cal == null)
			cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		else
			cal.set(year, monthOfYear, dayOfMonth);
		
		date.setText(DateFormat.format("dd/M/yyyy", cal.getTime()));
	}
	
	public void setOnConfirmListener(OnConfirmListener l){
		listener = l;
	}
	
	public void mostrarDetalle(String texto){
		
	}
	
	public interface OnConfirmListener{
		public void onAdd(String name, Calendar date, int totalPages);
		public void onEdit(long id, String name, Calendar date, int totalPages);
	}

	@Override
	public void onClick(View v) {
		String name = ((EditText) getView().findViewById(R.id.addExamName)).getText().toString();
		int totalPages = Integer.parseInt(((EditText) getView().findViewById(R.id.addExamPages)).getText().toString());
		
		if(id == -1){
			listener.onAdd(name, cal, totalPages);
		}else{
			listener.onEdit(id, name, cal, totalPages);
		}
	}
}
