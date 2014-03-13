package org.muffinapps.onlyaweek;

import prueba.DataSubject;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterSubject extends ArrayAdapter{
	private static class ViewHolder {
		TextView nameSubject;
		TextView dateSubject;
		TextView totalPag;
		TextView remainingPag;
		TextView assignedPag;
	}

	private Activity context;
	private Object[] data;
	
	public AdapterSubject(Activity context, int textViewResourceId,
			Object[] objects) {
		super(context, textViewResourceId, objects);
		
		this.context = context;
		this.data = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		/*
		--Version mejorada--
		
		View item = convertView;
		ViewHolder holder;
		
		if(item == null){
			LayoutInflater inflater = context.getLayoutInflater();
			item = inflater.inflate(R.layout.subject, null);
			
			holder = new ViewHolder();
			holder.nameSubject = (TextView) item.findViewById(R.id.nameSubject);
			holder.dateSubject = (TextView) item.findViewById(R.id.dateSubject);
			holder.totalPag = (TextView) item.findViewById(R.id.totalPag);
			holder.remainingPag = (TextView) item.findViewById(R.id.remainingPag);
			holder.assignedPag = (TextView) item.findViewById(R.id.assignedPag);
			
			item.setTag(holder);
		}else{
			holder = (ViewHolder)item.getTag();
		}
		
		holder.nameSubject.setText(((DataSubject) data[position]).name);
		holder.dateSubject.setText(((DataSubject) data[position]).date);
		holder.totalPag.setText(((DataSubject) data[position]).totalPag);
		holder.remainingPag.setText(((DataSubject) data[position]).remainingPag);
		holder.assignedPag.setText(((DataSubject) data[position]).assignedPag);
		*/
		
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.subject, null);
		
		TextView date = (TextView) item.findViewById(R.id.dateSubject);
		date.setText(((DataSubject) data[position]).date);
		
		TextView total = (TextView) item.findViewById(R.id.totalPag);
		total.setText(""+((DataSubject) data[position]).totalPag);
		
		TextView remaining = (TextView) item.findViewById(R.id.remainingPag);
		remaining.setText(""+((DataSubject) data[position]).remainingPag);
		
		TextView assigned = (TextView) item.findViewById(R.id.assignedPag);
		assigned.setText(""+((DataSubject) data[position]).assignedPag);
		
		return item;
	}


}
