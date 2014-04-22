package org.muffinapps.onlyaweek;

import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

@SuppressLint("ValidFragment")
public class DatePickerDialogFragment extends DialogFragment{
	private OnDateSetListener dateListener;
	
	public DatePickerDialogFragment(){
		
	}
	
	public DatePickerDialogFragment(OnDateSetListener listener){
		dateListener = listener;
	}

	public Dialog OnCreateDialog(Bundle savedInstanceState){
		Calendar cal = Calendar.getInstance();
		
		return new DatePickerDialog(getActivity(), dateListener, cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
	}
	
	
}
