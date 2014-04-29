package org.muffinapps.onlyaweek;

import org.muffinapps.onlyaweek.AddNewExamFragment.OnConfirmListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class AddNewExamActivity extends FragmentActivity implements OnConfirmListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_add_new_exam);
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		AddNewExamFragment addFragment = new AddNewExamFragment();
		addFragment.setOnConfirmListener(this);
		fragmentTransaction.replace(R.id.add_exam_content_frame, addFragment);
		fragmentTransaction.commit();
		
	}

	@Override
	public void onAdd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
		
	}


}
