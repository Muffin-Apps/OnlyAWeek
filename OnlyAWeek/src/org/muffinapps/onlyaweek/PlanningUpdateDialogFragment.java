package org.muffinapps.onlyaweek;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

public class PlanningUpdateDialogFragment extends DialogFragment implements OnClickListener{
	private OnConfirmListener listener;
	
	public OnConfirmListener getConfirmListener() {
		return listener;
	}

	public void setConfirmListener(OnConfirmListener listener) {
		this.listener = listener;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_planningupdate, null);
		
		builder.setTitle(R.string.planningupdate_title)
			.setPositiveButton(R.string.accept_label, this)
			.setNegativeButton(R.string.cancel_label, this)
			.setView(view);
		
		return builder.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which){
		case AlertDialog.BUTTON_POSITIVE:
			if(listener != null){
				String pagesString = ((EditText) getDialog().findViewById(R.id.pages_edit)).getText().toString();
				if(!pagesString.isEmpty())
					listener.planningUpdateConfirmed(Integer.parseInt(pagesString));
			}
			dismiss();
			break;
		case AlertDialog.BUTTON_NEGATIVE:
			dismiss();
			break;
		default:
			break;
		}
	}

	
	public static interface OnConfirmListener{
		public void planningUpdateConfirmed(int pages);
	}
}
