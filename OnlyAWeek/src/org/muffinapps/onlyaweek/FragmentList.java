package org.muffinapps.onlyaweek;

import prueba.DataSubject;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentList extends Fragment{
	private DataSubject[] data = new DataSubject[]{
				new DataSubject("Dispositivos Moviles", "05/06/2014", 1200, 80, 2),
				new DataSubject("DIU", "20/06/2014", 200, 20, 20),
				new DataSubject("TFG", "15/06/2014", 2200, 120, 1),
				new DataSubject("MDA", "10/06/2014", 800, 60, 8)
				};
	private ListView list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved){
	
		return inflater.inflate(R.layout.list_subject, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle state){
		super.onActivityCreated(state);
		
		list = (ListView)this.getView().findViewById(R.id.listSubject);
		//list.setAdapter(new AdapterSubject(this, R.layout.subject, data));
	}
}
