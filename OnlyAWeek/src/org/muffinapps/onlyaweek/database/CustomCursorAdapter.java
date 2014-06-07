package org.muffinapps.onlyaweek.database;

import java.util.GregorianCalendar;

import org.muffinapps.onlyaweek.R;
import org.muffinapps.onlyaweek.StudyingRatioUtils;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.SwipeListView;

public class CustomCursorAdapter extends CursorAdapter{


	public CustomCursorAdapter(Context context) {
		super(context, null, false);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup view) {
		LayoutInflater inflater = LayoutInflater.from(view.getContext());
		View retView = inflater.inflate(R.layout.subject, view, false);
        return retView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup){
		View view = super.getView(position, convertView, viewGroup);
		
		if(viewGroup instanceof SwipeListView)
			((SwipeListView) viewGroup).recycle(view, position);
		
		return view;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		int remainingPages, revisionDays;
		GregorianCalendar examDate;
		
		remainingPages = cursor.getInt(4);
		revisionDays = cursor.getInt(6);
		
		TextView name = (TextView) view.findViewById(R.id.nameSubject);
		name.setText(cursor.getString(1));
		
		TextView date = (TextView) view.findViewById(R.id.dateSubject);
		examDate = new GregorianCalendar();
		examDate.setTimeInMillis(cursor.getLong(2));
		date.setText(DateFormat.format("dd/M/yyyy", examDate.getTime()));
		
		TextView remainingPag = (TextView) view.findViewById(R.id.remainingPag);
		remainingPag.setText(""+remainingPages);
		
		TextView assignedPag = (TextView) view.findViewById(R.id.assignedPag);
		float ratio = StudyingRatioUtils.calculateRatio(remainingPages, examDate, revisionDays);
		assignedPag.setText(StudyingRatioUtils.getRatioString(ratio));
		
		TextView totalPag = (TextView) view.findViewById(R.id.totalPag);
		totalPag.setText(""+cursor.getInt(3));
	}

}
