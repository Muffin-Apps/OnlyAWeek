package org.muffinapps.onlyaweek;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewExamFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
	private EditText name;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance){
		
		return inflater.inflate(R.layout.add_new_exam, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state){
		
		name = (EditText) getView().findViewById(R.id.addExamName);
		name.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerDialogFragment(AddNewExamFragment.this);
				newFragment.show(getFragmentManager(), "date_dialog");
			}
		});
		
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		SimpleDateFormat f = new SimpleDateFormat("dd/mm/yyyy");
		name.setText(f.format(cal.getTime()));
	}
	
	public void mostrarDetalle(String texto){
		
	}
}
