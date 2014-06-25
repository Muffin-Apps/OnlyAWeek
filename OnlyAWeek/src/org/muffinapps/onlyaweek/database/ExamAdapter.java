package org.muffinapps.onlyaweek.database;

import java.util.GregorianCalendar;

import org.muffinapps.onlyaweek.R;
import org.muffinapps.onlyaweek.StudyingRatio;

import com.fortysevendeg.swipelistview.SwipeListView;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExamAdapter extends CursorAdapter{

	public ExamAdapter(Context context) {
		super(context, null, false);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listitem_exam, null, false);
 
        return view;
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
		//view.setBackgroundResource(R.drawable.choice_unselected);
		TextView name = (TextView) view.findViewById(R.id.nameExam);
		name.setText(cursor.getString(1));
		
		TextView date = (TextView) view.findViewById(R.id.dateExam);
		GregorianCalendar examDate = new GregorianCalendar();
		examDate.setTimeInMillis(cursor.getLong(2));
		date.setText(DateFormat.format("dd/M/yyyy", examDate.getTime()));
		
		boolean preparing = cursor.getInt(5) == 1;
		View colorLayout = view.findViewById(R.id.colorExam);
		
		if(preparing){
			int remainingPages = cursor.getInt(4), revisionDays = cursor.getInt(6);
			StudyingRatio ratio = new StudyingRatio(remainingPages, examDate, revisionDays);
			int colorResource = ratio.getRatioColorResource();
			
			colorLayout.setBackgroundResource(colorResource);
		}else{
			colorLayout.setBackgroundResource(R.color.color_not_preparing);
		}
		
	}

}
