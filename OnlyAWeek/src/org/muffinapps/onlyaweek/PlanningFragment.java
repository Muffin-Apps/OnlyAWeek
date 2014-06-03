package org.muffinapps.onlyaweek;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

@SuppressLint("NewApi")
public class PlanningFragment extends Fragment implements OnClickListener{
	private static final String ID_KEY = "id";

	public static Bundle getArgsAsBundle(long id){
		Bundle bundle = new Bundle();
		
		bundle.putLong(ID_KEY, id);
		
		return bundle;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance){
		View view = inflater.inflate(R.layout.fragment_planning, container, false);
		
		LinearLayout l = (LinearLayout) view.findViewById(R.id.layoutCheckBox);
		
		TextView text = new TextView(this.getActivity());
		text.setText("Planificando");
		text.setTextAppearance(getActivity(), R.style.ValueHeaderText);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.75f);
		
		l.addView(text, lp);
		
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.25f);
		
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES. ICE_CREAM_SANDWICH ){
			Switch button = new Switch(this.getActivity());
			button.setTextOn("Si");
			button.setTextOn("No");
			button.setId(1);
			l.addView(button, lp);
		}else{
			CheckBox button = new CheckBox(this.getActivity());
			button.setText("Planificando");
			button.setId(1);
			l.addView(button, lp);
		}
		
		return view;
	}

	@Override
	public void onClick(View v) {
		Checkable button = (Checkable) v;
		
		if(v.isActivated()){
			
		}else{
			
		}
	}
	
	
}
