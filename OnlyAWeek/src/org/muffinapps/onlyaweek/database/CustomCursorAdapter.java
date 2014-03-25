package org.muffinapps.onlyaweek.database;

import org.muffinapps.onlyaweek.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter{


	public CustomCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup view) {
		LayoutInflater inflater = LayoutInflater.from(view.getContext());
        View retView = inflater.inflate(R.layout.subject, view, false);
 
        return retView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
	
		TextView name = (TextView) view.findViewById(R.id.nameSubject);
		name.setText(cursor.getString(1));
		
		TextView date = (TextView) view.findViewById(R.id.dateSubject);
		date.setText(cursor.getString(2));
		
		TextView remainingPag = (TextView) view.findViewById(R.id.remainingPag);
		remainingPag.setText(cursor.getString(3));
		
		TextView assignedPag = (TextView) view.findViewById(R.id.assignedPag);
		assignedPag.setText(cursor.getString(4));
		
		TextView totalPag = (TextView) view.findViewById(R.id.totalPag);
		totalPag.setText(cursor.getString(5));
	}

}
