package org.muffinapps.onlyaweek;

import java.util.GregorianCalendar;

import org.emud.support.v4.content.ObserverCursorLoader;
import org.muffinapps.onlyaweek.database.ExamDataSource;
import org.muffinapps.onlyaweek.database.QueryExam;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;

@SuppressLint("NewApi")
public class PlanningFragment extends Fragment implements OnClickListener, LoaderCallbacks<Cursor>, OnCheckedChangeListener, PlanningUpdateDialogFragment.OnConfirmListener{
	private static final String ID_KEY = "id";
	
	private long id = -1;
	private PlanningListener listener;
	private QueryExam query;
	private Checkable planningCheckable;

	private int remainingPages, totalPages, revisionDays;
	private GregorianCalendar examDate;

	public static Bundle getArgsAsBundle(long id){
		Bundle bundle = new Bundle();
		
		bundle.putLong(ID_KEY, id);
		
		return bundle;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		examDate = new GregorianCalendar();
		
		if(savedInstanceState != null){
			id = savedInstanceState.getLong(ID_KEY);
			android.util.Log.d("PLF", "onCreate id:" + id);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance){
		View view = inflater.inflate(R.layout.fragment_planning, container, false);
		
		LinearLayout l = (LinearLayout) view.findViewById(R.id.layoutCheckBox);
		
		TextView text = new TextView(this.getActivity());
		text.setText("Planificando");
		text.setTextAppearance(getActivity(), R.style.ValueHeaderText);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 2);
		
		l.addView(text, lp);
		
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
		
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH ){
			Switch button = new Switch(this.getActivity());
			button.setOnCheckedChangeListener(this);
			button.setTextOn("Si");
			button.setTextOff("No");
			button.setId(1);
			button.setText("");
			l.addView(button, lp);
			planningCheckable = button;
		}else{
			CheckBox button = new CheckBox(this.getActivity());
			button.setOnCheckedChangeListener(this);
			button.setId(1);
			l.addView(button, lp);
			planningCheckable = button;
		}
		
		((EditText) view.findViewById(R.id.planning_revision)).addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				revisionDaysChanged(s);
			}
			
		});
		
		view.findViewById(R.id.planning_update).setOnClickListener(this);
		
		return view;
	}
	
	private void revisionDaysChanged(Editable s) {
		String revisionDaysString = s.toString();
		int newRevisionDays;
		
		if(revisionDaysString.length() > 0){
			newRevisionDays = Integer.parseInt(revisionDaysString);
		}else{
			newRevisionDays = 0;
		}
		
		if(revisionDays == newRevisionDays)
			return;
		
		revisionDays = newRevisionDays;
		updateRatioDisplay();
		
		if(listener != null)
			listener.onRevisionDaysSet(id, revisionDays);
	}

	@Override
	public void onActivityCreated(Bundle state){
		super.onActivityCreated(state);		

		Bundle args = getArguments();
		
		if(args != null)
			id = args.getLong(ID_KEY, -1);
		
		if(id != -1){
			ExamDataSource ds = ((OnlyAWeekApplication) getActivity().getApplicationContext()).getDataBase();
			query = new QueryExam(ds, id);
			getLoaderManager().restartLoader(0, null, this);			
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		
		savedInstanceState.putLong(ID_KEY, id);
	}

	@Override
	public void onClick(View v) {
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		PlanningUpdateDialogFragment fragment = new PlanningUpdateDialogFragment();
		
		fragment.setConfirmListener(this);
		fragment.show(fragmentManager, "planningUpdateDialog");
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		setDataLayoutEnabled((ViewGroup) getView().findViewById(R.id.layoutExamPlanningData), isChecked);
		
		if(listener != null)
			listener.onPlanningSet(id, isChecked);
	}
	
	public void setExamId(long newId){
		if(id == newId)
			return;
		
		id = newId;

		if(query == null){
			ExamDataSource ds = ((OnlyAWeekApplication) getActivity().getApplicationContext()).getDataBase();
			query = new QueryExam(ds, id);
		}
		
		query.setExamId(id);
		getLoaderManager().restartLoader(0, null, this);
	}

	public void setPlanningListener(PlanningListener planningListener) {
		this.listener = planningListener;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {		
		return new ObserverCursorLoader(getActivity(), query);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		populateData(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
	}

	private void populateData(Cursor cursor) {
		View view = getView();
		boolean planning = cursor.getInt(5) == 1;
		
		remainingPages = cursor.getInt(4);
		totalPages = cursor.getInt(3);
		revisionDays = cursor.getInt(6);
		examDate.setTimeInMillis(cursor.getLong(2));
		
		((TextView) view.findViewById(R.id.planning_total)).setText(""+totalPages);
		((EditText) view.findViewById(R.id.planning_revision)).setText(""+revisionDays);
		planningCheckable.setChecked(planning);
		
		setDataLayoutEnabled((ViewGroup) getView().findViewById(R.id.layoutExamPlanningData), planning);
		
		updateRatioDisplay();
	}

	private void updateRatioDisplay(){
		StudyingRatio ratio;
		View view = getView();		
		
		ratio = new StudyingRatio(remainingPages, examDate, revisionDays);

		((TextView) view.findViewById(R.id.planning_remaining)).setText(""+remainingPages);
		((TextView) view.findViewById(R.id.planning_ratio)).setText(ratio.getRatioString());
		((TextView) view.findViewById(R.id.planning_comment)).setText(ratio.getRatioComment());
	}
	
	private void setDataLayoutEnabled(ViewGroup viewGroup, boolean enabled){
		int n = viewGroup.getChildCount();
		
		for(int i=0; i<n; i++){
			View v = viewGroup.getChildAt(i);
			if (v instanceof ViewGroup) {
				setDataLayoutEnabled((ViewGroup) v, enabled);
	        } else {
	            v.setEnabled(enabled);
	        }
		}
	}
	
	public static interface PlanningListener{
		public void onPlanningSet(long id, boolean planning);
		public void onPlanningUpdated(long id, int pages);
		public void onRevisionDaysSet(long id, int revisionDays);
	}

	@Override
	public void planningUpdateConfirmed(int pages) {
		if(pages > remainingPages)
			pages = remainingPages;
		
		remainingPages -= pages;
		
		if(listener!=null)
			listener.onPlanningUpdated(id, pages);
		
		updateRatioDisplay();
	}
}
