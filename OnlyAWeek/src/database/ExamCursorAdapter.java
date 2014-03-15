package database;

import org.muffinapps.onlyaweek.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExamCursorAdapter extends CursorAdapter{

	public ExamCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup view) {
		LayoutInflater inflater = LayoutInflater.from(view.getContext());
        View retView = inflater.inflate(R.layout.exam, view, false);
 
        return retView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView name = (TextView) view.findViewById(R.id.nameExam);
		name.setText(cursor.getString(1));
		
		TextView date = (TextView) view.findViewById(R.id.dateExam);
		date.setText(cursor.getString(2));
		
	}

}
