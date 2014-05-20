package org.muffinapps.onlyaweek.database;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.muffinapps.onlyaweek.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter{


	public CustomCursorAdapter(Context context) {
		super(context, null, false);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup view) {
		LayoutInflater inflater = LayoutInflater.from(view.getContext());
       // View retView = inflater.inflate(R.layout.subject, view, false);
		View retView = inflater.inflate(R.layout.list_exam_item, view, false);
        return retView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
	/*
		TextView name = (TextView) view.findViewById(R.id.nameSubject);
		name.setText(cursor.getString(1));
		
		TextView date = (TextView) view.findViewById(R.id.dateSubject);
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(cursor.getLong(2));
		date.setText(DateFormat.format("dd/M/yyyy", cal.getTime()));
		
		TextView remainingPag = (TextView) view.findViewById(R.id.remainingPag);
		remainingPag.setText(cursor.getString(3));
		
		TextView assignedPag = (TextView) view.findViewById(R.id.assignedPag);
		assignedPag.setText(cursor.getString(4));
		
		TextView totalPag = (TextView) view.findViewById(R.id.totalPag);
		totalPag.setText(cursor.getString(5));*/
	}

}
